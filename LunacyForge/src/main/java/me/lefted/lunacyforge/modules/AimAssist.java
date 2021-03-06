package me.lefted.lunacyforge.modules;

import java.util.function.Consumer;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.clickgui.annotations.CheckboxInfo;
import me.lefted.lunacyforge.clickgui.annotations.ContainerInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider.NumberType;
import me.lefted.lunacyforge.events.AimAssistTimerEvent;
import me.lefted.lunacyforge.friends.FriendManager;
import me.lefted.lunacyforge.implementations.ITimer;
import me.lefted.lunacyforge.valuesystem.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.util.MathHelper;

@ModuleInfo(description = "Gradually aims at the nearest target\nLike a magnet which pulls your crosshair to the enemey", tags = { "SmoothAimbot" })
public class AimAssist extends Module {

    // VALUES
    @ContainerInfo(hoverText = "Intensity the crosshair locks to the target")
    @SliderInfo(min = 1, max = 8, step = 1D, description = "Intensity", numberType = NumberType.INTEGER)
    private Value<Float> intensityValue = new Value<Float>(this, "intensity", 5F);
    
    @CheckboxInfo(description = "Target Animals")
    private Value<Boolean> targetAnimals = new Value<Boolean>(this, "targetAnimals", true);

    @CheckboxInfo(description = "Target Hostiles")
    private Value<Boolean> targetHostiles = new Value<Boolean>(this, "targetHostiles", true);

    @CheckboxInfo(description = "Target Players")
    private Value<Boolean> targetPlayers = new Value<Boolean>(this, "targetPlayers", true);

    // ATTRIBUTES
    private static float maximumRotation = 1.0F;
    private static float minimumRotation = 1.0E-4F;
    private static float aimFov = 90.0F;

    public AimAssist() {
	super("AimAssist", Category.COMBAT);
	this.intensityValue.setConsumer(new Consumer<Float>() {

	    @Override
	    public void accept(Float t) {
		((ITimer) Minecraft.getMinecraft()).getAimAssistTimer().timerSpeed = t.floatValue() * 2.5F;
	    }
	});
    }

    @EventTarget
    public void onTimer(AimAssistTimerEvent e) {
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

	if (entity instanceof EntityAnimal && !targetAnimals.getObject().booleanValue()) {
	    return false;
	}

	if (entity instanceof EntityMob && !targetHostiles.getObject().booleanValue()) {
	    return false;
	}

	if (entity instanceof EntityPlayer) {
	    if (!targetPlayers.getObject().booleanValue()) {
		return false;
	    }
	    if (!ModuleManager.getModule(NoFriends.class).isEnabled() && FriendManager.instance.isPlayerFriend(((EntityPlayer) entity).getGameProfile().getName())) {
		return false;
	    }
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

	    diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
	}
	double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
	float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
	return new float[] { mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper
	    .wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch) };
    }

    private void faceEntity(Entity entity, float yaw, float pitch) {

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

	    yDifference = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
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
