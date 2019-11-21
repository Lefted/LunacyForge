package me.lefted.lunacyforge.modules;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.events.SmoothEvent;
import me.lefted.lunacyforge.injection.mixins.dummies.BoundingBoxer;
import me.lefted.lunacyforge.valuesystem.Value;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.util.MathHelper;

public class SmoothAimbot extends Module {

    // VALUES
    private Value<Float> intensityValue = new Value<Float>("intensity", Float.valueOf(5.0F));
    private Value<Boolean> targetAnimals = new Value<Boolean>("targetAnimals", Boolean.valueOf(true));
    private Value<Boolean> targetHostiles = new Value<Boolean>("targetHostiles", Boolean.valueOf(true));
    private Value<Boolean> targetPlayers = new Value<Boolean>("targetPlayers", Boolean.valueOf(true));

    // ATTRIBUTES
    private static float maximumRotation = 1.0F;
    private static float minimumRotation = 1.0E-4F;
    private static float aimFov = 90.0F;

    public SmoothAimbot() {
	super("SmoothAimbot", Category.COMBAT);
    }

    @EventTarget
    public void onSmooth(SmoothEvent e) {
	if (this.isEnabled()) {

	    try {
		double dist = 0.0D;
		double ang = 0.0D;
		for (Object ob : mc.theWorld.loadedEntityList) {
		    if (ob instanceof EntityLivingBase) {
			Entity entity = (Entity) ob;
			if (validate(entity) && canBeHit(entity)) {
			    final double distance = entity.getDistanceToEntity(mc.thePlayer);

			    this.faceEntity(entity, maximumRotation / 10.0F, minimumRotation);
			    return;
			}
		    }
		}
	    } catch (Exception exception) {
	    }
	}
    }

    private boolean validate(Entity entity) {
	final EntityPlayer player = this.mc.thePlayer;

	if (this.mc.theWorld == null || player == null || player.isDead || player.isPlayerSleeping() || entity.isDead) {
	    return false;
	}
	if (this.mc.currentScreen instanceof GuiContainer || this.mc.currentScreen instanceof GuiChat || this.mc.currentScreen instanceof GuiScreen) {
	    return false;
	}
	// if (!(entity instanceof EntityPlayer) && (Boolean) this.onlyPlayers.getValue()) {
	// return false;
	// }
	if (entity instanceof EntityPlayer) {
	    // if (!ModuleManager.getModule(NoFriends.class).isEnabled() && FriendManager.isPlayerFriendly(((EntityPlayer) entity).getGameProfile().getName()))
	    // {
	    // return false;
	    // }
	}
	if (!(player.getCurrentEquippedItem().getItem() instanceof Item)) {
	    return false;
	}
	if (player.getCurrentEquippedItem().getItem() instanceof ItemBlock || player.getCurrentEquippedItem().getItem() instanceof ItemBow) {
	    return false;
	}
	if (entity == player) {
	    return false;
	}
	if (!player.canEntityBeSeen(entity)) {
	    return false;
	}
	float dist = 4.0F;
	final Reach reach = (Reach) ModuleManager.getModule(Reach.class);
	if (reach.isEnabled()) {
	    dist = (float) reach.getValue("range").getObject();
	}
	if (player.getDistanceToEntity(entity) > dist) {
	    return false;
	}
	return true;
    }

    private boolean canBeHit(Entity entity) {
	if (!(entity instanceof EntityLivingBase)) {
	    return false;
	}
	float[] yawAndPitch = getRotationsNeeded((EntityLivingBase) entity);
	float yaw = yawAndPitch[0];
	float pitch = yawAndPitch[1];

	if (getDistanceBetweenAngles(mc.thePlayer.rotationYaw, yaw) < aimFov) {
	    if (getDistanceBetweenAngles(mc.thePlayer.rotationPitch, pitch) < aimFov) {
		return true;
	    }
	}
	return false;
    }

    private float getDistanceBetweenAngles(float angle1, float angle2) {
	return Math.abs(angle1 % 360.0F - angle2 % 360.0F) % 360.0F;
    }

    private float[] getRotationsNeeded(Entity entity) {
	final BoundingBoxer boundingBoxer = (BoundingBoxer) entity;
	
	double diffY;
	if (entity == null) {
	    return null;
	}
	double diffX = entity.posX - this.mc.thePlayer.posX;
	double diffZ = entity.posZ - this.mc.thePlayer.posZ;

	if (entity instanceof EntityLivingBase) {

	    EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
	    diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
	} else {

	    diffY = (boundingBoxer.getBoundingBox().minY + boundingBoxer.getBoundingBox().maxY) / 2.0D - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
	}
	double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
	float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
	return new float[] { mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper
	    .wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch) };
    }

    private void faceEntity(Entity entity, float yaw, float pitch) {
	final BoundingBoxer boundingBoxer = (BoundingBoxer) entity;
	
	double yDifference;
	if (entity == null) {
	    return;
	}
	double diffX = entity.posX - mc.thePlayer.posX;
	double diffZ = entity.posZ - mc.thePlayer.posZ;

	if (entity instanceof EntityLivingBase) {

	    EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
	    yDifference = entityLivingBase.posY + entityLivingBase.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
	} else {

	    yDifference = (boundingBoxer.getBoundingBox().minY + boundingBoxer.getBoundingBox().maxY) / 2.0D - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
	}
	double var14 = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	float var12 = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
	float var13 = (float) -(Math.atan2(yDifference, var14) * 180.0D / Math.PI);
	this.mc.thePlayer.rotationYaw = this.updateRotation(mc.thePlayer.rotationYaw, var12, yaw);
	// mc.thePlayer.rotationPitch = updateRotation(mc.thePlayer.rotationPitch, var13, pitch);
    }

    // Arguments: current rotation, intended rotation, max increment.
    private float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
	float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
	if (var4 > p_70663_3_) {
	    var4 = p_70663_3_;
	}
	if (var4 < -p_70663_3_) {
	    var4 = -p_70663_3_;
	}
	return p_70663_1_ + var4;
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
