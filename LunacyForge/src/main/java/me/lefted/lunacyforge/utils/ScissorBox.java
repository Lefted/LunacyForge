package me.lefted.lunacyforge.utils;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.SearchBar;
import me.lefted.lunacyforge.clickgui.screens.SettingsScreen;
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
	int bottomY = (Minecraft.getMinecraft()).currentScreen.height - (y + height);

	GL11.glScissor(x * factor, factor * bottomY, width * factor, height * factor);
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
