package me.lefted.lunacyforge.clickgui.elements.api;

import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public abstract class Element extends Gui {

    // ATTRIBUTES
    protected int posX;
    protected int posY;
    private boolean visible = true;

    // METHODS
    /*
     * please choose for yourself which methods you want to override
     */
    public void draw(int mouseX, int mouseY, float partialTicks) {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {

    }

    public boolean isMouseOver(int mouseX, int mouseY) {
	return false;
    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public void updateScreen() {

    }

    public int getPosX() {
	return posX;
    }

    public void setPosX(int posX) {
	this.posX = posX;
    }

    public int getPosY() {
	return posY;
    }

    public void setPosY(int posY) {
	this.posY = posY;
    }

    public boolean isVisible() {
	return visible;
    }

    public void setVisible(boolean visible) {
	this.visible = visible;
    }
}
