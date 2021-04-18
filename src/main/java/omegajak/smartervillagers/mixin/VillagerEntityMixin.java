package omegajak.smartervillagers.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;
import net.minecraft.village.*;
import net.minecraft.world.World;
import omegajak.smartervillagers.SmarterVillagers;
import omegajak.smartervillagers.ai.brain.SmarterVillagerActivities;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntityMixin {
    @Shadow public abstract VillagerData getVillagerData();

    @Override
    protected void fillRecipesFromPool(TradeOfferList recipeList, TradeOffers.Factory[] pool, int count, CallbackInfo ci) {
        World world = this.getMerchantWorld();
        if (world == null || world.isClient || !SmarterVillagers.getCurrentConfig().duplicateTradesConfig.enabled) return;

        System.out.println("Fill recipes override");
        Box searchBox = new Box(getBlockPos()).expand(SmarterVillagers.getCurrentConfig().duplicateTradesConfig.searchRange);
        List<VillagerEntity> villagersWithSameProfessionInRange = world.getEntitiesByType(EntityType.VILLAGER, searchBox, this::shouldCheckTradesAgainst);
        System.out.println("Found " + villagersWithSameProfessionInRange.size() + " other villagers of the same profession within " + SmarterVillagers.getCurrentConfig().duplicateTradesConfig.searchRange + " blocks.");

        ArrayList<TradeOffer> existingOffers = new ArrayList<>();
        for (VillagerEntity otherVillager : villagersWithSameProfessionInRange) {
            existingOffers.addAll(otherVillager.getOffers());
        }

        ArrayList<TradeOffer> offersNeedingReplacement = new ArrayList<>();
        for (int i = recipeList.size() - count; i < recipeList.size(); i++) {
            TradeOffer justAddedOffer = recipeList.get(i);
            if (isAnyOfferDuplicate(existingOffers, justAddedOffer)) {
                offersNeedingReplacement.add(recipeList.get(i));
            }
        }

        VillagerData villagerData = getVillagerData();
        ArrayList<TradeOffer> offersToAvoid = Stream.concat(existingOffers.stream(), recipeList.stream()).collect(Collectors.toCollection(ArrayList::new));
        for (TradeOffer offerNeedingReplacement : offersNeedingReplacement) {
            String toPrint = "Found offer needing replacement: " + toString(offerNeedingReplacement) + ". ";
            TradeOffer replacementOffer = GetValidReplacementOffer(offersToAvoid, villagerData);
            if (replacementOffer != null) {
                recipeList.set(recipeList.indexOf(offerNeedingReplacement), replacementOffer);
                offersToAvoid.add(replacementOffer);
                toPrint += "Successfully replaced with " + toString(replacementOffer);
            } else {
                toPrint += "Failed to find an offer to replace it with after " + SmarterVillagers.getCurrentConfig().duplicateTradesConfig.newTradeAttempts + " attempts.";
            }

            System.out.println(toPrint);
        }
    }

    @Inject(at = @At("HEAD"), method = "initBrain(Lnet/minecraft/entity/ai/brain/Brain;)V")
    public void initBrainMixin(Brain<VillagerEntity> brain, CallbackInfo callbackInfo) {
        brain.setTaskList(SmarterVillagerActivities.FOLLOW_LEADER_ACTIVITY, SmarterVillagerActivities.createFollowTasks());
    }

    @Nullable
    private TradeOffer GetValidReplacementOffer(Collection<TradeOffer> offersToAvoidDuplicating, VillagerData villagerData) {
        Int2ObjectMap<TradeOffers.Factory[]> tradeOfferFactoriesMap = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(villagerData.getProfession());
        if (tradeOfferFactoriesMap != null && !tradeOfferFactoriesMap.isEmpty()) {
            TradeOffers.Factory[] factories = tradeOfferFactoriesMap.get(villagerData.getLevel());
            if (factories != null) {
                for (TradeOffers.Factory factory : factories) {
                    for (int i = 0; i < SmarterVillagers.getCurrentConfig().duplicateTradesConfig.newTradeAttempts; i++) {
                        TradeOffer tradeOffer = factory.create(getThis(), this.random);
                        if (tradeOffer != null && !isAnyOfferDuplicate(offersToAvoidDuplicating, tradeOffer)) {
                            return tradeOffer;
                        }
                    }
                }
            }
        }

        return null;
    }

    private VillagerEntity getThis() {
        return (VillagerEntity)(Object)this;
    }

    private String toString(TradeOffer tradeOffer) {
        String result = "[" + toString(tradeOffer.getOriginalFirstBuyItem());
        if (tradeOffer.getSecondBuyItem() != ItemStack.EMPTY) result += " + " + toString(tradeOffer.getSecondBuyItem());
        result += " -> " + toString(tradeOffer.getSellItem()) + "]";
        return result;
    }

    private String toString(ItemStack itemStack) {
        String baseString = itemStack.toString();

        if (itemStack.getTag() != null) {
            baseString += itemStack.getTag().toString();
        }

        return baseString;
    }

    private boolean isAnyOfferDuplicate(Collection<TradeOffer> offersToAvoidDuplicating, TradeOffer offer) {
        return offersToAvoidDuplicating.stream().anyMatch(offerToAvoidDuplicating -> areDuplicateOffers(offer, offerToAvoidDuplicating));
    }

    private boolean areDuplicateOffers(TradeOffer a, TradeOffer b) {
        if (isPurchasingItems(a)) {
            return areOfferedItemStacksTradingDuplicates(a.getOriginalFirstBuyItem(), b.getOriginalFirstBuyItem())
                && areOfferedItemStacksTradingDuplicates(a.getSecondBuyItem(), b.getSecondBuyItem());
        } else {
            return areOfferedItemStacksTradingDuplicates(a.getSellItem(), b.getSellItem());
        }
    }

    private boolean areOfferedItemStacksTradingDuplicates(ItemStack a, ItemStack b) {
        return ItemStack.areItemsEqual(a, b) && ItemStack.areTagsEqual(a, b);
    }

    private boolean isPurchasingItems(TradeOffer tradeOffer) {
        return tradeOffer.getOriginalFirstBuyItem().getItem() != Items.EMERALD;
    }

    private boolean shouldCheckTradesAgainst(VillagerEntity otherVillager) {
        return otherVillager.getUuid() != getUuid() && hasSameProfession(otherVillager);
    }

    private boolean hasSameProfession(VillagerEntity otherVillager) {
        return otherVillager.getVillagerData().getProfession() == getVillagerData().getProfession();
    }
}
