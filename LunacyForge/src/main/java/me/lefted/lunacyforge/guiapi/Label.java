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
    public void draw(int mouseX, int mouseY, float partialTicks) {
	Minecraft.getMinecraft().fontRendererObj.drawString(this.text, this.getPosX(), this.getPosY(), 0xFF);
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

}
