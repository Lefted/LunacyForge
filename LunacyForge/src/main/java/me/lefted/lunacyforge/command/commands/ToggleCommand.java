package me.lefted.lunacyforge.command.commands;

import me.lefted.lunacyforge.command.Command;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.Logger;

/**
 * Copyright § 2015 - 2017 | CCBlueX | All rights reserved.
 * <p>
 * LiquidBase - By CCBlueX(Marco)
 */
public class ToggleCommand extends Command {

    public ToggleCommand() {
	super("toggle");
    }

    @Override
    public void execute(String[] strings) {
	if (strings.length > 1) {
	    final Module module = ModuleManager.getModuleByName(strings[1]);

	    if (module == null) {
		Logger.logChatMessage("§c§lError: §r§aThe entered module does not exist.");
		return;
	    }

	    module.setEnabled(!module.isEnabled());
	    Logger.logChatMessage("§cToggled module.");
	    return;
	}

	Logger.logChatMessage("§c§lSyntax: §r§a.toggle <module>");
    }
}
