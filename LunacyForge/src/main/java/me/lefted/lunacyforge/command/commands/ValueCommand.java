package me.lefted.lunacyforge.command.commands;

import me.lefted.lunacyforge.command.Command;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.Logger;
import me.lefted.lunacyforge.valuesystem.Value;

/**
 * Project: LiquidBase ----------------------------------------------------------- Copyright § 2017 | CCBlueX | All rights reserved.
 */
public class ValueCommand extends Command {

    public ValueCommand() {
	super("value");
    }

    @Override
    public void execute(String[] strings) {
	if (strings.length > 3) {
	    final Module module = ModuleManager.getModuleByName(strings[1]);

	    if (module == null) {
		Logger.logChatMessage("§c§lError: §r§aThe entered module does not exist.");
		return;
	    }

	    final Value value = module.getValue(strings[2]);

	    if (value == null) {
		Logger.logChatMessage("§c§lError: §r§aThe entered value does not exist.");
		return;
	    }

	    if (value.getObject() instanceof Float) {
		final float newValue = Float.parseFloat(strings[3]);
		value.setObject(newValue);
		Logger.logChatMessage("§cThe value of §a§l" + module.getName() + " §8(§a§l" + value.getValueName() + "§8) §c was set to §a§l" + newValue + "§c.");
	    } else if (value.getObject() instanceof Boolean) {
		final boolean newValue = Boolean.parseBoolean(strings[3]);
		value.setObject(newValue);
		Logger.logChatMessage("§cThe value of §a§l" + module.getName() + " §8(§a§l" + value.getValueName() + "§8) §c was set to §a§l" + newValue + "§c.");
	    } else if (value.getObject() instanceof Integer) {
		final int newValue = Integer.parseInt(strings[3]);
		value.setObject(newValue);
		Logger.logChatMessage("§cThe value of §a§l" + module.getName() + " §8(§a§l" + value.getValueName() + "§8) §c was set to §a§l" + newValue + "§c.");
	    } else {
		Logger.logChatMessage("§c§lError: §r§aCould not identify:" + value.getObject().getClass().getName());
	    }
	    return;
	}

	Logger.logChatMessage("§c§lSyntax: §r§a.value <module> <valuename> <new_value>");
    }
}