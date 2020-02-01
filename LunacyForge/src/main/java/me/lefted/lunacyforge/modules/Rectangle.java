package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.guiapi.Element;
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
	    utils.drawRect(this.getPosX(), this.getPosY(), this.width, this.height, new Integer(color).floatValue());
	}
    }

    /*
     * should this draw a selection box instead
     */
    public void setSelection(boolean selection) {
	this.selection = selection;
    }
}
