package me.lefted.lunacyforge.command.commands;

import me.lefted.lunacyforge.command.Command;
import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.utils.Logger;

public class ConfigCommand extends Command {

    public ConfigCommand() {
	super("config");
    }

    @Override
    public void execute(String[] strings) {
	if (strings.length == 2) {
	    if (strings[1].equalsIgnoreCase("list")) {
		listAllSettings();
		return;
	    }
	}
	
	if (strings.length > 2) {

	    final String setting = strings[1];
	    final String value = strings[2];

	    if (!(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))) {
		return;
	    }

	    if (setting.equalsIgnoreCase("announcemoduletoggle")) {
		ClientConfig.setAnnounceModuleToggle(Boolean.valueOf(value));
		Logger.logChatMessage("§cThe value of §a§l" + setting + "§c was set to §a§l" + value + "§c.");
		return;
	    } else if (setting.equalsIgnoreCase("enabled")) {
		ClientConfig.setEnabled(Boolean.valueOf(value));
		Logger.logChatMessage("§cThe value of §a§l" + setting + "§c was set to §a§l" + value + "§c.");
		return;
	    } else if (setting.equalsIgnoreCase("renderarraylist")) {
		ClientConfig.setRenderArrayList(Boolean.valueOf(value));
		Logger.logChatMessage("§cThe value of §a§l" + setting + "§c was set to §a§l" + value + "§c.");
		return;
	    } else if (setting.equalsIgnoreCase("showragemods")) {
		ClientConfig.setShowRageMods(Boolean.valueOf(value));
		Logger.logChatMessage("§cThe value of §a§l" + setting + "§c was set to §a§l" + value + "§c.");
		return;
	    }
	}
	
	Logger.logChatMessage("§c§lSyntax: §r§a.config <valuename> <new_value>");
	Logger.logChatMessage("§r§a.config list §r to list all available settings");
    }

    private void listAllSettings() {
	Logger.logChatMessage("-enabled");
	Logger.logChatMessage("-renderArrayList");
	Logger.logChatMessage("-announceModuleToggle");
	Logger.logChatMessage("-showRageMods");
    }
}
