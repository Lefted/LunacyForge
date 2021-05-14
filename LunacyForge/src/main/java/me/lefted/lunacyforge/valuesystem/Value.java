package me.lefted.lunacyforge.valuesystem;

import java.util.function.Consumer;

import me.lefted.lunacyforge.LunacyForge;
import me.lefted.lunacyforge.config.ModuleConfig;
import me.lefted.lunacyforge.modules.Module;
import scala.actors.threadpool.Arrays;

/**
 * Project: LiquidBase ----------------------------------------------------------- Copyright © 2017 | CCBlueX | All rights reserved.
 */
public class Value<T> {

    // ATTRIBUTES
    private String valueName;
    private T valueObject;
    private Consumer<T> consumer;

    private Node<T> node;

    private boolean rage;
    
    // CONSTRUCTOR
    /**
     * Constructor with no children
     * 
     * @param valueName          the name in the config
     * @param defaultValueObject the default value
     */
    public Value(Module module, String valueName, T defaultValueObject) {
	this(valueName, defaultValueObject, new Node<T>(module, null, null));
    }

    // CONSTRUCTOR
    /**
     * @param valueName            value name in config
     * @param defaultValueObject   default value
     * @param childrenNames        the names of the children (valuenames)
     * @param childrenAvailability when the children are available
     */
    public Value(Module module, String valueName, T defaultValueObject, String[] childrenNames, Handler<T> childrenAvailabilityHandler) {
	this(valueName, defaultValueObject, new Node<T>(module, childrenNames, childrenAvailabilityHandler));
    }

    // CONSTRUCTOR
    /**
     * @param valueName          value name in config
     * @param defaultValueObject default value
     * @param node               {@link Node} (may be null)
     */
    private Value(String valueName, T defaultValueObject, Node node) {
	this.valueName = valueName;
	this.valueObject = defaultValueObject;
	this.node = node;
	node.setValue(this);
    }

    // METHODS
    // USETHIS to create a array filled with true/false
    /**
     * Creates a handler where all children are affected the sames
     * 
     * @param count
     * @param value
     * @return
     */
    public static boolean[] createHandler(int childCount, boolean value) {
	boolean[] result = new boolean[childCount];
	Arrays.fill(result, value);
	return result;
    }

    public String getValueName() {
	return valueName;
    }

    public T getObject() {
	return valueObject;
    }

    public void setConsumer(Consumer<T> consumer) {
	this.consumer = consumer;
    }

    public void setObject(T valueObject) {
	this.valueObject = valueObject;
	if (ModuleConfig.initDone) {
	    LunacyForge.instance.moduleConfig.saveModules();
	}
	if (this.consumer != null) {
	    this.consumer.accept(this.valueObject);
	}
    }

    public Node<T> getNode() {
	return node;
    }
    
    public boolean isRage() {
	return rage;
    }
    
    public void setRage() {
	rage = true;
    }
}
