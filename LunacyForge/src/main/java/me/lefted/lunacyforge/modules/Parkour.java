package me.lefted.lunacyforge.modules;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.events.UpdateEvent;
import me.lefted.lunacyforge.guiscreenold.interpreter.ModuleInterpreter;
import me.lefted.lunacyforge.implementations.IJumper;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;

@ModuleInterpreter(description = "Jumps at the perfect moment; useful for jump and runs")
public class Parkour extends Module {

    // DONE always bunnyhopping due to onUpdate being called twice
    // TODO FIXME jumping while parkour is jumping causes bunnyhop
    public Parkour() {
	super("Parkour", Category.MOVEMENT);
    }

    boolean wasJumping = false; 
    
    // is called twice
    @EventTarget
    public void onUpdate(UpdateEvent event) {
	if (!wasJumping && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && mc.thePlayer.onGround && !mc.thePlayer.isSneaking() && mc.theWorld
	    .getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).contract(0.001D, 0.0D, 0.001D)).isEmpty()
	    && (mc.thePlayer.isCollidedVertically || !mc.thePlayer.isAirBorne)) {
	    mc.thePlayer.jump();
	    wasJumping = true;
	} else if (wasJumping && mc.thePlayer.onGround && (mc.thePlayer.isCollidedVertically || !mc.thePlayer.isAirBorne)) {
	    wasJumping = false;
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
