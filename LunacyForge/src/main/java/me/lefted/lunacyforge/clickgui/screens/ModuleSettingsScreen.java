package me.lefted.lunacyforge.clickgui.screens;

import java.util.ArrayList;

import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.BackButton;
import me.lefted.lunacyforge.clickgui.utils.AnnotationUtils;
import me.lefted.lunacyforge.modules.Module;

public class ModuleSettingsScreen extends SettingsScreen {

    // INSTANCE
    public static ModuleSettingsScreen instance;

    // ATTRIBUTES
    private Module module;
    private BackButton backButton;

    // METHODS
    @Override
    public void addAllSettings(ArrayList<SettingContainer> settings) {
	if (module != null) {
	    // add module name and description
	    addModuleInfo(settings);

	    // TODO add settings
	    // if (module.has)
	}
    }

    @Override
    public void initOtherElements() {
	// pass call
	super.initOtherElements();

	backButton = new BackButton();
	backButton.setCallback(() -> mc.displayGuiScreen(SearchScreen.instance));
    }

    @Override
    public void drawOtherElements(int mouseX, int mouseY, float partialTicks) {
	// pass call
	super.drawOtherElements(mouseX, mouseY, partialTicks);

	// back button
	backButton.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
	super.updateScreen();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// pass call
	super.mouseClicked(mouseX, mouseY, mouseButton);

	// if rightclick go back to search
	if (mouseButton == 1) {
	    mc.displayGuiScreen(SearchScreen.instance);
	} else {
	    // back button
	    backButton.mouseClicked(mouseX, mouseY, mouseButton);
	}
    }

    @Override
    public boolean doesGuiPauseGame() {
	return false;
    }

    @Override
    public boolean isUseInventoryMove() {
	return true;
    }

    public Module getModule() {
	return module;
    }

    // USETHIS before showing the screen
    public void changeModule(Module module) {
	this.module = module;
    }

    private void addModuleInfo(ArrayList<SettingContainer> settings) {
	final SettingContainer info = new SettingContainer();
	info.centerX();
	info.setDescription(module.getName() + " " + getModuleDescription(module));

	settings.add(info);
    }

    // returns a description if the module has one
    private String getModuleDescription(Module module) {
	final ModuleInfo info = AnnotationUtils.getModuleInfo(module);
	if (info != null) {
	    return info.description();
	}
	return "";
    }
}
