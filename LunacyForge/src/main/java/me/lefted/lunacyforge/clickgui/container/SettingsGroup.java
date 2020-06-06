package me.lefted.lunacyforge.clickgui.container;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import me.lefted.lunacyforge.clickgui.screens.SettingsScreen;
import me.lefted.lunacyforge.utils.Logger;

/* function class that stores settings */
public class SettingsGroup {

    // ATTRIBUTES
    private List<SettingContainer> settings;
    private int posX;
    private int posY;
    private int width;
    private int height;

    private boolean available = true;

    // CONSTRUCTOR
    public SettingsGroup(List<SettingContainer> settings) {
	this.settings = new ArrayList<SettingContainer>();
	this.settings.addAll(settings);

	this.posX = settings.stream().min(Comparator.comparingInt(container -> container.getPosX())).get().getPosX();
	this.posY = settings.stream().min(Comparator.comparingInt(container -> container.getPosY())).get().getPosY();
	this.width = settings.stream().max(Comparator.comparingInt(container -> container.getWidth())).get().getWidth();
	this.height = settings.stream().mapToInt(container -> container.getHeight()).sum() + ((int) (this.settings.stream().filter(container -> container
	    .isAvailable()).count() - 1)) * SettingsScreen.CONTAINER_SPACING;
    }

    // METHODS
    public void addSettings(List<SettingContainer> settings) {
	try {
	    this.settings.addAll(settings);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	this.posX = this.settings.stream().min(Comparator.comparingInt(container -> container.getPosX())).get().getPosX();
	this.posY = this.settings.stream().min(Comparator.comparingInt(container -> container.getPosY())).get().getPosY();
	this.width = this.settings.stream().max(Comparator.comparingInt(container -> container.getWidth())).get().getWidth();
	this.height = this.settings.stream().mapToInt(container -> container.getHeight()).sum() + ((int) (this.settings.stream().filter(container -> container
	    .isAvailable()).count() - 1)) * SettingsScreen.CONTAINER_SPACING;
    }

    public void updateHeight() {
	this.height = this.settings.stream().filter(container -> container.isAvailable()).mapToInt(container -> container.getHeight()).sum()
	    + ((int) (this.settings.stream().filter(container -> container.isAvailable()).count() - 1)) * SettingsScreen.CONTAINER_SPACING;
    }

    public List<SettingContainer> getSettings() {
	return settings;
    }

    public int getPosX() {
	return posX;
    }

    public int getPosY() {
	return posY;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public void setPosY(int posY) {
	this.posY = posY;
    }

    public boolean isAvailable() {
	return available;
    }

    public void setAvailable(boolean available) {
	this.available = available;
    }
}
