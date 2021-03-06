package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.clickgui.elements.api.Element;
import me.lefted.lunacyforge.utils.DrawUtils;

public class Rectangle extends Element {

    // ATTRIBUTES
    private int width;
    private int height;
    private int color;
    private boolean selection = false;

    // CONSTRUCOTR
    public Rectangle(int x, int y, int width, int height, int color) {
	this.posX = x;
	this.posY = y;
	this.width = width;
	this.height = height;
	this.color = color;
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	if (this.selection) {
	    utils.drawSelectionBox(this.getPosX(), this.width, this.getPosY(), this.height);
	} else {
	    utils.drawRectWidthHeight(this.getPosX(), this.getPosY(), this.width, this.height, color);
	}
    }

    /* sets if this should draw a selection box instead of a filled rectangle */
    public void setSelection(boolean selection) {
	this.selection = selection;
    }

    public int getHeight() {
	return height;
    }

    public void setHeight(int height) {
	this.height = height;
    }

    public int getWidth() {
	return width;
    }

    public void setWidth(int width) {
	this.width = width;
    }

    public int getColor() {
	return color;
    }

    public void setColor(int color) {
	this.color = color;
    }
}
