package me.lefted.lunacyforge.valuesystem;

import me.lefted.lunacyforge.LunacyForge;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
public class Value<T> {

    private String valueName;
    private T valueObject;

    public Value(String valueName, T defaultValueObject) {
        this.valueName = valueName;
        this.valueObject = defaultValueObject;
    }

    public String getValueName() {
        return valueName;
    }

    public T getObject() {
        return valueObject;
    }

    public void setObject(T valueObject) {
        this.valueObject = valueObject;
        LunacyForge.INSTANCE.moduleConfig.saveModules();
    }
}
