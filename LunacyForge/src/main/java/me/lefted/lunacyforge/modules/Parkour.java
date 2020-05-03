package me.lefted.lunacyforge.modules;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.events.UpdateEvent;
import me.lefted.lunacyforge.guiscreenold.interpreter.ModuleInterpreter;
import net.minecraft.client.Minecraft;

@ModuleInterpreter(description = "Jumps at the perfect moment; useful for jump and runs")
public class Parkour extends Module {

    // TODO FIXME jumping while parkour is jumping
    public Parkour() {
	super("Parkour", Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
	if (!mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && mc.thePlayer.onGround && !mc.thePlayer.isSneaking() && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox()
	    .offset(0.0D, -0.5D, 0.0D).contract(0.001D, 0.0D, 0.001D)).isEmpty() && ((Minecraft.getMinecraft()).thePlayer.isCollidedVertically || !(Minecraft
		.getMinecraft()).thePlayer.isAirBorne)) {
	    mc.thePlayer.jump();
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
