package omegajak.smartervillagers.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.text.Text;
import omegajak.smartervillagers.SmarterVillagers;
import omegajak.smartervillagers.client.FollowButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin extends HandledScreenMixin {

    protected MerchantScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init()V")
    private void initMixin(CallbackInfo callbackInfo) {
        if (!SmarterVillagers.getCurrentConfig().followButtonConfig.enabled) return;

        int buttonX = this.x + 4;
        int buttonY = this.y + 4;
        FollowButtonWidget followButtonWidget = new FollowButtonWidget(buttonX, buttonY);
        this.addButton(followButtonWidget);
    }
}
