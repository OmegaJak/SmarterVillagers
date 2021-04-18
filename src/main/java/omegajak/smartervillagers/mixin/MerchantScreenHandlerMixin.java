package omegajak.smartervillagers.mixin;

import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.village.Merchant;
import omegajak.smartervillagers.api.MerchantProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MerchantScreenHandler.class)
public class MerchantScreenHandlerMixin implements MerchantProvider {
    @Shadow @Final private Merchant merchant;

    @Override
    public Merchant getMerchant() {
        return merchant;
    }
}
