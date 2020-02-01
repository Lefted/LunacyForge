package me.lefted.lunacyforge.guiapi;

import net.minecraft.client.Minecraft;

public class Label extends Element {

    // ATTRIBUTES
    private String text;
    private int color;

    // CONSTRUCOTR
    public Label(int x, int y, String text, int color) {
	super.setPosX(x);
	super.setPosY(y);
	this.text = text;
	this.color = color;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	Minecraft.getMinecraft().fontRendererObj.drawString(this.text, this.getPosX(), this.getPosY(), this.color);
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

}
