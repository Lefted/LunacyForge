package me.lefted.lunacyforge.modules;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.lefted.lunacyforge.LunacyForge;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.config.ModuleConfig;
import me.lefted.lunacyforge.utils.Logger;
import me.lefted.lunacyforge.valuesystem.Value;
import net.minecraft.client.Minecraft;

public abstract class Module {

    // ATTRIBUTES
    private int keycode;
    private String name;
    private boolean enabled;
    private boolean rage;
    private Category category;

    // ACCESS
    protected Minecraft mc = Minecraft.getMinecraft();

    // CONSTRUCTOR
    public Module(String name, Category category) {
	this.name = name;
	this.category = category;
	this.enabled = false;
	this.rage = false;
	this.keycode = 0;
    }

    // METHODS
    // marks the module as rage
    protected void setRage() {
	this.rage = true;
    }

    public void toggle() {
	this.enabled = !this.enabled;

	if (this.enabled) {
	    this.onEnable();
	} else {
	    this.onDisable();
	}
	if (ClientConfig.isAnnounceModuleToggle()) {
	    Logger.logChatMessage("§6" + this.name + " §7has been" + (this.enabled ? " enabled" : " disabled"));
	}
	if (ModuleConfig.initDone) {
	    LunacyForge.instance.moduleConfig.saveModules();
	}
    }

    public abstract void onEnable();

    public abstract void onDisable();

    // TODO public abstract void onSettingsUpdate();

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	// if nothing would change
	if (enabled == this.isEnabled()) {
	    return;
	}

	this.enabled = enabled;
	if (this.enabled) {
	    this.onEnable();
	} else {
	    this.onDisable();
	}
	if (ModuleConfig.initDone) {
	    LunacyForge.instance.moduleConfig.saveModules();
	}
    }

    public Category getCategory() {
	return category;
    }

    public int getKeycode() {
	return keycode;
    }

    public void setKeycode(int keycode) {
	this.keycode = keycode;
	if (ModuleConfig.initDone) {
	    LunacyForge.instance.moduleConfig.saveModules();
	}
    }

    public String getName() {
	return name;
    }

    public boolean isRage() {
	return rage;
    }

    public Value getValue(final String valueName) {
	for (final Field field : getClass().getDeclaredFields()) {
	    try {
		field.setAccessible(true);

		final Object o = field.get(this);

		// System.out.println(field.getName());

		if (o instanceof Value) {
		    final Value value = (Value) o;

		    if (value.getValueName().equalsIgnoreCase(valueName))
			return value;
		}
	    } catch (IllegalAccessException e) {
		e.printStackTrace();
	    }
	}

	return null;
    }

    public List<Value> getValues() {
	final List<Value> values = new ArrayList<>();

	for (final Field field : getClass().getDeclaredFields()) {
	    try {
		field.setAccessible(true);

		final Object o = field.get(this);

		if (o instanceof Value)
		    values.add((Value) o);
	    } catch (IllegalAccessException e) {
		e.printStackTrace();
	    }
	}

	return values;
    }

    public String[] getTags() {
	final ModuleInfo info = this.getClass().getAnnotation(ModuleInfo.class);
	return info.tags();
	// return tags;
    }

    public boolean hasTags() {
	final ModuleInfo info = this.getClass().getAnnotation(ModuleInfo.class);
	return info != null && info.tags() != null && info.tags().length != 0;
	// return tags != null && tags.length != 0;
    }
}
