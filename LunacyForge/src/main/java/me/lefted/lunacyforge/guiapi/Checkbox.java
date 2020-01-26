package me.lefted.lunacyforge.guiapi;

import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Checkbox extends Element {

    private ResourceLocation location = new ResourceLocation("lunacyforge", "checkboxalt.png");

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final DrawUtils utils = DrawUtils.INSTANCE;
	utils.bindTexture(this.location);
	utils.drawTexture((double) (this.getPosX() + 1), (double) (this.getPosY() - 1), 256.0D, 256.0D, 16.0D, 16.0D, 1.0F);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }

    @Override
    public void updateScreen() {
    }

}
