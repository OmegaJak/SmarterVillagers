package omegajak.smartervillagers.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import omegajak.smartervillagers.SmarterVillagers;
import omegajak.smartervillagers.network.Networking;

@Environment(EnvType.CLIENT)
public class FollowButtonWidget extends TexturedButtonWidget {
    private static final Identifier TEXTURE = SmarterVillagers.id("textures/gui/follow_me_button_2.png");

    private static final int HOVERED_V_OFFSET = 13;
    private static final int TEXTURE_WIDTH = 12;
    private static final int TEXTURE_HEIGHT = 25;
    private static final int WIDTH = 12;
    private static final int HEIGHT = 12;

    public FollowButtonWidget(int x, int y) {
        super(x, y, WIDTH, HEIGHT, 0, 0, HOVERED_V_OFFSET, TEXTURE, TEXTURE_WIDTH, TEXTURE_HEIGHT, null, ButtonWidget.EMPTY, new LiteralText(""));
    }

    @Override
    public void renderToolTip(MatrixStack matrices, int mouseX, int mouseY) {
        if (MinecraftClient.getInstance().currentScreen != null && this.isHovered()) MinecraftClient.getInstance().currentScreen.renderTooltip(matrices, new LiteralText("Follow Me!"), mouseX, mouseY);
    }

    @Override
    public void onPress() {
        Networking.sendFollowPacket();
    }
}
