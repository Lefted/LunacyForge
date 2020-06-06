package me.lefted.lunacyforge.valuesystem;

import java.util.HashMap;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;

public class RelationManager {

    // INSTANCE
    public static RelationManager instance;

    // ATTRIBUTES
    /**
     * assisgns a name to a container
     */
    private HashMap<String, SettingContainer> relationMap;

    // CONSTRUCTOR
    public RelationManager() {
	this.relationMap = new HashMap<String, SettingContainer>();
    }

    // METHODS
    public void addContainerToMap(String name, SettingContainer container) {
	this.relationMap.put(name, container);
    }

    /**
     * @param name the name of the container
     * @return the container; if not found <b>null<b>
     */
    public SettingContainer getContainerByName(String name) {
	return this.relationMap.get(name);
    }

    public void clearMap() {
	relationMap.clear();
    }
    
    public HashMap<String, SettingContainer> getRelationMap() {
	return relationMap;
    }
}
