package me.lefted.lunacyforge.clickgui.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.container.SettingsGroup;
import me.lefted.lunacyforge.clickgui.elements.BackButton;
import me.lefted.lunacyforge.clickgui.elements.SearchBar;
import me.lefted.lunacyforge.clickgui.utils.ScissorBox;
import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import scala.actors.threadpool.Arrays;

/* Abstract class that implements the settings functionality using a scrollable list of custom sized containers. You might like to override some methods like
 * mouseClicked, released, keyTyped, etc to pass the calls to your other elments (which are no settingcontainers). Just don't forget to pass the call via
 * super() to this class */
public abstract class SettingsScreen extends Panel {

    // CONSTANTS
    public static final int CONTAINER_SPACING = 4;

    // ATTRIBUTES
    protected boolean initDone = false; // if true, initGui() has been finished
    private BackButton btnBack;
    private ArrayList<SettingContainer> settings = new ArrayList<SettingContainer>();
    private ScissorBox scissorBox; // used to cut off rendering when scrolling
    private List<SettingsGroup> groupsList = new ArrayList<SettingsGroup>();

    /* should only be called once*/
    // CONSTRUCTOR
    public SettingsScreen() {
	super(0, 0);
	setVerticalScrolling(true);
	setVerticalWheelScrolling(true);
	setScrollMultiplier(10);
	setDrawDefaultBackground(false);
    }

    /* called whenever the menu opens*/
    // METHODS
    @Override
    public void initGui() {
	// clear already existing settingslist
	settings.clear();
	// clear settings groups
	groupsList.clear();

	// readd them
	addAllSettings(settings);

	// init other elements
	initOtherElements();

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

	// custom background
	DrawUtils.INSTANCE.drawCustomBackground(this.width, this.height);

	// enable blending
	GlStateManager.enableBlend();
	GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	// draw other elements
	drawOtherElements(mouseX, mouseY, partialTicks);

	// settings
	if (hasSettingsContainer()) {
	    // scissor box
	    scissorBox.cut();
	    GL11.glEnable(GL11.GL_SCISSOR_TEST);

	    // y position of the first container
	    final int startY = scissorBox.getY();

	    // update all containers
	    for (int i = 0; i < settings.size(); i++) {
		final SettingContainer container = settings.get(i);

		// update container's y position
		// if its the first one set it to startY + panels' y
		if (i == 0) {
		    container.setPosY(startY + this.getY());
		} else {
		    // set it to the y of the previous container + its height + spacing + panely
		    final SettingContainer prevContainer = settings.get(i - 1);
		    container.setPosY(prevContainer.getPosY() + prevContainer.getHeight() + CONTAINER_SPACING + this.getY());
		}

		// and update its visible coords
		container.updateVisibleCoords(scissorBox);

		// if completly out of scissorbox make them invisible
		if ((container.getPosY() + container.getHeight()) <= scissorBox.getY() || container.getPosY() >= (scissorBox.getY() + scissorBox.getHeight())) {
		    container.setVisible(false);
		} else {
		    container.setVisible(true);
		}
	    }

	    // draw the groups
	    if (groupsList != null && !groupsList.isEmpty()) {
		groupsList.forEach(this::drawGroup);
	    }

	    // draw the containers
	    settings.forEach(container -> container.draw(mouseX, mouseY, partialTicks));
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

	if (isUseInventoryMove()) {
	    // TODO check if any text field is focused
	    // movement
	    // InventoryMove Credits @Andrew Saint 2.3
	    KeyBinding[] moveKeys = new KeyBinding[] { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft,
		    mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint };
	    KeyBinding[] array = moveKeys;
	    int length = moveKeys.length;
	    for (int i = 0; i < length; ++i) {
		KeyBinding bind = array[i];
		KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
	    }
	}
    }

    // draws the group
    private void drawGroup(SettingsGroup group) {
	final int posY = group.getSettings().stream().min(Comparator.comparingInt(container -> container.getPosY())).get().getPosY();

	SettingContainer.drawContainerTexture(group.getPosX(), posY, group.getWidth(), group.getHeight());
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
	// pass call
	super.onGuiClosed();
	initDone = false;
    }

    // USETHIS to add all settings, can also be used to initialize other elements
    public abstract void addAllSettings(ArrayList<SettingContainer> settings);

    // USETHIS to group settings how the background looks
    public void groupSettings(SettingContainer... settings) {
	// create the group containing all settings
	final List<SettingContainer> settingsList = Arrays.asList(settings);

	final SettingsGroup group = new SettingsGroup(settingsList);

	// set the group in every settings
	settingsList.forEach(setting -> setting.setGroup(group));
	// and add the group
	groupsList.add(group);
    }

    // USETHIS to initialize other elments
    public void initOtherElements() {
    }

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
	return (sc.getScaledHeight() / 10) + SearchBar.HEIGHT + (CONTAINER_SPACING * 5);
    }

    // USETHIS to determine the width of the list
    public int getListWidth() {
	return SearchBar.WIDTH;
    }

    // USETHIS to determine where's the bottom of the list
    public int getListHeight() {
	return 250;
    }

    // USETHIS
    public boolean isUseInventoryMove() {
	return false;
    }

    public ArrayList<SettingContainer> getSettings() {
	return settings;
    }
}
