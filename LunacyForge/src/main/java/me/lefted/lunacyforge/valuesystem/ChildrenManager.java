package me.lefted.lunacyforge.valuesystem;

import java.util.HashMap;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;

public class ChildrenManager {

    // INSTANCE
    public static ChildrenManager instance;

    // ATTRIBUTES
    private HashMap<Integer, SettingContainer> childrenMap;

    // CONSTRUCTOR
    public ChildrenManager() {
	this.childrenMap = new HashMap<Integer, SettingContainer>();
    }

    // METHODS
    public void addContainerToMap(int id, SettingContainer container) {
	this.childrenMap.put(id, container);
    }

    /**
     * @param id of the container
     * @return the container; if not found <b>null<b>
     */
    public SettingContainer getContainerByID(int id) {
	return this.childrenMap.get(id);
    }

    public void clearMap() {
	childrenMap.clear();
    }
}
