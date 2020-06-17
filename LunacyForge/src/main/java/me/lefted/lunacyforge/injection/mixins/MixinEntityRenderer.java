package me.lefted.lunacyforge.injection.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkmagician6.eventapi.EventManager;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.events.Render3DEvent;
import me.lefted.lunacyforge.implementations.ISetupCameraTransformAccessor;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.modules.Reach;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/* Also see: Reach.java */
@SideOnly(Side.CLIENT)
@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer implements ISetupCameraTransformAccessor {

    @Shadow
    private Minecraft mc;

    @Shadow
    private Entity pointedEntity;

    @Overwrite
    public void getMouseOver(float p_getMouseOver_1_) {
	Entity entity = this.mc.getRenderViewEntity();

	if (entity != null) {
	    if (this.mc.theWorld != null) {

		this.mc.mcProfiler.startSection("pick");
		this.mc.pointedEntity = null;
		double d0 = this.mc.playerController.getBlockReachDistance();
		this.mc.objectMouseOver = entity.rayTrace(d0, p_getMouseOver_1_);
		double d1 = d0;
		Vec3 vec3 = entity.getPositionEyes(p_getMouseOver_1_);
		boolean flag = false;
		boolean flag1 = true;
		// int i = 3;

		if (this.mc.playerController.extendedReach()) {
		    d0 = 6.0D;
		    d1 = 6.0D;
		} else if (d0 > 3.0D) {
		    // flag = true;
		    d1 = 3.0D;
		}
		d0 = d1;

		// REACH
		final Reach reach = (Reach) ModuleManager.getModule(Reach.class);
		if (ClientConfig.isEnabled() && reach.isEnabled()) {
		    final float rangeValue = (float) reach.getValue("range").getObject();

		    d0 = rangeValue;
		    d1 = rangeValue;
		}

		if (this.mc.objectMouseOver != null) {
		    d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
		}

		Vec3 vec31 = entity.getLook(p_getMouseOver_1_);
		Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
		this.pointedEntity = null;
		Vec3 vec33 = null;
		float f = 1.0F;
		List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord
		    * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
			public boolean apply(Entity p_apply_1_) {
			    return p_apply_1_.canBeCollidedWith();
			}
		    }));
		double d2 = d1;

		for (int j = 0; j < list.size(); j++) {

		    Entity entity1 = (Entity) list.get(j);
		    float f1 = entity1.getCollisionBorderSize();
		    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
		    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

		    if (axisalignedbb.isVecInside(vec3)) {

			if (d2 >= 0.0D) {
			    this.pointedEntity = entity1;
			    vec33 = (movingobjectposition == null) ? vec3 : movingobjectposition.hitVec;
			    d2 = 0.0D;
			}

		    } else if (movingobjectposition != null) {

			double d3 = vec3.distanceTo(movingobjectposition.hitVec);

			if (d3 < d2 || d2 == 0.0D) {
			    if (entity1 == entity.ridingEntity && !entity.canRiderInteract()) {

				if (d2 == 0.0D) {
				    this.pointedEntity = entity1;
				    vec33 = movingobjectposition.hitVec;
				}

			    } else {

				this.pointedEntity = entity1;
				vec33 = movingobjectposition.hitVec;
				d2 = d3;
			    }
			}
		    }
		}

		// if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > 3.0D) {
		//
		// this.pointedEntity = null;
		// this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing) null, new BlockPos(
		// vec33));
		// }

		if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {

		    this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);

		    if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof net.minecraft.entity.item.EntityItemFrame) {
			this.mc.pointedEntity = this.pointedEntity;
		    }
		}

		this.mc.mcProfiler.endSection();
	    }
	}
    }

    @Inject(method = { "renderWorldPass" }, at = {
	    @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE) })
    private void renderWorldPass(final int pass, final float partialTicks, final long finishTimeNano, final CallbackInfo callbackInfo) {
	EventManager.call(new Render3DEvent(partialTicks));
    }

    // USETHIS to access setupCameraTransform
    @Override
    public void setupCameraTransformAccessor(float partialTicks, int pass) {
	this.setupCameraTransform(partialTicks, pass);
    }

    @Shadow
    public abstract void setupCameraTransform(float partialTicks, int pass);

}
