package me.lefted.lunacyforge.clickgui.screens;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import net.minecraft.client.settings.KeyBinding;

/* Menu that lets the user control client specific settings. */
public class ClientSettingsScreen extends SettingsScreen {

    // INSTANCE
    public static ClientSettingsScreen instance;

    // METHODS
    @Override
    public void addAllSettings(ArrayList<SettingContainer> settings) {

	SettingContainer test = new SettingContainer();
	settings.add(test);
    }

    @Override
    public void updateScreen() {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	// pass call to containers
	super.updateScreen();

	// TODO check if any text field is focused
	// movement
	// InventoryMove Credits @Andrew Saint 2.3
	KeyBinding[] moveKeys = new KeyBinding[] { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft,
		mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint };
	KeyBinding[] array = moveKeys;
	int length = moveKeys.length;
	for (int i = 0; i < length; ++i) {
	    KeyBinding bind = array[i];
	    KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
	}
    }
}
