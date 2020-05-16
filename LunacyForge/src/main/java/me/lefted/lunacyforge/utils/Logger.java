package me.lefted.lunacyforge.utils;

import me.lefted.lunacyforge.LunacyForge;
import me.lefted.lunacyforge.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.ChatComponentText;

public class Logger {

    public static void logConsole(String message) {
	System.out.println(LunacyForge.PREFIX + message);
    }

    public static void logErrConsole(String message) {
	System.err.println(LunacyForge.PREFIX + message);
    }

    public static void logChatMessage(String message) {
	if (ClientConfig.isEnabled() && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null && Minecraft
	    .getMinecraft().gameSettings.chatVisibility != EnumChatVisibility.HIDDEN) {
	    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("§8[§6Lunacy§8] §r" + message));
	}
    }
}
