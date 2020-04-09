package me.lefted.lunacyforge.guiscreen.interpreter;

import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.guiscreen.Interpreter;
import me.lefted.lunacyforge.modules.Rectangle;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;

/*
 * The actual screen where you can change settings
 */
public class ModuleScreen extends Panel {

    // CONSTRUCTOR
    public ModuleScreen() {
	super(0, 0);
	this.setDrawDefaultBackground(true);
	this.setVerticalScrolling(true);
	this.setScrollMouseButtons(1);
    }

    // METHODS
    @Override
    public void initGui() {
	super.initGui();
	// clear prevous elements
	this.getElements().clear();

	// adds all available modules
	Interpreter.addInterpretedModules(this.getElements());
    }

}
