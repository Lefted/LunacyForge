package me.lefted.lunacyforge.testgui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import me.lefted.lunacyforge.guiapi.Button;
import me.lefted.lunacyforge.guiapi.Label;
import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.guiapi.Textfield;
import net.minecraft.client.gui.GuiTextField;

public class Screen extends Panel {

    public Screen() {
	super(0, 0);
    }

    @Override
    public void initGui() {
	this.getElements().add(new Label(10, 10, "Party"));
	Textfield field;
	this.getElements().add(field = new Textfield(200, 200, 200, 20));
	field.setText("Nicer text");

	Button button;
	this.getElements().add(button = new Button(100, 10, 200, 20, "Just a button"));
	button.setCallback(() -> System.out.println("callback"));
	Keyboard.enableRepeatEvents(true);
	this.setScrollMultiplier(50);

	this.setVerticalScrolling(true);
	this.setVerticalWheelScrolling(true);
	this.setHorizontalScrolling(true);

	this.getBorders().setMaxX(350).setMinX(0).setMaxY(200).setMinY(0);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
	super.keyTyped(typedChar, keyCode);

	// if (keyCode == Keyboard.KEY_DOWN) {
	// this.setY(this.getY() + 5);
	// } else if (keyCode == Keyboard.KEY_UP) {
	// this.setY(this.getY() - 5);
	// } else if (keyCode == Keyboard.KEY_RIGHT) {
	// this.setX(this.getX() + 5);
	// } else if (keyCode == Keyboard.KEY_LEFT) {
	// this.setX(this.getX() - 5);
	// }
    }
}
