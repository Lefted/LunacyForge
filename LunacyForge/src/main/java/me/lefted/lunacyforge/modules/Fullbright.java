package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;

// FEATUREREQUEST add brightness value
@ModuleInfo(description = "Makes everything brighter")
public class Fullbright extends Module{

    // CONSTRUCTOR
    public Fullbright() {
	super("Fullbright", Category.RENDER);
    }

    // METHODS
    @Override
    public void onEnable() {
	this.mc.gameSettings.gammaSetting = 100F;
    }

    @Override
    public void onDisable() {
	this.mc.gameSettings.gammaSetting = 1.0F;
    }
}
