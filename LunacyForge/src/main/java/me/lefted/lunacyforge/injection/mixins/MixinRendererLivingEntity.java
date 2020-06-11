package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.lefted.lunacyforge.modules.KeepSprint;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.modules.OutlineESP;
import me.lefted.lunacyforge.utils.OutlineUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T> {

    // CONSTRUCTOR
    protected MixinRendererLivingEntity(RenderManager p_i46179_1_) {
	super(p_i46179_1_);
    }

    // PROXIES
    /* renders HEXCEPTION outline esp*/
    @Redirect(method = "doRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;renderModel(Lnet/minecraft/entity/EntityLivingBase;FFFFFF)V", ordinal = 1, args = "log=false"))
    private void renderModelProxy(RendererLivingEntity<T> owner, T entity, float f6, float f5, float f7, float f2, float f8, float scaleFactor) {

	final OutlineESP esp = (OutlineESP) ModuleManager.getModule(OutlineESP.class);

	if (esp.isEnabled() && entity instanceof EntityLivingBase) {
	    final EntityLivingBase entityLB = (EntityLivingBase) entity;

	    if (esp.hexceptionShouldRenderEntity(entity)) {
		GlStateManager.depthMask(true);
		this.renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);
		OutlineUtils.renderOne(entityLB);
		this.renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);
		OutlineUtils.renderTwo();
		this.renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);
		OutlineUtils.renderThree();
		OutlineUtils.renderFour(entity);

		this.renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);
		OutlineUtils.renderFive();
	    } else {
		this.renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);
	    }
	} else {
	    this.renderModel(entity, f6, f5, f7, f2, f8, 0.0625F);
	}

    }

    @Shadow
    protected abstract void renderModel(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_,
	float p_77036_7_);
}
