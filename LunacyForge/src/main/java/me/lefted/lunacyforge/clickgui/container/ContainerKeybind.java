package me.lefted.lunacyforge.clickgui.container;

import java.util.function.Consumer;

import org.lwjgl.input.Keyboard;

import me.lefted.lunacyforge.guiapi.Element;
import net.minecraft.util.EnumChatFormatting;

public class ContainerKeybind extends Element {

    // ATTRIBUTES
    private ContainerButton button;
    private boolean listening = false;
    private int keycode;
    private Consumer<Integer> intConsumer;
    private Consumer<String> stringConsumer;

    // CONSTRUCTOR
    public ContainerKeybind(int width, int height, int keycode) {
	posX = 0;
	posY = 0;
	this.button = new ContainerButton(width, height, getKeybindName(keycode));
	this.keycode = keycode;
	this.button.setCallback(() -> {
	    this.listening = true;
	    this.button.setDisplayString(EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + this.getKeybindName(this.keycode)
		+ EnumChatFormatting.WHITE + " <");
	});
    }

    // METHODS
    private String getKeybindName(int keycode) {
	if (this.keycode == -100) {
	    return "Button 1";
	} else if (this.keycode == -99) {
	    return "Button 2";
	} else if (this.keycode == -98) {
	    return "Button 3";
	} else {
	    return Keyboard.getKeyName(keycode);
	}
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	this.button.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (this.listening) {
	    if (mouseButton == 0) {
		this.keycode = -100;
		this.button.setDisplayString(this.getKeybindName(this.keycode));
		this.listening = false;

		if (this.intConsumer != null) {
		    this.intConsumer.accept(this.keycode);
		}
		if (this.stringConsumer != null) {
		    this.stringConsumer.accept(this.button.getDisplayString());
		}
	    } else if (mouseButton == 1) {
		this.keycode = -99;
		this.button.setDisplayString(this.getKeybindName(this.keycode));
		this.listening = false;

		if (this.intConsumer != null) {
		    this.intConsumer.accept(this.keycode);
		}
		if (this.stringConsumer != null) {
		    this.stringConsumer.accept(this.button.getDisplayString());
		}
	    } else if (mouseButton == 2) {
		this.keycode = -98;
		this.button.setDisplayString(this.getKeybindName(this.keycode));
		this.listening = false;

		if (this.intConsumer != null) {
		    this.intConsumer.accept(this.keycode);
		}
		if (this.stringConsumer != null) {
		    this.stringConsumer.accept(this.button.getDisplayString());
		}
	    }
	}
	this.button.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
	if (this.listening) {
	    this.keycode = keyCode;
	    this.button.setDisplayString(Keyboard.getKeyName(this.keycode));
	    this.listening = false;

	    if (this.intConsumer != null) {
		this.intConsumer.accept(this.keycode);
	    }
	    if (this.stringConsumer != null) {
		this.stringConsumer.accept(this.button.getDisplayString());
	    }
	}
    }

    @Override
    public void updateScreen() {
    }

    public Consumer<Integer> getIntConsumer() {
	return intConsumer;
    }

    public void setIntConsumer(Consumer<Integer> consumer) {
	this.intConsumer = consumer;
    }

    public Consumer<String> getStringConsumer() {
	return stringConsumer;
    }
    
    public void setStringConsumer(Consumer<String> stringConsumer) {
	this.stringConsumer = stringConsumer;
    }
    
    @Override
    public void setPosX(int posX) {
	super.setPosX(posX);
	this.button.setPosX(posX);
    }

    @Override
    public void setPosY(int posY) {
	super.setPosY(posY);
	this.button.setPosY(posY);
    }

    public int getWidth() {
	return button.getWidth();
    }

    public int getHeight() {
	return button.getHeight();
    }
}
