package me.lefted.lunacyforge.guiapi;

import net.minecraft.client.Minecraft;

public class Label extends Element {

    // ATTRIBUTES
    private String text;

    // CONSTRUCOTR
    public Label(int x, int y, String text) {
	super.setPosX(x);
	super.setPosY(y);
	this.text = text;
    }

    @Override
    public void draw() {
	Minecraft.getMinecraft().fontRendererObj.drawString(this.text, this.getPosX(), this.getPosY(), 0xFF);
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

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }
}
