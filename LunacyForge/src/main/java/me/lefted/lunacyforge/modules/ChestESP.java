package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.clickgui.annotations.ColorInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider.NumberType;
import me.lefted.lunacyforge.valuesystem.Value;

@ModuleInfo(description = "Allows you to see chests easier", tags = {"Container", "ESP"})
public class ChestESP extends Module{

    // VALUES
    @SliderInfo(description = "Line Width", min = 1, max = 5, step = 0.25D, numberType = NumberType.DECIMAL)
    public Value<Float> lineWidth = new Value<Float>(this, "lineWidth", 2F);
    
    @ColorInfo(description = "Chest Color", hasAlpha = true)
    public Value<float[]> chestColor = new Value<float[]>(this, "chestColor", new float[] {1F, 0.35F, 1F, 1F});
    
    @ColorInfo(description = "Chest Color", hasAlpha = true)
    public Value<float[]> enderchestColor = new Value<float[]>(this, "enderchestColor", new float[] {0.5F, 0F, 1F, 1F});
    
    // CONSTRUCTOR
    public ChestESP() {
	super("ChestESP", Category.RENDER);
    }

    // METHODS
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

}
