package me.lefted.lunacyforge.testgui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import me.lefted.lunacyforge.guiapi.Label;
import me.lefted.lunacyforge.guiapi.Panel;
import net.minecraft.client.gui.GuiScreen;

public class Screen extends Panel {

    public Screen() {
	super(0, 0);
    }

    @Override
    public void initGui() {
	this.getElements().add(new Label(10, 10, "Party"));
	Keyboard.enableRepeatEvents(true);
	this.setScrollMultiplier(50);
	
	this.setVerticalScrolling(true);
	this.setVerticalWheelScrolling(true);
	this.setHorizontalScrolling(true);
	
	this.getBorders().setMaxX(350).setMinX(0).setMaxY(200).setMinY(0);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
	super.keyTyped(typedChar, keyCode);

	if (keyCode == Keyboard.KEY_DOWN) {
	    this.setY(this.getY() + 5);
	} else if (keyCode == Keyboard.KEY_UP) {
	    this.setY(this.getY() - 5);
	} else if (keyCode == Keyboard.KEY_RIGHT) {
	    this.setX(this.getX() + 5);
	} else if (keyCode == Keyboard.KEY_LEFT) {
	    this.setX(this.getX() - 5);
	}
    }
}
