package me.lefted.lunacyforge.clickgui.menus;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.container.ModuleContainer;
import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.BackButton;
import me.lefted.lunacyforge.clickgui.elements.SearchBar;
import me.lefted.lunacyforge.clickgui.utils.ScissorBox;
import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/* Abstract class that implements the settings functionality using a scrollable list of custom sized containers */
public abstract class SettingsMenu extends Panel {

    // CONSTANTS
    private static final int CONTAINER_SPACING = 4;

    // ATTRIBUTES
    private boolean initDone = false;
    private BackButton btnBack;
    private ArrayList<SettingContainer> settings = new ArrayList<SettingContainer>();
    private ScissorBox scissorBox; // used to cut off rendering when scrolling
    private int leftCut;

    /* should only be called once*/
    // CONSTRUCTOR
    public SettingsMenu() {
	super(0, 0);
	setVerticalScrolling(true);
	setVerticalWheelScrolling(true);
	setDrawDefaultBackground(false);
    }

    /* called whenever the menu opens*/
    // METHODS
    @Override
    public void initGui() {

	// reset settingslist
	settings.clear();

	// TODO
	// (re)addAllSettings()

	// DEBUG
	SettingContainer container = new SettingContainer();
	container.setDescription("Just testing this out.");
	// add it to the settings list
	settings.add(container);

	// back button
	btnBack = new BackButton();
	btnBack.setCallback(() -> back());

	// scissor box
	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	// TODO check if this is good
	final int boxTop = sc.getScaledHeight() / 10 + SearchBar.HEIGHT + CONTAINER_SPACING * 5;
	final int boxBottom = boxTop + 250;
	scissorBox = new ScissorBox(boxTop, boxBottom);

	// TODO may be changed
	leftCut = sc.getScaledWidth() / 2 - SearchBar.WIDTH / 2;

	// reset scroll
	setY(0);

	// initialisation finished
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

	// DEBUG
//	Logger.logChatMessage("panel y:" + this.getY());

	// settings
	if (hasSettingsContainer()) {
	    // y position of the first container
	    final int startY = scissorBox.getBoxTop();

	    // scissor box
	    // scissorBox.cut(leftCut, scissorBox.getBoxTop(), leftCut + SearchBar.WIDTH, scissorBox.getBoxBottom());
	    // GL11.glEnable(GL11.GL_SCISSOR_TEST);

	    // for all containers
	    for (int i = 0; i < settings.size(); i++) {
		final SettingContainer container = settings.get(i);

		// update container's y position
		container.setPosY(startY + ModuleContainer.HEIGHT * i + CONTAINER_SPACING * i + this.getY());
		// and its visible coords
		// TODO
		// container.updateVisibleCoords(scissorBox.getBoxTop(), scissorBox.getBoxBottom());

		// if completly out of scissorbox make them invisible
		// TODO get specific height of container
		if ((container.getPosY() + SettingContainer.HEIGHT) <= scissorBox.getBoxTop() || container.getPosY() >= scissorBox.getBoxBottom()) {
		    container.setVisible(false);
		} else {
		    container.setVisible(true);
		}

		// finally draw container
		container.draw(mouseX, mouseY, partialTicks);
	    }
	}
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// panel
	super.mouseClicked(mouseX, mouseY, mouseButton);

	// back button
	btnBack.mouseClicked(mouseX, mouseY, mouseButton);

	// settings
	if (hasSettingsContainer()) {
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

	// panel
	super.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);

	// settings
	if (hasSettingsContainer()) {
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

	// panel
	super.mouseReleased(mouseX, mouseY, mouseButton);

	// settings
	if (hasSettingsContainer()) {
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
	if (hasSettingsContainer()) {
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
	if (hasSettingsContainer()) {
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
	SearchMenu.setMenu(Menu.SEARCH);
    }

    private boolean hasSettingsContainer() {
	return settings != null && settings.size() > 0;
    }

    public ArrayList<SettingContainer> getSettings() {
	return settings;
    }

}
