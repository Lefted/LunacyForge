package me.lefted.lunacyforge.clickgui.screens;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.container.ModuleContainer;
import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.BackButton;
import me.lefted.lunacyforge.clickgui.elements.SearchBar;
import me.lefted.lunacyforge.clickgui.utils.ScissorBox;
import me.lefted.lunacyforge.guiapi.Panel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

/* Abstract class that implements the settings functionality using a scrollable list of custom sized containers */
public abstract class SettingsScreen extends Panel {

    // CONSTANTS
    protected static final int CONTAINER_SPACING = 4;

    // ATTRIBUTES
    protected boolean initDone = false; // if true, initGui() has been finished
    private BackButton btnBack;
    private ArrayList<SettingContainer> settings = new ArrayList<SettingContainer>();
    private ScissorBox scissorBox; // used to cut off rendering when scrolling

    /* should only be called once*/
    // CONSTRUCTOR
    public SettingsScreen() {
	super(0, 0);
	setVerticalScrolling(true);
	setVerticalWheelScrolling(true);
	setDrawDefaultBackground(false);
    }

    /* called whenever the menu opens*/
    // METHODS
    @Override
    public void initGui() {
	// clear already existing settingslist
	settings.clear();

	// readd them
	addAllSettings(settings);

	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());

	// DEBUG
	SettingContainer container = new SettingContainer(sc.getScaledWidth() - 350 / 2, 0, 350, 30);
	container.setDescription("Just testing this out.");
	// add it to the settings list
	settings.add(container);

	// back button
	if (this.isUseBackButton()) {
	    btnBack = new BackButton();
	    btnBack.setCallback(() -> back());
	}

	// scissor box
	scissorBox = new ScissorBox(getListX(), getListY(), getListWidth(), getListHeight());

	// reset scroll
	setY(0);

	// set panel borders
	setPanelBorders();

	// initialisation finished
	initDone = true;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// enable blending
	GlStateManager.enableBlend();
	GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	if (isUseBackButton()) {
	    // back button
	    btnBack.draw(mouseX, mouseY, partialTicks);
	}

	// draw other elements
	drawOtherElements(mouseX, mouseY, partialTicks);

	// settings
	if (hasSettingsContainer()) {
	    // scissor box
	    scissorBox.cut();
	    GL11.glEnable(GL11.GL_SCISSOR_TEST);

	    // y position of the first container
	    final int startY = scissorBox.getY();

	    // for all containers
	    for (int i = 0; i < settings.size(); i++) {
		final SettingContainer container = settings.get(i);

		// update container's y position
		container.setPosY(startY + ModuleContainer.HEIGHT * i + CONTAINER_SPACING * i + this.getY());
		// and its visible coords
		container.updateVisibleCoords(scissorBox);

		// if completly out of scissorbox make them invisible
		if ((container.getPosY() + container.getHeight()) <= scissorBox.getY() || container.getPosY() >= (scissorBox.getY() + scissorBox.getHeight())) {
		    container.setVisible(false);
		} else {
		    container.setVisible(true);
		}

		// finally draw container
		container.draw(mouseX, mouseY, partialTicks);
	    }
	}

	// disable scissor test
	GL11.glDisable(GL11.GL_SCISSOR_TEST);
	// disable blending
	GlStateManager.disableBlend();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// pass call to panel
	super.mouseClicked(mouseX, mouseY, mouseButton);

	if (isUseBackButton()) {
	    // back button
	    btnBack.mouseClicked(mouseX, mouseY, mouseButton);
	}

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

	// pass call to panel
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

	// pass call to panel
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

	// closing the gui
	if (keyCode == Keyboard.KEY_ESCAPE) {
	    super.keyTyped(typedChar, keyCode);
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

    // fired by the back button
    private void back() {
	// if screen is search, close gui
	if (Minecraft.getMinecraft().currentScreen instanceof SearchScreen) {
	    Minecraft.getMinecraft().displayGuiScreen(null);
	} else {
	    // else go back to search
	    Minecraft.getMinecraft().displayGuiScreen(SearchScreen.instance);
	}

    }

    // sets the borders accrodingly to how much the user needs to be able to scroll
    public void setPanelBorders() {
	// calculate the sum height of all containers and spacing
	final int sumContainersHeight = getSumContainersHeight();
	// calculate the scissorbox height
	final int scissorBoxHeight = scissorBox.getHeight();

	if (sumContainersHeight > scissorBoxHeight) {
	    getBorders().setMinY(-(sumContainersHeight - scissorBoxHeight));
	} else {
	    getBorders().setMinY(0);
	}
	getBorders().setMaxY(0);
    }

    // returns the total hight of all contains and spacing
    private int getSumContainersHeight() {
	int sumHeight = 0;

	if (hasSettingsContainer()) {
	    for (SettingContainer container : settings) {
		sumHeight += container.getHeight();
		sumHeight += CONTAINER_SPACING;
	    }
	    // substract one container spacing because there's one less than there are containers
	    sumHeight -= CONTAINER_SPACING;
	}
	return sumHeight;
    }

    private boolean hasSettingsContainer() {
	return settings != null && settings.size() > 0;
    }

    // USETHIS WITH SUPER CALL
    @Override
    public void onGuiClosed() {
	initDone = false;
    }

    // USETHIS to add all settings, can also be used to initialize other elements
    public abstract void addAllSettings(ArrayList<SettingContainer> settings);

    // USETHIS to draw elements which are no settingcontainers
    public void drawOtherElements(int mouseX, int mouseY, float partialTicks) {
    }

    // USETHIS to determine where's the left of the list
    public int getListX() {
	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	return sc.getScaledWidth() / 2 - SearchBar.WIDTH / 2;
    }

    // USETHIS to determine where's the top of the list
    public int getListY() {
	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	return sc.getScaledHeight() / 10 + SearchBar.HEIGHT + CONTAINER_SPACING * 5;
    }

    // USETHIS to determine the width of the list
    public int getListWidth() {
	return SearchBar.WIDTH;
    }

    // USETHIS to determine where's the bottom of the list
    public int getListHeight() {
	return 250;
    }

    // USETHIS to determine if the backbutton should show up
    public boolean isUseBackButton() {
	return true;
    }

    public ArrayList<SettingContainer> getSettings() {
	return settings;
    }
}