package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;

@ModuleInfo(description = "Removes a left click cap and therefore may increase cps")
public class NoLeftClickDelay extends Module {

    // CONSTRUCTOR
    public NoLeftClickDelay() {
	super("NoLeftClickDelay", Category.COMBAT);
    }

    // METHODS
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

}
