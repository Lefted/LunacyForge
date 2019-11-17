package me.lefted.lunacyforge.modules;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.events.KeyPressEvent;

public class Jump extends Module{

    public Jump() {
	super("Jump", Category.MOVEMENT);
	this.markAsRage();
    }

    @EventTarget
    public void keyPress(KeyPressEvent event) {
	if (!this.isEnabled()) {
	    return;
	}
	if (event.getKey() == Keyboard.KEY_J) {
	    this.mc.thePlayer.jump();
	}
    }
    
    @Override
    public void onEnable() {
	EventManager.register(this);
    }

    @Override
    public void onDisable() {
	EventManager.unregister(this);
    }

}
