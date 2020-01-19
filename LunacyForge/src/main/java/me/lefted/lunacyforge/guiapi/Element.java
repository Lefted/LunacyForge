package me.lefted.lunacyforge.guiapi;

public abstract class Element {

    // ATTRIBUTES
    private int posX;
    private int posY;
    private boolean visible;

    // METHODS
    public abstract void draw();
    public abstract void handleMouseInput(int mouseX, int mouseY, int mouseButton);
    
    public int getPosX() {
	return posX;
    }

    public void setPosX(int posX) {
	this.posX = posX;
    }

    public int getPosY() {
	return posY;
    }

    public void setPosY(int posY) {
	this.posY = posY;
    }

    public boolean isVisible() {
	return visible;
    }

    public void setVisible(boolean visible) {
	this.visible = visible;
    }

}
