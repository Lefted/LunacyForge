package me.lefted.lunacyforge.valuesystem;

import java.util.HashMap;
import java.util.Map;

import me.lefted.lunacyforge.modules.Module;

public class NodeTreeManager {

    // INSTANCE
    public static final NodeTreeManager INSTANCE = new NodeTreeManager();

    // ATTRIBUTES
    private Map<Module, NodeTree> treeMap;

    // CONSTRUCTOR
    public NodeTreeManager() {
	treeMap = new HashMap<Module, NodeTree>();
    }

    // METHODS
    public NodeTree getOrCreateNodeTree(Module module) {
	if (treeMap.containsKey(module)) {
	    // return existing tree
	    return treeMap.get(module);
	} else {
	    // create new tree
	    NodeTree tree = new NodeTree();
	    // put it in the map
	    treeMap.put(module, tree);
	    return tree;
	}
    }

    public Map<Module, NodeTree> getTreeMap() {
	return treeMap;
    }

    public void setTreeMap(Map<Module, NodeTree> treeMap) {
	this.treeMap = treeMap;
    }

}
