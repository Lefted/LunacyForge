package me.lefted.lunacyforge.command.commands;

import org.lwjgl.input.Keyboard;

import me.lefted.lunacyforge.command.Command;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.Logger;

/**
 * Project: LiquidBase ----------------------------------------------------------- Copyright § 2017 | CCBlueX | All rights reserved.
 */
public class BindCommand extends Command {

    public BindCommand() {
	super("bind");
    }

    @Override
    public void execute(String[] strings) {
	if (strings.length > 2) {
	    final Module module = ModuleManager.getModuleByName(strings[1]);

	    if (module == null) {
		Logger.logChatMessage("§c§lError: §r§aThe entered module does not exist.");
		return;
	    }

	    final int key = Keyboard.getKeyIndex(strings[2].toUpperCase());

	    module.setKeycode(key);
	    Logger.logChatMessage("§cThe keybind of §a§l" + module.getName() + " §r§cwas set to §a§l" + Keyboard.getKeyName(key) + "§c.");
	    return;
	}

	Logger.logChatMessage("§c§lSyntax: §r§a.bind <module> <key>");
    }
}