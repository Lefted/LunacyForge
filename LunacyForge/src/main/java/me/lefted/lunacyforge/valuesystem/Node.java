package me.lefted.lunacyforge.valuesystem;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.screens.SettingsScreen;
import me.lefted.lunacyforge.modules.Module;
import net.minecraft.client.Minecraft;

public class Node<T> {

    // ATTRIBUTES
    private Value value;
    private SettingContainer container;

    // used for meta data and later translated into children[]
    private String[] childrenNames;
    private Node[] children;
    private Module module;

    /**
     * determines the availabiliy of the children containers
     */
    private Handler<T> childrenAvailabilityHandler;

    // CONSTRUCTOR
    /**
     * @param childrenContainerNames      the names of the children containers (may be null)
     * @param childrenAvailabilityHandler controls when the children are available (may be null if there are no children)
     */
    public Node(Module module, String[] childrenContainerNames, Handler<T> childrenAvailabilityHandler) {
	// this.name = name;
	this.childrenNames = childrenContainerNames;
	this.childrenAvailabilityHandler = childrenAvailabilityHandler;
	this.module = module;

	// get new or existing tree
	NodeTree tree = NodeTreeManager.INSTANCE.getOrCreateNodeTree(module);
	// add it to the tree
	tree.getNodeList().add(this);
    }

    // METHODS
    public boolean hasParents() {
	for (Node node2 : NodeTreeManager.INSTANCE.getTreeMap().get(module).getNodeList()) {

	    // iterate over all children
	    if (node2.getChildren() != null && node2.getChildren().length > 0) {
		for (Node child : node2.getChildren()) {
		    // if any child has this node as child
		    if (child.getValue().getValueName().equalsIgnoreCase(this.value.getValueName())) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    /**
     * Changes the availability of the children based on the handler behaviour
     * 
     * @param newValue The latest value of the element (e.g. Checkbox state)
     * @throws Exception
     */
    public void handleChildren(T newValue) throws Exception {
	if (childrenAvailabilityHandler != null && children != null && children.length > 0) {

	    final boolean[] newChildrenAvailabilities = childrenAvailabilityHandler.handle(newValue);

	    if (newChildrenAvailabilities.length != children.length) {
		throw new Exception("Handler returned size that doesn't match children count\n handler size: " + newChildrenAvailabilities.length
		    + " children count: " + children.length);
	    }

	    int i = 0;
	    for (boolean available : newChildrenAvailabilities) {
		final Node child = children[i];

		if (!available) {

		    // hide the child and its children
		    child.hideItselfAndChildren();
		} else {
		    // make the child available
		    SettingsScreen screen = (SettingsScreen) Minecraft.getMinecraft().currentScreen;
		    screen.setSettingContainersAvailability(child.container, true);
		    // child.getContainer().setAvailable(true);

		    // make the child handle its children with its latest value
		    child.handleChildren(child.getValue().getObject());
		}
		i++;
	    }
	}
    }

    public void hideItselfAndChildren() {
	// hide itself

	SettingsScreen screen = (SettingsScreen) Minecraft.getMinecraft().currentScreen;
	screen.setSettingContainersAvailability(container, false);
	// container.setAvailable(false);

	// hide children
	if (children != null && children.length > 0 && childrenNames != null && childrenNames.length > 0) {
	    for (Node child : children) {
		child.hideItselfAndChildren();
	    }
	}
    }

    public void setChildrenNodes(Node[] children) {
	this.children = children;
    }

    public Node[] getChildren() {
	return this.children;
    }

    public String[] getChildrenNames() {
	return childrenNames;
    }

    public Handler<T> getChildrenAvailabilityHandler() {
	return childrenAvailabilityHandler;
    }

    // called whenever a node is created
    public void setValue(Value<T> value) {
	this.value = value;
    }

    public Value getValue() {
	return value;
    }

    public SettingContainer getContainer() {
	return container;
    }

    // called whenever a container is created
    public void setContainer(SettingContainer container) {
	this.container = container;
    }
}
