package me.lefted.lunacyforge.modules;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.clickgui.screens.SearchScreen;

public class ClickGui extends Module {

    // CONSTRUCTOR
    public ClickGui() {
	super("ClickGui", Category.RENDER);
	setKeycode(Keyboard.KEY_RSHIFT);
    }

    // METHODS
    @Override
    public void onEnable() {
	mc.displayGuiScreen(SearchScreen.instance);
	toggle();
    }

    @Override
    public void onDisable() {
    }
}
