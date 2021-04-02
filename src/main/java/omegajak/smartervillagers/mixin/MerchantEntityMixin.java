package omegajak.smartervillagers.mixin;

import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantEntity.class)
public abstract class MerchantEntityMixin extends LivingEntityMixin {
    @Shadow public abstract World getMerchantWorld();

    @Shadow @Nullable protected TradeOfferList offers;

    @Shadow public abstract void trade(TradeOffer offer);

    @Inject(at = @At("TAIL"), method = "fillRecipesFromPool(Lnet/minecraft/village/TradeOfferList;[Lnet/minecraft/village/TradeOffers$Factory;I)V")
    protected void fillRecipesFromPool(TradeOfferList recipeList, TradeOffers.Factory[] pool, int count, CallbackInfo ci) {
        System.out.println("Filled recipes from pool");
    }
}
