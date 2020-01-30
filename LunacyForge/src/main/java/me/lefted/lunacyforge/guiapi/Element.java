package me.lefted.lunacyforge.guiapi;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public abstract class Element extends Gui {

    // ATTRIBUTES
    private int posX;
    private int posY;
    private boolean visible = true;

    // METHODS
    /*
     * please choose for yourself which methods you want to override
     */
    public void draw(int mouseX, int mouseY, float partialTicks) {

    }

    /*
     * should handle the click and return if the element was clicked
     */
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
	return false;
    }

    /*
     * should handle the release and return if the mouse was released on the element
     */
    public boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
	return false;
    }

    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {

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
