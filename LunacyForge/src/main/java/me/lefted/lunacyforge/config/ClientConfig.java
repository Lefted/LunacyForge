package me.lefted.lunacyforge.config;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import me.lefted.lunacyforge.LunacyForge;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Gartn
 *
 */
@SideOnly(Side.CLIENT)
public final class ClientConfig {

    // ATTRIBUTES
    private final File dir = new File(Minecraft.getMinecraft().mcDataDir, LunacyForge.CLIENT_NAME);
    private final File saveFile = new File(dir, "clientconfig.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // SETTINGS AND DEFAULT VALUES
    private boolean enabled = true;
    private boolean announceModuleToggle = false;
    /**
     * 0 off, 1 normal, 2 shadow, 3 normal rect, 4 shadow rect
     */
    private int arraylistMode = 3;
    private boolean showRageMods = false;
    private Color guiColor = new Color(0x017AFF);

    // CONSTRUCTOR
    public ClientConfig() {
	dir.mkdirs();
    }

    // METHODS
    public void save() {
	if (!saveFile.exists()) {
	    try {
		saveFile.createNewFile();
	    } catch (IOException e) {
		Logger.logConsole("clientconfig.json already exists");
	    }
	}

	final JsonObject jsonObject = new JsonObject();

	jsonObject.addProperty("enabled", isEnabled());
	jsonObject.addProperty("announceModuleToggle", isAnnounceModuleToggle());
	// jsonObject.addProperty("renderArrayList", isRenderArrayList());
	// jsonObject.addProperty("renderArrayListBg", isRenderArrayListBg());
	jsonObject.addProperty("arraylistMode", getArraylistMode());
	jsonObject.addProperty("showRageMods", isShowRageMods());
	jsonObject.addProperty("guicolor", getGuiColor().getRGB());

	try {
	    final PrintWriter printWriter = new PrintWriter(saveFile);
	    printWriter.println(gson.toJson(jsonObject));
	    printWriter.flush();
	    printWriter.close();
	} catch (IOException e) {
	    Logger.logErrConsole("Error occured while writing clientconfig.json");
	    e.printStackTrace();
	}
    }

    public void load() {
	if (!saveFile.exists()) {
	    try {
		saveFile.createNewFile();
	    } catch (IOException e) {
		Logger.logConsole("clientconfig.json already exists");
	    }
	    save();
	    return;
	}
	try {
	    final BufferedReader bufferedReader = new BufferedReader(new FileReader(saveFile));
	    final JsonElement jsonElement = gson.fromJson(bufferedReader, JsonElement.class);

	    if (jsonElement instanceof JsonNull) {
		return;
	    }

	    final JsonObject jsonObject = (JsonObject) jsonElement;

	    if (jsonObject.has("enabled")) {
		setEnabled(jsonObject.get("enabled").getAsBoolean());
	    }
	    if (jsonObject.has("announceModuleToggle")) {
		setAnnounceModuleToggle(jsonObject.get("announceModuleToggle").getAsBoolean());
	    }
	    if (jsonObject.has("arraylistMode")) {
		setArraylistMode(jsonObject.get("arraylistMode").getAsInt());
	    }
	    // if (jsonObject.has("renderArrayList")) {
	    // setRenderArrayList(jsonObject.get("renderArrayList").getAsBoolean());
	    // }
	    // if (jsonObject.has("renderArrayListBg")) {
	    // setRenderArrayListBg(jsonObject.get("renderArrayListBg").getAsBoolean());
	    // }
	    if (jsonObject.has("showRageMods")) {
		setShowRageMods(jsonObject.get("showRageMods").getAsBoolean());
	    }
	    if (jsonObject.has("guicolor")) {
		setGuiColor(new Color(jsonObject.get("guicolor").getAsInt()));
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	this.save();
    }

    // GETTERS AND SETTERS
    public static boolean isEnabled() {
	return LunacyForge.instance.clientConfig.enabled;
    }

    public static void setEnabled(boolean enabled) {
	LunacyForge.instance.clientConfig.enabled = enabled;
    }

    public static boolean isAnnounceModuleToggle() {
	return LunacyForge.instance.clientConfig.announceModuleToggle;
    }

    public static void setAnnounceModuleToggle(boolean announceModuleToggle) {
	LunacyForge.instance.clientConfig.announceModuleToggle = announceModuleToggle;
    }

    // public static boolean isRenderArrayList() {
    // return LunacyForge.instance.clientConfig.renderArrayList;
    // }
    //
    // public static boolean isRenderArrayListBg() {
    // return LunacyForge.instance.clientConfig.renderArrayListBg;
    // }
    //
    // public static void setRenderArrayList(boolean renderArrayList) {
    // LunacyForge.instance.clientConfig.renderArrayList = renderArrayList;
    // }
    //
    // public static void setRenderArrayListBg(boolean renderArrayListBg) {
    // LunacyForge.instance.clientConfig.renderArrayListBg = renderArrayListBg;
    // }

    /**
     * @return 0 off, 1 normal, 2 shadow, 3 normal rect, 4 shadow rect
     */
    public static int getArraylistMode() {
	return LunacyForge.instance.clientConfig.arraylistMode;
    }

    /**
     * @param arraylistMode 0 off, 1 normal, 2 shadow, 3 normal rect, 4 shadow rect
     * 
     */
    public static void setArraylistMode(int arraylistMode) {
	LunacyForge.instance.clientConfig.arraylistMode = arraylistMode;
    }

    public static boolean isShowRageMods() {
	return LunacyForge.instance.clientConfig.showRageMods;
    }

    public static void setShowRageMods(boolean showRageMods) {
	LunacyForge.instance.clientConfig.showRageMods = showRageMods;
    }

    public static Color getGuiColor() {
	return LunacyForge.instance.clientConfig.guiColor;
    }

    public static void setGuiColor(Color guiColor) {
	LunacyForge.instance.clientConfig.guiColor = guiColor;
    }

    public static void saveConfig() {
	LunacyForge.instance.clientConfig.save();
    }
}
