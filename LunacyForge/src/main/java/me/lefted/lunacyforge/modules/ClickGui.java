package me.lefted.lunacyforge.modules;

import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.clickgui.menus.SearchMenu;

public class ClickGui extends Module {

    // CONSTRUCTOR
    public ClickGui() {
	super("ClickGui", Category.RENDER);
    }

    // METHODS
    @Override
    public void onEnable() {
	mc.displayGuiScreen(SearchMenu.instance);
	toggle();
    }

    @Override
    public void onDisable() {
    }

}
