package me.lefted.lunacyforge.clickgui.screens;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.LunacyForge;
import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.BackButton;
import me.lefted.lunacyforge.clickgui.elements.ContainerCheckbox;
import me.lefted.lunacyforge.clickgui.elements.ContainerColorpicker;
import me.lefted.lunacyforge.clickgui.elements.ContainerComobox;
import me.lefted.lunacyforge.clickgui.elements.ContainerKeybind;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider;
import me.lefted.lunacyforge.clickgui.elements.ContainerTextfield;
import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.modules.ClickGui;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.ColorUtils;
import me.lefted.lunacyforge.utils.DrawUtils;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.discovery.ContainerType;

/* Menu that lets the user control client specific settings. */
public class ClientSettingsScreen extends SettingsScreen {

    // INSTANCE
    public static ClientSettingsScreen instance;

    // ATTRIBUTES
    private BackButton backButton;

    // METHODS
    @Override
    public void addAllSettings(ArrayList<SettingContainer> settings) {
	// use client
	SettingContainer enabledContainer = new SettingContainer();
	enabledContainer.centerX();
	enabledContainer.setDescription("Use Client");
	ContainerCheckbox enabledCheckbox = new ContainerCheckbox(ClientConfig.isEnabled());
	enabledCheckbox.setPosX(enabledContainer.getPosX() + enabledContainer.getWidth() - enabledCheckbox.WIDTH - 10);
	enabledCheckbox.setConsumer(newValue -> {
	    ClientConfig.setEnabled(newValue);
	    ClientConfig.saveConfig();
	});
	enabledContainer.setSettingOffsetY(7);
	enabledContainer.setSettingElement(enabledCheckbox);
	settings.add(enabledContainer);

	// clickgui keybind
	SettingContainer keybindContainer = new SettingContainer();
	keybindContainer.centerX();
	keybindContainer.setDescription("Clickgui Keybind");
	keybindContainer.setSettingOffsetY(7);
	ContainerKeybind keybind = new ContainerKeybind(this, 130, 15, ModuleManager.getModule(ClickGui.class).getKeycode());
	keybind.setPosX(keybindContainer.getPosX() + keybindContainer.getWidth() - keybind.getWidth() - 8);
	keybind.setIntConsumer(newValue -> ModuleManager.getModule(ClickGui.class).setKeycode(newValue));
	keybindContainer.setSettingElement(keybind);
	settings.add(keybindContainer);

	// client color
	SettingContainer clientColorContainer = new SettingContainer();
	ContainerColorpicker clientColorPicker = new ContainerColorpicker(this, clientColorContainer, ClientConfig.getGuiColor());
	clientColorContainer.centerX();
	clientColorPicker.setPosX(clientColorContainer.getPosX() + clientColorContainer.getWidth() - clientColorPicker.getWidth() - 71);
	clientColorPicker.setColorObjConsumer(color -> ClientConfig.setGuiColor(color));
	clientColorPicker.setHasAlpha(false);
	clientColorContainer.setDescription("Color Accent");
	clientColorContainer.setSettingOffsetY(10);
	clientColorContainer.setSettingElement(clientColorPicker);
	settings.add(clientColorContainer);

	// mode
	SettingContainer modeContainer = new SettingContainer();
	ContainerComobox modeCombobox = new ContainerComobox(this, modeContainer, ClientConfig.isShowRageMods() ? 0 : 1, "All", "Invis");
	modeContainer.centerX();
	modeCombobox.setPosX(modeContainer.getPosX() + modeContainer.getWidth() - modeCombobox.ENTRY_WIDTH - 10);
	modeCombobox.setIntConsumer(newValue -> {
	    ClientConfig.setShowRageMods(newValue == 0);
	    ClientConfig.saveConfig();
	});
	modeContainer.setSettingOffsetY(8);
	modeContainer.setDescription("Client Mode");
	modeContainer.setSettingElement(modeCombobox);
	modeContainer.setBackgroundLevel(1);
	settings.add(modeContainer);

	// arraylist mode
	SettingContainer arraylistBgContainer = new SettingContainer();
	arraylistBgContainer.centerX();
	arraylistBgContainer.setDescription("Arraylist Background");
	ContainerComobox arraylistBgCombobox = new ContainerComobox(this, arraylistBgContainer, ClientConfig.getArraylistMode(), "None", "Normal", "Shadow",
	    "Normal+Rect", "Shadow+Rect");
	arraylistBgCombobox.setPosX(arraylistBgContainer.getPosX() + arraylistBgContainer.getWidth() - arraylistBgCombobox.ENTRY_WIDTH - 10);
	arraylistBgCombobox.setIntConsumer(newValue -> {
	    ClientConfig.setArraylistMode(newValue);
	    ClientConfig.saveConfig();
	});
	arraylistBgContainer.setSettingOffsetY(8);
	arraylistBgContainer.setBackgroundLevel(1);
	arraylistBgContainer.setSettingElement(arraylistBgCombobox);
	settings.add(arraylistBgContainer);

	// announce module toggle
	SettingContainer chatContainer = new SettingContainer();
	chatContainer.centerX();
	chatContainer.setDescription("Notify In Chat When Enabling/Disabling A Module");
	ContainerCheckbox chatCheckbox = new ContainerCheckbox(ClientConfig.isAnnounceModuleToggle());
	chatCheckbox.setPosX(chatContainer.getPosX() + chatContainer.getWidth() - chatCheckbox.WIDTH - 10);
	chatCheckbox.setConsumer(newValue -> {
	    ClientConfig.setAnnounceModuleToggle(newValue);
	    ClientConfig.saveConfig();
	});
	chatContainer.setSettingOffsetY(7);
	chatContainer.setSettingElement(chatCheckbox);
	settings.add(chatContainer);
    }

    @Override
    public void initOtherElements() {
	// pass call
	super.initOtherElements();

	// back button
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
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	if (mouseButton == 1) {
	    backButton.getCallback().invoke();
	    return;
	}

	// pass call
	super.mouseClicked(mouseX, mouseY, mouseButton);

	// back button
	backButton.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean doesGuiPauseGame() {
	return false;
    }

    @Override
    public boolean isUseInventoryMove() {
	return true;
    }
}
