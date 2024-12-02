package planetsaturn.industry.screen;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import planetsaturn.industry.SaturnIndustry;

public class CrucibleScreen extends HandledScreen<CrucibleScreenHandler> {

    public static final Identifier TEXTURE = new Identifier(SaturnIndustry.MOD_ID, "textures/gui/crucible.png");

    public CrucibleScreen(CrucibleScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgressArrow(matrices, x, y);
        renderSpeedupProgressArrow(matrices, x, y);
        renderFuelTime(matrices, x, y);
    }

    private void renderProgressArrow(MatrixStack matrices, int x, int y) {
        if(handler.isCrafting()) {
            drawTexture(matrices, x + 79, y + 35, 176, 14, handler.getScaledProgress(), 17);
        }
    }

    private void renderSpeedupProgressArrow(MatrixStack matrices, int x, int y) {
            drawTexture(matrices, x + 45, y + 70 - handler.getScaledSpeedupProgress(), 176, 31, 5, handler.getScaledSpeedupProgress() + 1);
    }

    private void renderFuelTime(MatrixStack matrices, int x, int y) {
        drawTexture(matrices, x + 57, y + 50 - handler.getScaledFuelTime(), 176, 14 - handler.getScaledFuelTime(), 14, handler.getScaledFuelTime());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
