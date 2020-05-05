package me.lefted.lunacyforge.clickgui.container;

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
	this.settings = settings;

	this.posX = settings.stream().min(Comparator.comparingInt(container -> container.getPosX())).get().getPosX();
	this.posY = settings.stream().min(Comparator.comparingInt(container -> container.getPosY())).get().getPosY();
	this.width = settings.stream().max(Comparator.comparingInt(container -> container.getWidth())).get().getWidth();
	this.height = settings.stream().mapToInt(container -> container.getHeight()).sum() + settings.size() + SettingsScreen.CONTAINER_SPACING;
    }

    // METHODS
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
