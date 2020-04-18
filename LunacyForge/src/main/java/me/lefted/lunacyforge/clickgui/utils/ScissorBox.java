package me.lefted.lunacyforge.clickgui.utils;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/* Contains the proportions of the settings screen and provides functionality to cut off there. */
public class ScissorBox {

    // ATTRIBUTES
    private int x;
    private int y;
    private int width;
    private int height;

    // CONSTRUCTOR
    public ScissorBox(int x, int y, int width, int height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
    }

    // METHODS
    // Credits Wurst @Alexander1998
    public void cut() {
	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
	int factor = sr.getScaleFactor();
	int bottomY = width + height;
	// int bottomY = (Minecraft.getMinecraft()).currentScreen.height - yend;
	GL11.glScissor(x * factor, bottomY * factor, width * factor, height * factor);
    }

    // GETTERS AND SETTERS
    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }
}
