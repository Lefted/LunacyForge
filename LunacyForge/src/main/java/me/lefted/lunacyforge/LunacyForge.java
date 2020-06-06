package me.lefted.lunacyforge;

import java.io.IOException;

import com.darkmagician6.eventapi.EventManager;

import me.lefted.lunacyforge.clickgui.screens.AddFriendScreen;
import me.lefted.lunacyforge.clickgui.screens.ClientSettingsScreen;
import me.lefted.lunacyforge.clickgui.screens.FriendSettingsScreen;
import me.lefted.lunacyforge.clickgui.screens.ModuleSettingsScreen;
import me.lefted.lunacyforge.clickgui.screens.SearchScreen;
import me.lefted.lunacyforge.command.CommandManager;
import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.config.ModuleConfig;
import me.lefted.lunacyforge.friends.FriendManager;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.valuesystem.RelationManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;

/* FEATUREREQUEST
 * 
 * color in sliders add default values to hover tips slider in ios slider umändern color picker mouseclicks should be picked up by keybind buttons
 * 
 * Hotbar Grouping for modules Blur for ui Spritze Animation Booststreifen dann
 * 
 * make proper grouping when changing availibility in parenting (makeChildrenAvailable) (makeChildrenUnavailable) rename children class to relation or something
 * make availibilities refresh on gui init 
 * 
 * schnellball marker für enderchests (iwann mal trajectories)
 * chest stealer
 * inv cleaner
 * forcefield (no rotations)
 * 
 * 
 * 
 * */

@Mod(modid = "lunacyforge")
public final class LunacyForge {

    // CONSTANTS
    public static final double CLIENT_BUILD = 1;
    public static final String CLIENT_NAME = "LunacyForge";
    public static final String PREFIX = "[Lunacy] ";
    public static LunacyForge instance;

    // ATTRIBUTES
    public ModuleManager moduleManager;
    public ModuleConfig moduleConfig;
    public ClientConfig clientConfig;
    public CommandManager commandManager;

    // CONSTRUCTOR
    public LunacyForge() {
	instance = this;
    }

    // METHODS
    public void startClient() {
	moduleManager = new ModuleManager();
	moduleConfig = new ModuleConfig();
	clientConfig = new ClientConfig();
	commandManager = new CommandManager();
	FriendManager.instance = new FriendManager();

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
	ModuleSettingsScreen.instance = new ModuleSettingsScreen();
	FriendSettingsScreen.instance = new FriendSettingsScreen();
	AddFriendScreen.instance = new AddFriendScreen();
	RelationManager.instance = new RelationManager();
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
