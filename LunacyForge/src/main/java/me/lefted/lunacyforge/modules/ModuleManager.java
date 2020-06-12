package me.lefted.lunacyforge.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.events.KeyPressEvent;
import me.lefted.lunacyforge.events.Render2DEvent;
import me.lefted.lunacyforge.utils.ColorUtils;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ModuleManager {

    // ATTRIBUTES
    private static final List<Module> modules = new ArrayList<Module>();;

    // CONSTRUCTOR
    public ModuleManager() {
	EventManager.register(this);
    }

    // METHODS
    // registering modules goes here
    public void registerAllModules() {
	this.registerModule(new Reach());
	this.registerModule(new AimAssist());
	this.registerModule(new KeepSprint());
	this.registerModule(new Parkour());
	this.registerModule(new Fullbright());
	this.registerModule(new FastBridge());
	this.registerModule(new ClickGui());
	this.registerModule(new NoFriends());
	this.registerModule(new OutlineESP());
	this.registerModule(new ChestESP());
    }

    // listens for key inputs
    @EventTarget
    public void onKeyPress(KeyPressEvent event) {
	for (Module module : getModuleList()) {
	    if (module.getKeycode() == event.getKey()) {
		if (ClientConfig.isEnabled() || module == ModuleManager.getModule(ClickGui.class)) {
		    module.toggle();
		}
	    }
	}
    }

    // renders non-module specific stuff
    @EventTarget
    public void onRender2D(Render2DEvent event) {
	ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
	Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
	GlStateManager.enableBlend();

	// LUNACY
	if (ClientConfig.isEnabled() && ClientConfig.getArraylistMode() != 0) {

	    // ARRAY LIST
	    renderArrayList(scaledresolution);
	}
    }

    private void renderArrayList(ScaledResolution resolution) {
	resolution.getScaledWidth();
	resolution.getScaledHeight();
	final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

	// MODULE NAMES
	List<String> display = new ArrayList();
	for (Module module : ModuleManager.getModuleList()) {
	    if (module.isEnabled()) {
		display.add(String.valueOf(module.getName()));
	    }
	}

	// SORT
	Collections.sort(display, new Comparator<String>() {
	    @Override
	    public int compare(String m1, String m2) {
		if (fr.getStringWidth(m1) > fr.getStringWidth(m2)) {
		    return -1;
		}
		if (fr.getStringWidth(m1) < fr.getStringWidth(m2)) {
		    return 1;
		}
		return 0;
	    }
	});

	// RENDER
	int i = 0;
	for (String string : display) {
	    int width = fr.getStringWidth(string);
	    int posX = resolution.getScaledWidth() - width - 2;
	    int posY = 10 * i + 2;

	    if (ClientConfig.getArraylistMode() >= 3) {
		if (i == 0) {
		    Gui.drawRect(posX - 2, posY - 2, posX + width + 2, posY + 9, 0xEE252838);
		} else {
		    Gui.drawRect(posX - 2, posY - 1, posX + width + 2, posY + 9, 0xEE252838);
		}
	    }
	    if (ClientConfig.getArraylistMode() == 1 || ClientConfig.getArraylistMode() == 3) {
		fr.drawString(string, posX, posY, ColorUtils.rainbowEffect(200000000L, 1.0F).getRGB());
	    } else if (ClientConfig.getArraylistMode() == 2 || ClientConfig.getArraylistMode() == 4) {
		fr.drawStringWithShadow(string, posX, posY, ColorUtils.rainbowEffect(200000000L, 1.0F).getRGB());
	    }
	    i++;
	}
	display.clear();
    }

    private void registerModule(Module module) {
	if (!this.modules.contains(module)) {
	    this.modules.add(module);
	} else {
	    Logger.logErrConsole("Module + " + module.getName() + " already registered");
	}
    }

    public static List<Module> getModuleList() {
	return modules;
    }

    // by class
    public static Module getModule(Class<?> clazz) {
	for (final Module module : modules) {
	    if (module.getClass() == clazz) {
		return module;
	    }
	}
	return null;
    }

    // by name
    public static Module getModuleByName(String moduleName) {
	for (final Module module : modules) {
	    if (module.getName().equalsIgnoreCase(moduleName.trim())) {
		return module;
	    }
	}
	return null;
    }

}
