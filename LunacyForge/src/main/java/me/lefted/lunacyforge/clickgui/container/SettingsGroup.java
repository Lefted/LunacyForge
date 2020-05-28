package me.lefted.lunacyforge.clickgui.container;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import me.lefted.lunacyforge.clickgui.screens.SettingsScreen;

/* function class that stores settings */
public class SettingsGroup {

    // ATTRIBUTES
    private List<SettingContainer> settings;
    private int posX;
    private int posY;
    private int width;
    private int height;

    // CONSTRUCTOR
    public SettingsGroup(List<SettingContainer> settings) {
	this.settings = new ArrayList<SettingContainer>();
	this.settings.addAll(settings);

	this.posX = settings.stream().min(Comparator.comparingInt(container -> container.getPosX())).get().getPosX();
	this.posY = settings.stream().min(Comparator.comparingInt(container -> container.getPosY())).get().getPosY();
	this.width = settings.stream().max(Comparator.comparingInt(container -> container.getWidth())).get().getWidth();
	this.height = settings.stream().mapToInt(container -> container.getHeight()).sum() + (settings.size() - 1) * SettingsScreen.CONTAINER_SPACING;

	// for (SettingContainer container : settings) {
	// this.height += container.getHeight() + settings.size() * SettingsScreen.CONTAINER_SPACING;
	// }

    }

    // METHODS
    public void addSettings(List<SettingContainer> settings) {
	try {
	    this.settings.addAll(settings);
	    // settings.forEach(setting -> this.settings.add(setting));
	} catch (Exception e) {
	    e.printStackTrace();
	}

	this.posX = this.settings.stream().min(Comparator.comparingInt(container -> container.getPosX())).get().getPosX();
	this.posY = this.settings.stream().min(Comparator.comparingInt(container -> container.getPosY())).get().getPosY();
	this.width = this.settings.stream().max(Comparator.comparingInt(container -> container.getWidth())).get().getWidth();
	this.height = this.settings.stream().mapToInt(container -> container.getHeight()).sum() + (this.settings.size() - 1) * SettingsScreen.CONTAINER_SPACING;
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
}
