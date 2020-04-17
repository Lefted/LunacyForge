package me.lefted.lunacyforge.clickgui.utils;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ScissorBox {

    // ATTRIBUTES
    private int boxTop;
    private int boxBottom;

    // CONSTRUCTOR
    public ScissorBox(int boxTop, int boxBottom) {
	super();
	this.boxTop = boxTop;
	this.boxBottom = boxBottom;
    }

    // METHODS
    // Credits Wurst @Alexander1998
    public void cut(int x, int y, int xend, int yend) {
	int width = xend - x;
	int height = yend - y;
	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
	int factor = sr.getScaleFactor();
	int bottomY = (Minecraft.getMinecraft()).currentScreen.height - yend;
	GL11.glScissor(x * factor, bottomY * factor, width * factor, height * factor);
    }

    // GETTERS AND SETTERS
    public int getBoxTop() {
	return boxTop;
    }

    public int getBoxBottom() {
	return boxBottom;
    }
}
