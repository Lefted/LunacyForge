package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.clickgui.annotations.CheckboxInfo;
import me.lefted.lunacyforge.clickgui.annotations.ContainerInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.valuesystem.Value;

@ModuleInfo(tags = {"NoHurtcam", "StaticBow"}, description = "Sweet")
public class Animations extends Module{

    // VALUES
    @ContainerInfo(hoverText = "Prevents shaking when pulling your bow")
    @CheckboxInfo(description = "StaticBow")
    public Value<Boolean> useStaticBow = new Value<Boolean>(this, "useStaticBow", true);
    
    @ContainerInfo(hoverText = "Prevents the screen shaking when taking damage")
    @CheckboxInfo(description = "NoHurtcam")
    public Value<Boolean> useNoHurtcam = new Value<Boolean>(this, "useNoHurtcam", true);
    
    @CheckboxInfo(description = "NoSwing (Clientside)")
    public Value<Boolean> useNoSwing = new Value<Boolean>(this, "useNoSwing", false);
    
    // CONSTRUCTOR
    public Animations() {
	super("Animations", Category.PLAYER);
    }

    // METHODS
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

}
