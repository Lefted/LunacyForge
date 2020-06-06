package me.lefted.lunacyforge.valuesystem;

import java.util.function.Consumer;
import java.util.function.Predicate;

import me.lefted.lunacyforge.LunacyForge;

/**
 * Project: LiquidBase ----------------------------------------------------------- Copyright © 2017 | CCBlueX | All rights reserved.
 */
public class Value<T> {

    // ATTRIBUTES
    private String valueName;
    private T valueObject;
    private Consumer<T> consumer;
    private Relation<T> relation;

    // CONSTRUCTOR
    public Value(String valueName, T defaultValueObject) {
	this(valueName, defaultValueObject, new Relation<T>(valueName, null, null));
    }

    // CONSTRUCTOR
    /**
     * @param valueName              value name in config
     * @param defaultValueObject     default value
     * @param childrenContainerNames the names of the children (valuenames)
     * @param childrenAvailability   when the children are available
     */
    public Value(String valueName, T defaultValueObject, String[] childrenContainerNames, Predicate<T> childrenAvailability) {
	this(valueName, defaultValueObject, new Relation<T>(valueName, childrenContainerNames, childrenAvailability));
    }

    // CONSTRUCTOR
    /**
     * @param valueName          value name in config
     * @param defaultValueObject default value
     * @param relation           {@link Relation} (may be null)
     */
    private Value(String valueName, T defaultValueObject, Relation relation) {
	this.valueName = valueName;
	this.valueObject = defaultValueObject;
	this.relation = relation;
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

    public Relation<T> getRelation() {
	return relation;
    }
}
