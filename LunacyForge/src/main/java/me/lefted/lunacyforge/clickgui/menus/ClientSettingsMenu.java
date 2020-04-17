package me.lefted.lunacyforge.clickgui.menus;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.BackButton;
import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.guiapi.Slider;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.renderer.chunk.SetVisibility;

/*
 * Menu that lets the user control client specific settings.
 * It contains of SettingContainers which consist of the background, a description and an element
 * Those settings are affected by this classes x, y and position.
 */
public class ClientSettingsMenu extends Panel {

    // ATTRIBUTES
    private boolean initDone = false;
    private BackButton btnBack;
    private ArrayList<SettingContainer> settings = new ArrayList<SettingContainer>();
    // TODO change texture
    private SettingContainer sliderGuiColor;

    /* only called once*/
    // CONSTRUCTOR
    public ClientSettingsMenu() {
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
	// reset settingslist
	settings.clear();
	
	// back button
	btnBack = new BackButton();
	btnBack.setCallback(() -> back());

	// create color slider
	sliderGuiColor = new SettingContainer();
	sliderGuiColor.setDescription("Just testing this out.");
	sliderGuiColor.setSettingElement(new Slider(20, 0, 30, 0, 10, 1, 0));
	sliderGuiColor.setPosY(50);
	// add it to the setings list
	settings.add(sliderGuiColor);
	
	initDone = true;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// back button
	btnBack.draw(mouseX, mouseY, partialTicks);

	// settings
	if (hasSettings()) {
	    for (SettingContainer setting : settings) {
		setting.draw(mouseX, mouseY, partialTicks);
	    }
	}
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// back button
	btnBack.mouseClicked(mouseX, mouseY, mouseButton);

	// settings
	if (hasSettings()) {
	    for (SettingContainer setting : settings) {
		setting.mouseClicked(mouseX, mouseY, mouseButton);
	    }
	}
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// settings
	if (hasSettings()) {
	    for (SettingContainer setting : settings) {
		setting.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
	    }
	}
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// settings
	if (hasSettings()) {
	    for (SettingContainer setting : settings) {
		setting.mouseReleased(mouseX, mouseY, mouseButton);
	    }
	}
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// settings
	if (hasSettings()) {
	    for (SettingContainer setting : settings) {
		setting.keyTyped(typedChar, keyCode);
	    }
	}
    }

    @Override
    public void updateScreen() {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// settings
	if (hasSettings()) {
	    for (SettingContainer setting : settings) {
		setting.updateScreen();
	    }
	}
    }

    @Override
    public void onGuiClosed() {
	initDone = false;
    }

    // fired by the back button
    private void back() {
	this.onGuiClosed();
	ClickGuiScreen.setMenu(Menu.SEARCH);
    }

    private boolean hasSettings() {
	return settings != null && settings.size() > 0;
    }

    public ArrayList<SettingContainer> getSettings() {
	return settings;
    }
}
