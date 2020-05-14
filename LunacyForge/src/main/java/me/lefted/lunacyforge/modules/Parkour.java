package me.lefted.lunacyforge.modules;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.events.UpdateEvent;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;

@ModuleInfo(description = "Jumps at the perfect moment\nUseful for jump and runs")
public class Parkour extends Module {

    // CONSTRUCTOR
    public Parkour() {
	super("Parkour", Category.MOVEMENT);
    }

    // METHODS
    @EventTarget
    public void onUpdate(UpdateEvent event) {
	if (!Minecraft.getMinecraft().thePlayer.isInWater() && !Minecraft.getMinecraft().thePlayer.isInLava() && Minecraft
	    .getMinecraft().thePlayer.onGround && !Minecraft.getMinecraft().thePlayer.isSneaking() && !Minecraft.getMinecraft().gameSettings.keyBindSneak
		.isPressed() && !Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed() && Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(
		    Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).contract(0.001D,
			0.0D, 0.001D)).isEmpty()) {
	    Minecraft.getMinecraft().thePlayer.jump();
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
