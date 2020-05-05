package me.lefted.lunacyforge.clickgui.screens;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider;
import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.utils.ColorUtils;
import me.lefted.lunacyforge.utils.DrawUtils;
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

    // METHODS
    @Override
    public void addAllSettings(ArrayList<SettingContainer> settings) {
	// adds the settings that determine the client color
	addRGBContainer(settings);
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

	// description
	SettingContainer title = new SettingContainer();
	title.centerX();
	// title.setPosX(sc.getScaledWidth() / 2 - title.getWidth() / 2);
	title.setDescription("Gui color");
	settings.add(title);

	// red
	ContainerSlider sliderRed = new ContainerSlider(ContainerSlider.NumberType.INTEGER, 0, 255, 1D);
	sliderRed.setValue(ClientConfig.getGuiColor().getRed());
	sliderRed.setConsumer((d) -> {
	    colorRed.setDescription("Red:" + sliderRed.getValueString());
	    updateGuiColor();
	});

	colorRed = new SettingContainer();
	colorRed.setSettingOffsetY(10);
	colorRed.setDescription("Red: " + ClientConfig.getGuiColor().getRed());

	colorRed.centerX();
	sliderRed.setPosX(colorRed.getPosX() + colorRed.getWidth() - sliderRed.WIDTH - 10);
	colorRed.setSettingElement(sliderRed);
	settings.add(colorRed);

	// green
	ContainerSlider sliderGreen = new ContainerSlider(ContainerSlider.NumberType.INTEGER, 0, 255, 1D);
	sliderGreen.setValue(ClientConfig.getGuiColor().getGreen());
	sliderGreen.setConsumer((d) -> {
	    colorGreen.setDescription("Green:" + sliderGreen.getValueString());
	    updateGuiColor();
	});

	colorGreen = new SettingContainer();
	colorGreen.setSettingOffsetY(10);
	colorGreen.setDescription("Green:" + ClientConfig.getGuiColor().getGreen());

	colorGreen.centerX();
	sliderGreen.setPosX(colorGreen.getPosX() + colorGreen.getWidth() - sliderGreen.WIDTH - 10);
	colorGreen.setSettingElement(sliderGreen);
	settings.add(colorGreen);

	// blue
	ContainerSlider sliderBlue = new ContainerSlider(ContainerSlider.NumberType.INTEGER, 0, 255, 1D);
	sliderBlue.setValue(ClientConfig.getGuiColor().getBlue());
	sliderBlue.setConsumer((d) -> {
	    colorBlue.setDescription("Blue:" + sliderBlue.getValueString());
	    updateGuiColor();
	});

	colorBlue = new SettingContainer();
	colorBlue.setSettingOffsetY(10);
	colorBlue.setDescription("Blue:" + ClientConfig.getGuiColor().getBlue());

	colorBlue.centerX();
	sliderBlue.setPosX(colorBlue.getPosX() + colorBlue.getWidth() - sliderBlue.WIDTH - 10);
	colorBlue.setSettingElement(sliderBlue);
	settings.add(colorBlue);
	
	groupSettings(title, colorRed, colorGreen, colorBlue);
    }

    private void updateGuiColor() {
	final int red = (int) ((ContainerSlider) colorRed.getSettingElement()).getValue();
	final int green = (int) ((ContainerSlider) colorGreen.getSettingElement()).getValue();
	final int blue = (int) ((ContainerSlider) colorBlue.getSettingElement()).getValue();

	ClientConfig.setGuiColor(new Color(red, green, blue));
    }
}
