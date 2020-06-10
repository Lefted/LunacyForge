package me.lefted.lunacyforge.valuesystem;

import java.util.ArrayList;
import java.util.List;

public class NodeTree {

    // ATTRIBUTES
    private List<Node> nodeList; // stores all relations
    private boolean connected = false;

    // CONSTRUCTOR
    public NodeTree() {
	nodeList = new ArrayList<Node>();
    }

    // METHODS
    /**
     * connects all relations together
     * should be called whenever relations exist from the constructor of module
     */
    // Problem Player bekommt radius und width direkt als children
    public void connectNodes() {

	if (nodeList.size() > 0) {
	    // iterate through the list
	    for (Node parent : nodeList) {

		// check if it has children
		if (parent.getChildrenNames() != null && parent.getChildrenNames().length > 0) {
		    // link the children to the parent
		    Node[] children = new Node[parent.getChildrenNames().length];
		    for (int i = 0; i < parent.getChildrenNames().length; i++) {
			final String childName = parent.getChildrenNames()[i];
			children[i] = getNodeByValueName(childName);
		    }
		    parent.setChildrenNodes(children);
		}
	    }

	}
	connected = true;
    }

    public Node getNodeByValueName(String nodeName) {
	if (nodeList.size() > 0) {
	    return nodeList.stream().filter(node -> node.getValue().getValueName().equalsIgnoreCase(nodeName)).findAny().orElse(null);
	}

	return null;
    }

    public List<Node> getNodeList() {
	return nodeList;
    }
}
