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
	Logger.logChatMessage("draw");
	for (Element element : this.elements) {
	    element.draw();
	}
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	for (Element element : this.elements) {
	    element.mouseClicked(mouseX, mouseY, mouseButton);
	}
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	for (Element element : this.elements) {
	    element.mouseReleased(mouseX, mouseY, mouseButton);
	}
    }

    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
	for (Element element : this.elements) {
	    element.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
	}
    }

    public void keyTyped(char typedChar, int keyCode) {
	for (Element element : this.elements) {
	    element.keyTyped(typedChar, keyCode);
	}
    }

    public void setX(int x) {
	this.posX = x;

	// update elements positions relative
	for (Element element : this.elements) {
	    element.setPosX(element.getPosX() + x);
	}
    }

    public void setY(int y) {
	this.posY = y;

	// update elements positions relative
	for (Element element : this.elements) {
	    element.setPosY(element.getPosY() + y);
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
