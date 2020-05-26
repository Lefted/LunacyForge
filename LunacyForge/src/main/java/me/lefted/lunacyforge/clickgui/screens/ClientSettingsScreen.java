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
    private SettingContainer colorRed;
    private SettingContainer colorGreen;
    private SettingContainer colorBlue;

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

	// adds the settings that determine the client color
	// addRGBContainer(settings);

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
	ContainerComobox modeCombobox = new ContainerComobox(modeContainer, ClientConfig.isShowRageMods() ? 0 : 1, "All", "Invis");
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
	ContainerComobox arraylistBgCombobox = new ContainerComobox(arraylistBgContainer, ClientConfig.getArraylistMode(), "None", "Normal", "Shadow",
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

    private void addRGBContainer(ArrayList<SettingContainer> settings) {
	// final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());

	// // description
	// SettingContainer title = new SettingContainer(350, 20);
	// title.centerX();
	// // title.setPosX(sc.getScaledWidth() / 2 - title.getWidth() / 2);
	// title.setDescription("Colors");
	// settings.add(title);
	//
	// // red
	// ContainerSlider sliderRed = new ContainerSlider(this, ContainerSlider.NumberType.INTEGER, 0, 255, 1D);
	// sliderRed.setValue(ClientConfig.getGuiColor().getRed());
	// sliderRed.setConsumer((d) -> {
	// colorRed.setDescription("Red: " + sliderRed.getValueString());
	// updateGuiColor();
	// });
	//
	// colorRed = new SettingContainer(350, 20);
	// colorRed.setSettingOffsetY(10);
	// colorRed.setDescription("Red: " + ClientConfig.getGuiColor().getRed());
	//
	// colorRed.centerX();
	// sliderRed.setPosX(colorRed.getPosX() + colorRed.getWidth() - sliderRed.WIDTH - 10);
	// colorRed.setSettingElement(sliderRed);
	// settings.add(colorRed);
	//
	// // green
	// ContainerSlider sliderGreen = new ContainerSlider(this, ContainerSlider.NumberType.INTEGER, 0, 255, 1D);
	// sliderGreen.setValue(ClientConfig.getGuiColor().getGreen());
	// sliderGreen.setConsumer((d) -> {
	// colorGreen.setDescription("Green: " + sliderGreen.getValueString());
	// updateGuiColor();
	// });
	//
	// colorGreen = new SettingContainer(350, 20);
	// colorGreen.setSettingOffsetY(10);
	// colorGreen.setDescription("Green: " + ClientConfig.getGuiColor().getGreen());
	//
	// colorGreen.centerX();
	// sliderGreen.setPosX(colorGreen.getPosX() + colorGreen.getWidth() - sliderGreen.WIDTH - 10);
	// colorGreen.setSettingElement(sliderGreen);
	// settings.add(colorGreen);
	//
	// // blue
	// ContainerSlider sliderBlue = new ContainerSlider(this, ContainerSlider.NumberType.INTEGER, 0, 255, 1D);
	// sliderBlue.setValue(ClientConfig.getGuiColor().getBlue());
	// sliderBlue.setConsumer((d) -> {
	// colorBlue.setDescription("Blue: " + sliderBlue.getValueString());
	// updateGuiColor();
	// });
	//
	// colorBlue = new SettingContainer();
	// colorBlue.setSettingOffsetY(10);
	// colorBlue.setDescription("Blue: " + ClientConfig.getGuiColor().getBlue());
	//
	// colorBlue.centerX();
	// sliderBlue.setPosX(colorBlue.getPosX() + colorBlue.getWidth() - sliderBlue.WIDTH - 10);
	// colorBlue.setSettingElement(sliderBlue);
	// settings.add(colorBlue);
	//
	// groupSettings(title, colorRed, colorGreen, colorBlue);
    }

    private void updateGuiColor() {
	final int red = (int) ((ContainerSlider) colorRed.getSettingElement()).getValue();
	final int green = (int) ((ContainerSlider) colorGreen.getSettingElement()).getValue();
	final int blue = (int) ((ContainerSlider) colorBlue.getSettingElement()).getValue();

	ClientConfig.setGuiColor(new Color(red, green, blue));
    }
}
