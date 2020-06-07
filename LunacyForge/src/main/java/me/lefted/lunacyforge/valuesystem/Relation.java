package me.lefted.lunacyforge.valuesystem;

import java.util.function.Predicate;

public class Relation<T> {

    // ATTRIBUTES
    /**
     * the name of this container
     */
    private String name;
    /**
     * the names of the respective children containers
     */
    private String[] childrenNames;
    /**
     * determines the availabiliy of the children containers
     */
    private Predicate<T> childrenAvailability;

    // CONSTRUCTOR
    /**
     * @param name                   this container's name (e.g. valuename)
     * @param childrenContainerNames the names of the children containers (may be null)
     * @param childrenAvailability   controls when the children are available (may be null if there are no children)
     */
    public Relation(String name, String[] childrenContainerNames, Predicate<T> childrenAvailability) {
	this.name = name;
	this.childrenNames = childrenContainerNames;
	this.childrenAvailability = childrenAvailability;
    }

    // METHODS
    public void hideThisAndChildren() {
	if (childrenNames != null && childrenNames.length > 0) {
	    for (String name : childrenNames) {
		
	    }
	}
    }
    
    public String getContainerName() {
	return name;
    }

    public String[] getChildrenContainerNames() {
	return childrenNames;
    }

    /**
     * @return the predicate tests true if the children are available (shown) and false if they should hide
     */
    public Predicate<T> getChildrenAvailability() {
	return childrenAvailability;
    }
}
