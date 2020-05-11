package me.lefted.lunacyforge.clickgui.screens;

import java.util.ArrayList;

import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.container.ContainerKeybind;
import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.BackButton;
import me.lefted.lunacyforge.clickgui.utils.AnnotationUtils;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.utils.DrawUtils;

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

	    // add the keybind setting
	    // addModuleKeybind(settings);

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

    // adds the name and the description and the keybind
    private void addModuleInfo(ArrayList<SettingContainer> settings) {
	final SettingContainer name = new SettingContainer() {
	    @Override
	    public boolean isMouseOverHoverArea(int mouseX, int mouseY) {
		return mouseX >= posX && mouseX <= posX + DrawUtils.INSTANCE.getStringWidth(module.getName()) + 20 && mouseY >= posY && mouseY <= posY + height;
	    }
	};
	name.centerX();
	name.setDescription(module.getName());

	final String descriptionString = getModuleDescription(module);
	if (!descriptionString.isEmpty()) {
	    name.setHoverText(descriptionString);
	}

	final ContainerKeybind keybind = new ContainerKeybind(this, 75, 16, module.getKeycode());
	keybind.setPosX(name.getPosX() + name.getWidth() - keybind.getWidth() - 10);
	keybind.setIntConsumer(keycode -> module.setKeycode(keycode));

	name.setSettingOffsetY(6);
	name.setSettingElement(keybind);

	settings.add(name);
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
