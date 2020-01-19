package me.lefted.lunacyforge.command.commands;

import me.lefted.lunacyforge.command.Command;
import me.lefted.lunacyforge.testgui.Screen;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;

public class GuiCommand extends Command{

    public GuiCommand() {
	super("gui");
    }
    
    @Override
    public void execute(String[] strings) {
	Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(Minecraft.getMinecraft().thePlayer));
	Logger.logChatMessage("d1");
    }

}
