package me.lefted.lunacyforge.clickgui.screens;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.lefted.lunacyforge.clickgui.container.ModuleContainer;
import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.ClientSettingsButton;
import me.lefted.lunacyforge.clickgui.elements.FriendSettingsButton;
import me.lefted.lunacyforge.clickgui.elements.GuiSecurity;
import me.lefted.lunacyforge.clickgui.elements.SearchBar;
import me.lefted.lunacyforge.modules.ClickGui;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class SearchScreen extends SettingsScreen {

    // ATTRIBUTES
    private boolean shouldBlur = false;
    private GuiSecurity security;
    private SearchBar search;
    private ClientSettingsButton btnSettings;
    private FriendSettingsButton btnFriends;

    private boolean clickGuiBindCanClose; // prevents the gui from closing instantly

    // INSTANCE
    public static SearchScreen instance;

    // METHODS
    @Override
    public void initOtherElements() {
	clickGuiBindCanClose = false;

	// settings button
	btnSettings = new ClientSettingsButton();
	btnSettings.setCallback(() -> mc.displayGuiScreen(ClientSettingsScreen.instance));

	// friends button
	btnFriends = new FriendSettingsButton();
	btnFriends.setCallback(() -> mc.displayGuiScreen(FriendSettingsScreen.instance));

	// security
	security = new GuiSecurity();

	// searchbar
	search = new SearchBar(this.getSettings(), this);
    }

    @Override
    public void addAllSettings(ArrayList<SettingContainer> settings) {
	// add all modules
	addAllModules(settings);
    }

    // add all available modules
    public static void addAllModules(ArrayList<SettingContainer> settings) {
	// clear container list
	settings.clear();
	// add all available modules as container
	for (Module module : ModuleManager.getModuleList()) {
	    if (module instanceof ClickGui) {
		continue;
	    }
	    // create new container
	    final ModuleContainer container = new ModuleContainer(module);

	    // and add it
	    settings.add(container);
	}
    }

    @Override
    public void drawOtherElements(int mouseX, int mouseY, float partialTicks) {
	// search
	search.draw(mouseX, mouseY, partialTicks);
	// security
	security.draw(mouseX, mouseY, partialTicks);
	// settings button
	btnSettings.draw(mouseX, mouseY, partialTicks);
	// friends button
	btnFriends.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
	return false;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// pass call to containers
	super.mouseClicked(mouseX, mouseY, mouseButton);

	// security
	security.mouseClicked(mouseX, mouseY, mouseButton);
	// settings button
	btnSettings.mouseClicked(mouseX, mouseY, mouseButton);
	// friends button
	btnFriends.mouseClicked(mouseX, mouseY, mouseButton);
	
	// search
	search.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	// pass call to containers
	super.keyTyped(typedChar, keyCode);

	// close if keybind is pressed
	if (keyCode == ModuleManager.getModule(ClickGui.class).getKeycode()) {
	    if (clickGuiBindCanClose) {
		this.mc.displayGuiScreen((GuiScreen) null);

		if (this.mc.currentScreen == null) {
		    this.mc.setIngameFocus();
		}
	    }
	}
	// searchbar
	search.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	// if key gui was released
	if (!Keyboard.isKeyDown(ModuleManager.getModule(ClickGui.class).getKeycode())) {
	    clickGuiBindCanClose = true;
	}

	// pass call to containers
	super.updateScreen();

	// searchbar
	search.updateScreen();
    }

    @Override
    public void onGuiClosed() {
	/*
	 * End blur 
	 */
	if (shouldBlur && mc.entityRenderer.getShaderGroup() != null) {
	    mc.entityRenderer.getShaderGroup().deleteShaderGroup();
	    mc.entityRenderer.stopUseShader();// = null;
	}
	initDone = false;

	// pass call
	super.onGuiClosed();
    }

    @Override
    public boolean isUseInventoryMove() {
	return !search.getTextfield().isFocused();
    }

}
