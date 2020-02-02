package me.lefted.lunacyforge.testgui;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import me.lefted.lunacyforge.guiapi.Button;
import me.lefted.lunacyforge.guiapi.ButtonSelection;
import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.guiapi.Slider;
import me.lefted.lunacyforge.guiscreen.interpreter.ModuleScreen;
import me.lefted.lunacyforge.modules.Rectangle;
import me.lefted.lunacyforge.utils.ColorUtils;
import net.minecraft.client.Minecraft;

public class Screen extends Panel {

    public Screen() {
	super(0, 0);
    }

    @Override
    public void initGui() {
	this.getElements().clear();
	// KeybindButton keybindBtn = new KeybindButton(150, 150, 200, 20, 0);
	// this.getElements().add(keybindBtn);
	//
	// Checkbox box;
	// this.getElements().add(box = new Checkbox(20, 40, true));
	//
	// this.getElements().add(new Label(10, 10, "Party"));
	// Textfield field;
	// this.getElements().add(field = new Textfield(200, 200, 200, 20));
	// field.setText("Nicer text");
	// field.setConsumer(new Consumer<String>() {
	// @Override
	// public void accept(String t) {
	// System.out.println(t);
	// }
	// });
	//
	// Button button;
	// this.getElements().add(button = new Button(100, 10, 200, 20, "Just a button"));
	// button.setCallback(() -> System.out.println("callback"));
	// Keyboard.enableRepeatEvents(true);

//	Rectangle rect = new Rectangle(40, 150, 120, 80, ColorUtils.toRGB(Color.PINK));
//	this.getElements().add(rect);
//
//	Button schoen = new Button(30, 40, 200, 20, "schön");
//	Button traurig = new Button(30, 80, 200, 20, "traurig");
//	Button cool = new Button(30, 120, 200, 20, "modules");
//
//	schoen.setCallback(() -> {
//	    System.out.println("schön");
//	});
//	cool.setCallback(() -> {
//	    Minecraft.getMinecraft().displayGuiScreen(new ModuleScreen());
//	});
//	traurig.setCallback(() -> {
//	    System.out.println("traurig");
//	});
//
//	ButtonSelection selection = new ButtonSelection(0, traurig, cool, schoen);
//	this.getElements().add(selection);
//
	Slider slider = new Slider(50, 100, 200, 0, 100, 1, 0);
	this.getElements().add(slider);
	
	
	this.setScrollMultiplier(50);

	this.setVerticalScrolling(true);
	this.setVerticalWheelScrolling(true);
	this.setHorizontalScrolling(true);

	this.getBorders().setMaxX(350).setMinX(0).setMaxY(200).setMinY(0);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    // @Override
    // public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    // super.mouseClicked(mouseX, mouseY, mouseButton);
    // }

    // @Override
    // public void keyTyped(char typedChar, int keyCode) throws IOException {
    // super.keyTyped(typedChar, keyCode);
    //
    // // if (keyCode == Keyboard.KEY_DOWN) {
    // // this.setY(this.getY() + 5);
    // // } else if (keyCode == Keyboard.KEY_UP) {
    // // this.setY(this.getY() - 5);
    // // } else if (keyCode == Keyboard.KEY_RIGHT) {
    // // this.setX(this.getX() + 5);
    // // } else if (keyCode == Keyboard.KEY_LEFT) {
    // // this.setX(this.getX() - 5);
    // // }
    // }

    @Override
    public void onGuiClosed() {
	Keyboard.enableRepeatEvents(false);
    }
}
