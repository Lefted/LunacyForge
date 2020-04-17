package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.guiscreenold.interpreter.ModuleInterpreter;

@ModuleInterpreter(description = "Makes everything brighter")
public class Fullbright extends Module{

    public Fullbright() {
	super("Fullbright", Category.RENDER);
    }

    @Override
    public void onEnable() {
	this.mc.gameSettings.gammaSetting = 100F;
    }

    @Override
    public void onDisable() {
	this.mc.gameSettings.gammaSetting = 1.0F;
    }
}
