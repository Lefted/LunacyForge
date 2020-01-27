package me.lefted.lunacyforge.guiapi;

import java.util.function.Consumer;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.EnumChatFormatting;

public class KeybindButton extends Element {

    // ATTRIBUETS
    private Button button;
    private boolean listening = false;
    private int keycode;
    private Consumer<Integer> consumer;

    // CONSTRUCTOR
    public KeybindButton(int x, int y, int widthIn, int heightIn, int keycode) {
	this.button = new Button(x, y, widthIn, heightIn, this.getKeybindName(keycode));
	this.setPosX(x);
	this.setPosY(y);
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

		if (this.consumer != null) {
		    this.consumer.accept(this.keycode);
		}
		return;
	    } else if (mouseButton == 1) {
		this.keycode = -99;
		this.button.setDisplayString(this.getKeybindName(this.keycode));
		this.listening = false;

		if (this.consumer != null) {
		    this.consumer.accept(this.keycode);
		}
		return;
	    } else if (mouseButton == 2) {
		this.keycode = -98;
		this.button.setDisplayString(this.getKeybindName(this.keycode));
		this.listening = false;

		if (this.consumer != null) {
		    this.consumer.accept(this.keycode);
		}
		return;
	    }
	}
	this.button.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
	if (this.listening) {
	    this.keycode = keyCode;
	    this.button.setDisplayString(Keyboard.getKeyName(this.keycode));
	    this.listening = false;

	    if (this.consumer != null) {
		this.consumer.accept(this.keycode);
	    }
	}
    }

    @Override
    public void updateScreen() {
    }

    public Consumer<Integer> getConsumer() {
	return consumer;
    }

    public void setConsumer(Consumer<Integer> consumer) {
	this.consumer = consumer;
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
}
