package me.lefted.lunacyforge.guiapi;

import java.util.ArrayList;

import me.lefted.lunacyforge.utils.Logger;

public class Panel {

    // ATTRIBUTES
    private ArrayList<Element> elements;
    private int posX;
    private int posY;

    // CONSTRUCTOR
    public Panel(int x, int y) {
	this.posX = x;
	this.posY = y;
	this.elements = new ArrayList();
    }

    // METHODS
    public void draw(int mouseX, int mouseY) {
	for (Element element : this.elements) {
	    element.draw();
	}
    }

    private boolean newClick = false;

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	newClick = true;
	for (Element element : this.elements) {
	    element.mouseClicked(mouseX, mouseY, mouseButton);
	}
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	for (Element element : this.elements) {
	    element.mouseReleased(mouseX, mouseY, mouseButton);
	}
    }

    private int lastMouseY = 0;

    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
	// Logger.logConsole("button:" + mouseButton + "y:" + mouseY + "time:" + timeSinceClick);

	// final boolean newClick = timeSinceClick < lastTimeSinceClick || timeSinceClick == 0;
	lastMouseY = (newClick) ? mouseY : lastMouseY;
	final int diffY = mouseY - lastMouseY;

	this.setY(this.getY() + diffY);

	newClick = false;

	for (Element element : this.elements) {
	    element.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
	}
	lastMouseY = mouseY;
    }

    public void keyTyped(char typedChar, int keyCode) {
	for (Element element : this.elements) {
	    element.keyTyped(typedChar, keyCode);
	}
    }

    public void setX(int x) {
	final int offsetX = x - this.posX;
	this.posX = x;

	// offset the elements accordingly
	for (Element element : this.elements) {
	    element.setPosX(element.getPosX() + offsetX);
	}
    }

    public void setY(int y) {
	final int offsetY = y - this.posY;
	this.posY = y;

	// offset the elements accordingly
	for (Element element : this.elements) {
	    element.setPosY(element.getPosY() + offsetY);
	}
    }

    public int getX() {
	return this.posX;
    }

    public int getY() {
	return this.posY;
    }

    public ArrayList<Element> getElements() {
	return this.elements;
    }

}
