package me.lefted.lunacyforge.guiscreen.interpreter;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.utils.Logger;

public class ClientSettingsOverlay extends Panel {

    // ATTRIBUTES
    private boolean initDone = false;
    private BackButton btnBack;

    /* only called once*/
    // CONSTRUCTOR
    public ClientSettingsOverlay() {
	super(0, 0);
	setVerticalScrolling(true);
	setVerticalWheelScrolling(true);
	setDrawDefaultBackground(false);
    }

    /* called whenever the menu opens*/
    // METHODS
    @Override
    public void initGui() {
	super.initGui();
	// reset scroll
	setY(0);

	// back button
	btnBack = new BackButton();
	btnBack.setCallback(() -> back());

	initDone = true;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	btnBack.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	btnBack.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	Logger.logChatMessage("key:" + Keyboard.getKeyName(keyCode));
    }

    @Override
    public void onGuiClosed() {
	initDone = false;
    }

    private void back() {
	this.onGuiClosed();
	ClickGuiScreen.setMenu(Menu.SEARCH);
    }
}
