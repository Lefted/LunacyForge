package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.clickgui.annotations.CheckboxInfo;
import me.lefted.lunacyforge.clickgui.annotations.ColorInfo;
import me.lefted.lunacyforge.clickgui.annotations.ContainerInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider.NumberType;
import me.lefted.lunacyforge.valuesystem.Value;

@ModuleInfo(tags = { "Blaze", "Wither" }, description = "Sweet")
public class Cosmetics extends Module {

    // VALUES
    @CheckboxInfo(description = "Blazesticks")
    public Value<Boolean> useBlaze = new Value<Boolean>(this, "useBlaze", false);

    @ContainerInfo(groupID = 0)
    @CheckboxInfo(description = "Wither charge")
    public Value<Boolean> useCharge = new Value<Boolean>(this, "useCharge", false);

    @ContainerInfo(groupID = 0)
    @SliderInfo(description = "Velocity X", min = -10, max = 10, step = 1D, numberType = NumberType.INTEGER)
    public Value<Integer> chargeVelX = new Value<Integer>(this, "chargeVelX", 5);
    
    @ContainerInfo(groupID = 0)
    @SliderInfo(description = "Velocity Y", min = -10, max = 10, step = 1D, numberType = NumberType.INTEGER)
    public Value<Integer> chargeVelY = new Value<Integer>(this, "chargeVelY", 5);
    
    @ContainerInfo(groupID = 0)
    @ColorInfo(description = "Color", hasAlpha = false)
    public Value<float[]> chargeColor = new Value<float[]>(this, "chargeColor", new float[] {0.5f, 0.5f, 0.5f, 1f});
    
    // CONSTRUCTOR
    public Cosmetics() {
	super("Cosmetics", Category.RENDER);
    }

    // METHODS
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

}
