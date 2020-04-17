package me.lefted.lunacyforge.guiscreenold;

import java.awt.Color;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.guiapi.Label;
import me.lefted.lunacyforge.guiscreenold.interpreter.ModuleInterpreter;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.modules.Rectangle;
import me.lefted.lunacyforge.utils.ColorUtils;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;

public class Interpreter {

    /*
     * @GuiInterpreter(type = "LABEL", name = "description = "sneaks and unsneaks at the end of a block");
     * public class FastBridge extends Module
     * 
     * @GuiInterpreter(type = "CHECKBOX", description = "target players", tooltip = "")
     * private Value targetPlayerValue = new Value<Boolean>();
     * @GuiInterpreter(type = "BUTTON_SELECTION", names = {"Hexception", "Lefted", "Custom"}, values = {0, 1, 2}, description = "OutlineESP mode", tooltip = "")
     *
     *
     */

    /*
     * adds all available modules including their name, toggles and settings
     */
    public static void addInterpretedModules(ArrayList<Element> elementList) {

	for (int i = 0; i < ModuleManager.getModuleList().size(); i++) {
	    final Module module = ModuleManager.getModuleList().get(i);

	    if (Interpreter.moduleHasClassAnnotation(module)) {
		// creates the moduleElement (Box with Name, Keybind, Toggle, Options)
		ModuleElement moduleElement = new ModuleElement(module, i, elementList);
		elementList.add(moduleElement);
	    }
	}
    }

    /*
     * returns if the module has the annotation @ModuleInterpreter(description = "does things") where the description is optional
     */
    private static boolean moduleHasClassAnnotation(Module module) {
	final Class moduleClass = module.getClass();

	for (Annotation annotation : moduleClass.getDeclaredAnnotations()) {
	    if (annotation instanceof ModuleInterpreter) {
		return true;
	    }
	}
	return false;
    }

}
