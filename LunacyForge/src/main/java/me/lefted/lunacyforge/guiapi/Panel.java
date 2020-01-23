package me.lefted.lunacyforge.guiapi;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.gui.GuiScreen;

public class Panel extends GuiScreen {

    // ATTRIBUTES
    private ArrayList<Element> elements;
    private int posX;
    private int posY;
    private boolean drawDefaultBackground = true;
    private boolean horizontalScrolling = false;
    private boolean verticalScrolling = false;
    private boolean verticalWheelScrolling = false;
    private boolean newClick = false;
    private int lastMouseY = 0;
    private int lastMouseX = 0;
    private int scrollMultiplier = 5;
    private int initialX;
    private int initialY;
    private Borders borders;

    // CONSTRUCTOR
    public Panel(int x, int y) {
	this.posX = x;
	this.posY = y;
	this.initialX = x;
	this.initialY = y;
	this.elements = new ArrayList();
	this.borders = new Borders();
    }

    // METHODS
    public void scrollVerticalByAmount(int amount) {
	final int futurePosY = this.getY() + amount;

	boolean flag = !this.borders.isMaxYDefined() || futurePosY <= this.borders.getMaxY();
	boolean flag1 = !this.borders.isMinYDefined() || futurePosY >= this.borders.getMinY();

	if (flag && flag1) {
	    this.setY(this.getY() + amount);
	}
    }

    public void scrollHorizontalByAmount(int amount) {
	final int futurePosX = this.getX() + amount;

	boolean flag = !this.borders.isMaxXDefined() || futurePosX <= this.borders.getMaxX();
	boolean flag1 = !this.borders.isMinXDefined() || futurePosX >= this.borders.getMinX();

	if (flag && flag1) {
	    this.setX(this.getX() + amount);
	}
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	super.drawScreen(mouseX, mouseY, partialTicks);

	if (this.isDrawDefaultBackground()) {
	    this.drawDefaultBackground();
	}
	for (Element element : this.elements) {
	    element.draw(mouseX, mouseY, partialTicks);
	}
    }

    @Override
    public void handleMouseInput() throws IOException {
	super.handleMouseInput();

	if (this.isVerticalWheelScrolling()) {
	    int delta = Mouse.getEventDWheel();
	    if (delta != 0) {
		if (delta > 0) {
		    delta = -1;
		} else if (delta < 0) {
		    delta = 1;
		}
		this.scrollVerticalByAmount(delta * this.scrollMultiplier);
	    }
	}
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	this.newClick = true;
	for (Element element : this.elements) {
	    element.mouseClicked(mouseX, mouseY, mouseButton);
	}
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	for (Element element : this.elements) {
	    element.mouseReleased(mouseX, mouseY, mouseButton);
	}
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
	if (this.isVerticalScrolling()) {
	    this.lastMouseY = (this.newClick) ? mouseY : this.lastMouseY;
	    final int diffY = mouseY - this.lastMouseY;
	    // this.setY(this.getY() + diffY);
	    this.scrollVerticalByAmount(diffY);
	}

	if (this.isHorizontalScrolling()) {
	    this.lastMouseX = (this.newClick) ? mouseX : this.lastMouseX;
	    final int diffX = mouseX - this.lastMouseX;
	    // this.setX(this.getX() + diffX);
	    this.scrollHorizontalByAmount(diffX);
	}

	for (Element element : this.elements) {
	    element.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
	}
	this.newClick = false;
	this.lastMouseY = mouseY;
	this.lastMouseX = mouseX;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
	super.keyTyped(typedChar, keyCode);

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

    public boolean isDrawDefaultBackground() {
	return drawDefaultBackground;
    }

    public void setDrawDefaultBackground(boolean drawDefaultBackground) {
	this.drawDefaultBackground = drawDefaultBackground;
    }

    public boolean isHorizontalScrolling() {
	return horizontalScrolling;
    }

    public void setHorizontalScrolling(boolean horizontalScrolling) {
	this.horizontalScrolling = horizontalScrolling;
    }

    public boolean isVerticalScrolling() {
	return verticalScrolling;
    }

    public void setVerticalScrolling(boolean verticalScrolling) {
	this.verticalScrolling = verticalScrolling;
    }

    public boolean isVerticalWheelScrolling() {
	return verticalWheelScrolling;
    }

    public void setVerticalWheelScrolling(boolean verticalWheelScrolling) {
	this.verticalWheelScrolling = verticalWheelScrolling;
    }

    public int getScrollMultiplier() {
	return scrollMultiplier;
    }

    public void setScrollMultiplier(int scrollMultiplier) {
	this.scrollMultiplier = scrollMultiplier;
    }

    public int getInitialX() {
	return initialX;
    }

    public int getInitialY() {
	return initialY;
    }

    public Borders getBorders() {
	return borders;
    }
}
