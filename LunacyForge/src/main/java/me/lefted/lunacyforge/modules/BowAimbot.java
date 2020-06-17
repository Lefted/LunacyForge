package me.lefted.lunacyforge.modules;

import java.awt.Color;
import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.clickgui.annotations.CheckboxInfo;
import me.lefted.lunacyforge.clickgui.annotations.ColorInfo;
import me.lefted.lunacyforge.clickgui.annotations.ComboInfo;
import me.lefted.lunacyforge.clickgui.annotations.ContainerInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider.NumberType;
import me.lefted.lunacyforge.events.BowAimbotTimerEvent;
import me.lefted.lunacyforge.events.Render3DEvent;
import me.lefted.lunacyforge.friends.FriendManager;
import me.lefted.lunacyforge.implementations.ITimer;
import me.lefted.lunacyforge.utils.Logger;
import me.lefted.lunacyforge.valuesystem.NodeTreeManager;
import me.lefted.lunacyforge.valuesystem.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.MathHelper;

@ModuleInfo(description = "Shows a marker or\nGradually aims for you")
public class BowAimbot extends Module {

    // VALUES
    @ContainerInfo(groupID = 0)
    @ComboInfo(description = "Movement mode", entryNames = { "No movement", "Horizontal", "Vertical", "Both" })
    public Value<Integer> mode = new Value<Integer>(this, "mode", 0, new String[] { "intensity" }, newValue -> Value.createHandler(1, newValue != 0));

    @ContainerInfo(groupID = 0)
    @SliderInfo(description = "Intensity", min = 1, max = 8, step = 1, numberType = NumberType.INTEGER)
    public Value<Float> intensity = new Value<Float>(this, "intensity", 5F);

    @ContainerInfo(hoverText = "Draws a rectangle at the position where you should put your crosshair at", groupID = 1)
    @CheckboxInfo(description = "Show markers")
    public Value<Boolean> showMarkers = new Value<Boolean>(this, "showMarkers", true, new String[] { "markerColor" }, newValue -> Value.createHandler(1,
	newValue));

    @ContainerInfo(groupID = 1)
    @ColorInfo(description = "Marker Color", hasAlpha = true)
    public Value<float[]> markerColor = new Value<float[]>(this, "markerColor", new float[] { 1F, 0.7F, 0F, 1F });

    @ContainerInfo(groupID = 2)
    @CheckboxInfo(description = "Players")
    public Value<Boolean> includePlayers = new Value<Boolean>(this, "includePlayers", true);

    @ContainerInfo(groupID = 2)
    @CheckboxInfo(description = "Hostiles")
    public Value<Boolean> includeHostiles = new Value<Boolean>(this, "includeHostiles", false);

    @ContainerInfo(groupID = 2)
    @CheckboxInfo(description = "Animals")
    public Value<Boolean> includeAnimals = new Value<Boolean>(this, "includeAnimals", false);

    // CONSTANTS
    public static float MAXIMUM_ROTATION = 1.0f;
    public static float MINIMUM_ROTATION = 1.0E-4f;
    public static float AIM_FOV = 90.0f;

    // ATTRIBUTES
    private float drawYaw;
    private float drawPitch;
    private float velocity;
    private EntityLivingBase target;

    // CONSTRUCTOR
    public BowAimbot() {
	super("BowAimbot", Category.COMBAT);
	NodeTreeManager.INSTANCE.getTreeMap().get(this).connectNodes();

	intensity.setConsumer(new Consumer<Float>() {

	    @Override
	    public void accept(Float t) {
		((ITimer) Minecraft.getMinecraft()).getBowAimbotTimer().timerSpeed = t.floatValue() * 2.5F;
	    }
	});
    }

    // METHODS
    @Override
    public void onEnable() {
	EventManager.register(this);
    }

    @Override
    public void onDisable() {
	EventManager.unregister(this);
    }

    public void renderMarker(EntityLivingBase entity, double x, double y, double z) {
	if (mc.theWorld == null || mc.thePlayer == null || !isHoldingBow()) {
	    return;
	}

	if (!isEntityValid(entity)) {
	    return;
	}

	final EntityPlayer thePlayer = mc.thePlayer;
	final RenderManager renderManager = mc.getRenderManager();
	final FontRenderer fontrenderer = mc.fontRendererObj;

	final double diffX = entity.posX - thePlayer.posX;
	final double diffY = entity.posY + entity.getEyeHeight() - 0.15 - thePlayer.posY - thePlayer.getEyeHeight();
	final double diffZ = entity.posZ - thePlayer.posZ;
	final double hDistance = Math.sqrt(diffX * diffX + diffZ * diffZ);
	final double hDistanceSq = hDistance * hDistance;
	final float g = 0.006f;
	final double neededPitch = Math.toDegrees(Math.atan((1.0 - Math.sqrt(1.0 - 0.006000000052154064 * (0.006000000052154064 * hDistanceSq + 2.0 * diffY
	    * 1.0))) / (0.006000000052154064 * hDistance)));
	final double deltaY = Math.tan(Math.toRadians(neededPitch)) * hDistance - diffY;
	final String str = "ma";
	final float f = 1.6f;
	final float f2 = 0.016666668f * f;
	GlStateManager.pushMatrix();
	GlStateManager.translate((float) x + 0.0f, (float) y + entity.getEyeHeight() + deltaY, (float) z);
	GL11.glNormal3f(0.0f, 1.0f, 0.0f);
	GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
	GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
	GlStateManager.scale(-f2, -f2, f2);
	GlStateManager.disableLighting();
	GlStateManager.depthMask(false);
	GlStateManager.disableDepth();
	GlStateManager.enableBlend();
	GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	final Tessellator tessellator = Tessellator.getInstance();
	final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	final byte b0 = 0;
	final int i = fontrenderer.getStringWidth("ma") / 2;
	GlStateManager.disableTexture2D();

	final float[] color = markerColor.getObject();
	worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
	worldrenderer.pos(-i - 1, -1 + b0, 0.0).color(color[0], color[1], color[2], color[3]).endVertex();
	worldrenderer.pos(-i - 1, 10 + b0, 0.0).color(color[0], color[1], color[2], color[3]).endVertex();
	worldrenderer.pos(i + 1, 10 + b0, 0.0).color(color[0], color[1], color[2], color[3]).endVertex();
	worldrenderer.pos(i + 1, -1 + b0, 0.0).color(color[0], color[1], color[2], color[3]).endVertex();
	tessellator.draw();
	GlStateManager.enableTexture2D();
	GlStateManager.enableDepth();
	GlStateManager.depthMask(true);
	GlStateManager.enableLighting();
	GlStateManager.disableBlend();
	GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	GlStateManager.popMatrix();
    }

    private boolean isHoldingBow() {
	return mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem()
	    .getItem() instanceof ItemBow;
    }

    @EventTarget
    public void onBowTimer(BowAimbotTimerEvent event) {
	if (mode.getObject().intValue() == 0) {
	    return;
	}

	target = null;
	drawYaw = Float.NaN;
	drawPitch = Float.NaN;
	final EntityPlayer player = mc.thePlayer;

	if (mc.theWorld != null && player != null && isHoldingBow() && this.mc.gameSettings.keyBindUseItem.isKeyDown()) {

	    target = getBestLivingEntity();
	    smoothToTarget();
	} else {
	    this.velocity = -1.0f;
	}
    }

    /**
     * gradually changes pitch and yaw
     */
    private void smoothToTarget() {
	if (target == null) {
	    return;
	}

	final int bowCharge = mc.thePlayer.getItemInUseDuration();
	this.velocity = bowCharge / 20.0f;
	this.velocity = (this.velocity * this.velocity + this.velocity * 2.0f) / 3.0f;
	if (this.velocity >= 1.0f) {
	    this.velocity = 1.0f;
	}
	if (this.velocity != 1.0f) {
	    return;
	}
	final double posX = target.posX - mc.thePlayer.posX;
	final double posY = target.posY + target.getEyeHeight() - 0.15 - mc.thePlayer.posY - mc.thePlayer.getEyeHeight();
	final double posZ = target.posZ - this.mc.thePlayer.posZ;
	float yaw = mc.thePlayer.rotationYaw;
	float pitch = mc.thePlayer.rotationPitch;

	// if horizontal or both
	if (mode.getObject().intValue() == 1 || mode.getObject().intValue() == 3) {
	    yaw = (float) (Math.atan2(posZ, posX) * 180.0 / 3.141592653589793) - 90.0f;
	    this.updateYaw(drawYaw = yaw);
	}
	// if vertical or both
	if (mode.getObject().intValue() == 2 || mode.getObject().intValue() == 3) {
	    final double hDistance = Math.sqrt(posX * posX + posZ * posZ);
	    final double hDistanceSq = hDistance * hDistance;
	    final float g = 0.006f;
	    final float velocitySq = this.velocity * this.velocity;
	    final float velocityPow4 = velocitySq * velocitySq;
	    final double weisned = Math.atan((velocitySq - Math.sqrt(velocityPow4 - g * (g * hDistanceSq + 2.0 * posY * velocitySq))) / (g * hDistance));
	    final float neededPitch = (float) (-Math.toDegrees(weisned));
	    if (!Float.isNaN(neededPitch)) {
		pitch = neededPitch;
		this.updatePitch(drawPitch = pitch);
	    }
	}
    }

    private void updateYaw(final float intendedRotation) {
	mc.thePlayer.rotationYaw = updateRotation(mc.thePlayer.rotationYaw, intendedRotation, MAXIMUM_ROTATION / 10.0f);
    }

    private void updatePitch(float intentedPitch) {
	mc.thePlayer.rotationPitch = updateRotation(mc.thePlayer.rotationPitch, intentedPitch, MAXIMUM_ROTATION / 10.0f);
    }

    private float updateRotation(float currentRotation, float intendedRotation, float maxIncrement) {
	float change = MathHelper.wrapAngleTo180_float(intendedRotation - currentRotation);

	if (change > maxIncrement) {
	    change = maxIncrement;
	}

	if (change < -maxIncrement) {
	    change = -maxIncrement;
	}

	return currentRotation + change;
    }

    private EntityLivingBase getBestLivingEntity() {
	EntityLivingBase closestEntity = null;
	float distance = 9999.0f;

	// iterate over all living entities
	for (final Object obj : mc.theWorld.getLoadedEntityList()) {
	    if (obj instanceof EntityLivingBase) {
		final EntityLivingBase entity = (EntityLivingBase) obj;

		// validate
		if (!isEntityValid(entity)) {
		    continue;
		}

		// compare distance
		float newDist = mc.thePlayer.getDistanceToEntity(entity);
		if (entity instanceof IMob) {
		    newDist *= 1.5f;
		}
		if (entity instanceof IAnimals) {
		    newDist *= 3.0f;
		}
		if (closestEntity != null) {
		    if (distance <= newDist) {
			continue;
		    }
		    closestEntity = entity;
		    distance = newDist;
		} else {
		    closestEntity = entity;
		    distance = newDist;
		}
	    }
	}
	return closestEntity;
    }

    private boolean isEntityValid(EntityLivingBase entity) {
	if (entity == null || !entity.isEntityAlive() || entity.isDead || entity.isInvisible()) {
	    return false;
	}
	if (entity.isInvisibleToPlayer(mc.thePlayer)) {
	    return false;
	}

	if (entity instanceof EntityPlayer) {
	    // are players selected
	    if (includePlayers.getObject().booleanValue()) {
		final EntityPlayer player = (EntityPlayer) entity;
		// filter out sleeping players
		if (player.isPlayerSleeping() || player == mc.thePlayer) {
		    return false;
		}
		// filter out friends
		if (FriendManager.instance.isPlayerFriend(player.getGameProfile().getName())) {
		    return false;
		}
	    } else {
		return false;
	    }
	}

	if (entity instanceof EntityMob) {
	    // are hostiles selected
	    if (includeHostiles.getObject().booleanValue()) {
		return true;
	    } else {
		return false;
	    }
	}

	if (entity instanceof EntityAnimal) {
	    // are animals selected
	    if (includeAnimals.getObject().booleanValue()) {
		return true;
	    } else {
		return false;
	    }
	}

	// default false
	return false;
    }
}
