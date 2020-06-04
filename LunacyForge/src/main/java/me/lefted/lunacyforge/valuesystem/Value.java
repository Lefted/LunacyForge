package me.lefted.lunacyforge.valuesystem;

import java.util.function.Consumer;

import me.lefted.lunacyforge.LunacyForge;

/**
 * Project: LiquidBase ----------------------------------------------------------- Copyright © 2017 | CCBlueX | All rights reserved.
 */
public class Value<T> {

    // ATTRIBUTES
    private String valueName;
    private T valueObject;
    private Consumer<T> consumer;
    private Children<T> children;

    // CONSTRUCTOR
    public Value(String valueName, T defaultValueObject) {
	this(valueName, defaultValueObject, null);
    }

    // CONSTRUCTOR
    /**
     * @param valueName          value name in config
     * @param defaultValueObject default value
     * @param children           {@link Children} (may be null)
     */
    public Value(String valueName, T defaultValueObject, Children children) {
	this.valueName = valueName;
	this.valueObject = defaultValueObject;
	this.children = children;
    }

    // METHODS
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
	LunacyForge.instance.moduleConfig.saveModules();
	if (this.consumer != null) {
	    this.consumer.accept(this.valueObject);
	}
    }

    public Children<T> getChildren() {
	return children;
    }
}
