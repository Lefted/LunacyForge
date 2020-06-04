package me.lefted.lunacyforge.valuesystem;

import java.util.function.Predicate;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;

public class Children<T> {

    // ATTRIBUTES
    /**
     * the id of this container
     */
    private int containerID;
    /**
     * the ids of the respective children containers
     */
    private int[] childrenIDs;
    /**
     * determines the availabiliy of the children
     */
    private Predicate<T> childrenAvailability;

    // CONSTRUCTOR
    /**
     * @param containerID          this containers id
     * @param childrenIDs          the ids of the children containers (may be null)
     * @param childrenAvailability controls when the children are available (may be null if there are no children)
     */
    public Children(int containerID, int[] childrenIDs, Predicate<T> childrenAvailability) {
	this.containerID = containerID;
	this.childrenIDs = childrenIDs;
	this.childrenAvailability = childrenAvailability;
    }

    // METHODS
    public int getContainerID() {
	return containerID;
    }

    public int[] getChildrenIDs() {
	return childrenIDs;
    }

    /**
     * @return the predicate tests true if the children are available (shown) and false if they should hide
     */
    public Predicate<T> getChildrenAvailability() {
	return childrenAvailability;
    }
}
