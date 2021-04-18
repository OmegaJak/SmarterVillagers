package omegajak.smartervillagers.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public class HandledScreenMixin extends Screen {
    @Shadow protected int x;

    @Shadow protected int backgroundWidth;

    @Shadow protected int y;

    protected HandledScreenMixin(Text title) {
        super(title);
    }
}
