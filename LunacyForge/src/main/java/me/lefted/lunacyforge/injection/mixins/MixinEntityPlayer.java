package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.lefted.lunacyforge.modules.KeepSprint;
import me.lefted.lunacyforge.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/* Also see: KeepSprint.java */

@SideOnly(Side.CLIENT)
@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {

    public MixinEntityPlayer(World p_i1594_1_) {
	super(p_i1594_1_);
    }

    /* aslong as KeepSpring is enabled preserve motionX*/
    @Redirect(method = "attackTargetEntityWithCurrentItem", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;motionX:D", opcode = Opcodes.PUTFIELD, ordinal = 0, args = "log=false"))
    private void setFieldValue(EntityPlayer owner, double value) {
	if (!ModuleManager.getModule(KeepSprint.class).isEnabled()) {
	    Minecraft.getMinecraft().thePlayer.motionX = value;
	}
    }

    /* aslong as KeepSpring is enabled preserve motionZ*/
    @Redirect(method = "attackTargetEntityWithCurrentItem", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;motionZ:D", opcode = Opcodes.PUTFIELD, ordinal = 0, args = "log=false"))
    private void setFieldValue2(EntityPlayer owner, double value) {
	if (!ModuleManager.getModule(KeepSprint.class).isEnabled()) {
	    Minecraft.getMinecraft().thePlayer.motionZ = value;
	}
    }

    /* aslong as KeepSpring is enabled preserve sprinting*/
    @Redirect(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;setSprinting(Z)V", ordinal = 0, args = "log=false"))
    private void setSprintingProxy(EntityPlayer owner, boolean value) {
	if (!ModuleManager.getModule(KeepSprint.class).isEnabled()) {
	    Minecraft.getMinecraft().thePlayer.setSprinting(value);
	}
    }

    // @Inject(method = "attackTargetEntityWithCurrentItem", at = @At(target = "INVOKE", value = "",
    // args = {"log=true"}))
    // public void onKeepSprint(CallbackInfo callbackInfo) {
    //
    // }
    //

    // @ModifyVariable( method = { "attackTargetEntityWithCurrentItem" }, at = @At(value = "FIELD",
    // target = "Lnet/minecraft/entity/Entity;motionX:D;"))
    // private double motionX(double motionX) {
    // Logger.logConsole(motionX + "");
    // return 0D;
    // }

    // @ModifyVariable(method = "attackTargetEntityWithCurrentItem", at = @At(value = "STORE", ordinal
    // = 0), require = 1, print = true)
    // private double modifiedMotionX(double d0) {
    // Logger.logChatMessage(d0 + "");
    // return d0 * 100D;
    // }

    // @Overwrite
    // public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
    // if (targetEntity.canAttackWithItem()) {
    // if (!targetEntity.hitByEntity(this)) {
    // float f = (float)
    // this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
    // int i = 0;
    // float f1 = 0.0F;
    //
    // if (targetEntity instanceof EntityLivingBase) {
    // f1 = EnchantmentHelper.getModifierForCreature(this.getHeldItem(), ((EntityLivingBase)
    // targetEntity).getCreatureAttribute());
    // } else {
    // f1 = EnchantmentHelper.getModifierForCreature(this.getHeldItem(),
    // EnumCreatureAttribute.UNDEFINED);
    // }
    //
    // i = i + EnchantmentHelper.getKnockbackModifier(this);
    //
    // if (this.isSprinting()) {
    // ++i;
    // }
    //
    // if (f > 0.0F || f1 > 0.0F) {
    // boolean flag = this.fallDistance > 0.0F && !this.onGround && !this.isOnLadder() &&
    // !this.isInWater() && !this.isPotionActive(
    // Potion.blindness) && this.ridingEntity == null && targetEntity instanceof EntityLivingBase;
    //
    // if (flag && f > 0.0F) {
    // f *= 1.5F;
    // }
    //
    // f = f + f1;
    // boolean flag1 = false;
    // int j = EnchantmentHelper.getFireAspectModifier(this);
    //
    // if (targetEntity instanceof EntityLivingBase && j > 0 && !targetEntity.isBurning()) {
    // flag1 = true;
    // targetEntity.setFire(1);
    // }
    //
    // double d0 = targetEntity.motionX;
    // Logger.logChatMessage(d0 + "");
    // double d1 = targetEntity.motionY;
    // double d2 = targetEntity.motionZ;
    // boolean flag2 =
    // targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(Minecraft.getMinecraft().thePlayer), f);
    //
    // if (flag2) {
    // if (i > 0) {
    // targetEntity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F)
    // * (float) i * 0.5F), 0.1D,
    // (double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F));
    //
    // KeepSprint keepsprint = (KeepSprint) ModuleManager.getModule(KeepSprint.class);
    // if (ClientConfig.isEnabled() && keepsprint.isEnabled()) {
    // keepsprint.onKeepSprint();
    // } else {
    // // DEFAULT
    // this.motionX *= 0.6D;
    // this.motionZ *= 0.6D;
    // this.setSprinting(false);
    // }
    // }
    //
    // if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
    // ((EntityPlayerMP) targetEntity).playerNetServerHandler.sendPacket(new
    // S12PacketEntityVelocity(targetEntity));
    // targetEntity.velocityChanged = false;
    // targetEntity.motionX = d0;
    // targetEntity.motionY = d1;
    // targetEntity.motionZ = d2;
    // }
    //
    // if (flag) {
    // Minecraft.getMinecraft().thePlayer.onCriticalHit(targetEntity);
    // }
    //
    // if (f1 > 0.0F) {
    // Minecraft.getMinecraft().thePlayer.onEnchantmentCritical(targetEntity);
    // }
    //
    // if (f >= 18.0F) {
    // Minecraft.getMinecraft().thePlayer.triggerAchievement(AchievementList.overkill);
    // }
    //
    // this.setLastAttacker(targetEntity);
    //
    // if (targetEntity instanceof EntityLivingBase) {
    // EnchantmentHelper.applyThornEnchantments((EntityLivingBase) targetEntity, this);
    // }
    //
    // EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
    // ItemStack itemstack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
    // Entity entity = targetEntity;
    //
    // if (targetEntity instanceof EntityDragonPart) {
    // IEntityMultiPart ientitymultipart = ((EntityDragonPart) targetEntity).entityDragonObj;
    //
    // if (ientitymultipart instanceof EntityLivingBase) {
    // entity = (EntityLivingBase) ientitymultipart;
    // }
    // }
    //
    // if (itemstack != null && entity instanceof EntityLivingBase) {
    // itemstack.hitEntity((EntityLivingBase) entity, Minecraft.getMinecraft().thePlayer);
    //
    // if (itemstack.stackSize <= 0) {
    // Minecraft.getMinecraft().thePlayer.destroyCurrentEquippedItem();
    // }
    // }
    //
    // if (targetEntity instanceof EntityLivingBase) {
    // Minecraft.getMinecraft().thePlayer.addStat(StatList.damageDealtStat, Math.round(f * 10.0F));
    //
    // if (j > 0) {
    // targetEntity.setFire(j * 4);
    // }
    // }
    //
    // Minecraft.getMinecraft().thePlayer.addExhaustion(0.3F);
    // } else if (flag1) {
    // targetEntity.extinguish();
    // }
    // }
    // }
    // }
    // }

}
