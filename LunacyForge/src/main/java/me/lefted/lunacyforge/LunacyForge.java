package me.lefted.lunacyforge;

import java.io.IOException;

import com.darkmagician6.eventapi.EventManager;

import me.lefted.lunacyforge.clickgui.screens.ClientSettingsScreen;
import me.lefted.lunacyforge.clickgui.screens.SearchScreen;
import me.lefted.lunacyforge.command.CommandManager;
import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.config.ModuleConfig;
import me.lefted.lunacyforge.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "lunacyforge")
public final class LunacyForge {

    // CONSTANTS
    public static final double CLIENT_BUILD = 1;
    public static final String CLIENT_NAME = "LunacyForge";
    public static final String PREFIX = "[Lunacy] ";
    public static LunacyForge instance;

    // ATTRIBUTES
    public final ModuleManager moduleManager = new ModuleManager();
    public final ModuleConfig moduleConfig = new ModuleConfig();
    public final ClientConfig clientConfig = new ClientConfig();
    public final CommandManager commandManager = new CommandManager();

    // CONSTRUCTOR
    public LunacyForge() {
	instance = this;
    }

    // METHODS
    public void startClient() {
	this.moduleManager.registerAllModules();
	this.commandManager.registerCommands();

	try {
	    this.moduleConfig.loadModules();
	    ModuleConfig.initDone = true;
	    this.clientConfig.load();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	SearchScreen.instance = new SearchScreen();
	ClientSettingsScreen.instance = new ClientSettingsScreen();
    }

    public void stopClient() {
	this.moduleConfig.saveModules();
	this.clientConfig.save();
	EventManager.unregister(moduleManager);
    }

    // returns the folder location
    public static String getPath() {
	String result = "";
	final String MC_DIR = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();

	if (MC_DIR.endsWith(".")) {
	    result = MC_DIR.substring(0, MC_DIR.length() - 1);
	} else {
	    result = MC_DIR + "\\";
	}
	result = result.concat(instance.CLIENT_NAME);

	return result;
    }
}
