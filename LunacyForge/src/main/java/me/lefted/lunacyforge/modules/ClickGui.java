package me.lefted.lunacyforge.modules;

import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.guiscreen.interpreter.ClickGuiScreen;

public class ClickGui extends Module {

    // CONSTRUCTOR
    public ClickGui() {
	super("ClickGui", Category.RENDER);
    }

    // METHODS
    @Override
    public void onEnable() {
	mc.displayGuiScreen(ClickGuiScreen.instance);
//	ClickGuiScreen.instance.initGui();
	toggle();
    }

    @Override
    public void onDisable() {
    }

}
