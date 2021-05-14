package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.clickgui.annotations.ContainerInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider.NumberType;
import me.lefted.lunacyforge.valuesystem.Value;

public class Velocity extends Module {

    // VALUES
    @ContainerInfo(hoverText = "( ͡° ʖ ͡°)")
    @SliderInfo(description = "Multiplier", max = 200, min = 0, step = 1D, numberType = NumberType.PERCENT)
    public Value<Integer> multiplier = new Value<Integer>(this, "multiplier", 100);
    
    // CONSTRUCTOR
    public Velocity() {
	super("Velocity", Category.PLAYER);
    }

    // METHODS
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

}
