package me.lefted.lunacyforge.guiscreen.interpreter;

import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.guiscreen.Interpreter;

public class ModuleScreen extends Panel {

    // CONSTRUCTOR
    public ModuleScreen() {
	super(0, 0);
	this.setDrawDefaultBackground(true);
	this.setVerticalScrolling(true);
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
