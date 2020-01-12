package me.lefted.lunacyforge.command;

import java.util.ArrayList;
import java.util.List;

import me.lefted.lunacyforge.command.commands.BindCommand;
import me.lefted.lunacyforge.command.commands.ConfigCommand;
import me.lefted.lunacyforge.command.commands.ToggleCommand;
import me.lefted.lunacyforge.command.commands.ValueCommand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Project: LiquidBase ----------------------------------------------------------- Copyright © 2017 | CCBlueX | All rights reserved.
 * Also see: MixinGuiScreen.java
 */
@SideOnly(Side.CLIENT)
public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public void registerCommands() {
	registerCommand(new BindCommand());
	registerCommand(new ValueCommand());
	registerCommand(new ToggleCommand());
	registerCommand(new ConfigCommand());
    }

    public void registerCommand(final Command command) {
	commands.add(command);
    }

    public void callCommand(final String s) {
	final String[] strings = s.split(" ");
	commands.stream().filter(command -> strings[0].equalsIgnoreCase("." + command.getName()) || strings[0].equalsIgnoreCase("\\" + command.getName())).forEach(command -> command.execute(strings));
    }
}