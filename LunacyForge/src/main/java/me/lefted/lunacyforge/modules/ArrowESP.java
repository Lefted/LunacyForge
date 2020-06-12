package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.clickgui.annotations.ColorInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider.NumberType;
import me.lefted.lunacyforge.valuesystem.Value;

@ModuleInfo(description = "Allows you to see arrows easier", tags = { "ESP" })
public class ArrowESP extends Module {

    // VALUES
    @SliderInfo(description = "Line Width", min = 1, max = 10, step = 1D, numberType = NumberType.DECIMAL)
    public Value<Float> lineWidth = new Value<Float>(this, "lineWidth", 5F);

    @ColorInfo(description = "Color", hasAlpha = true)
    public Value<float[]> color = new Value<float[]>(this, "color", new float[] { 0.6F, 0.7F, 0F, 1F });

    // CONSTRUCTOR
    public ArrowESP() {
	super("ArrowESP", Category.RENDER);
    }

    // METHODS
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

}
