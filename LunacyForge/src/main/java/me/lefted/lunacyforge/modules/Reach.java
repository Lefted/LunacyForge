package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.clickgui.annotations.ContainerInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider.NumberType;
import me.lefted.lunacyforge.valuesystem.Value;

/* Also see: MixinEntityRenderer.java */
@ModuleInfo(description = "Extends the distance from where you can hit things", tags = {"range", "hitdistance"})
public class Reach extends Module {

    // ATTRIBUTES
    @ContainerInfo(hoverText = "Default: 3")
    @SliderInfo(min = 0, max = 6, step = 0.125D, description = "Range in blocks", numberType = NumberType.DECIMAL)
    private Value<Float> rangeValue = new Value(this, "range", Float.valueOf(3.375F));

    // CONSTRUCTOR
    public Reach() {
	super("Reach", Category.COMBAT);
    }

    // METHODS
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

}
