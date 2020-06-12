package me.lefted.lunacyforge.injection.mixins;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import me.lefted.lunacyforge.modules.ArrowESP;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.OutlineUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(RenderArrow.class)
public abstract class MixinRenderArrow extends Render<Entity> {

    // CONSTRUCTOR
    protected MixinRenderArrow(RenderManager p_i46179_1_) {
	super(p_i46179_1_);
    }

    // METHODS
    @Overwrite
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
	final ArrowESP esp = (ArrowESP) ModuleManager.getModule(ArrowESP.class);
	
	if (esp.isEnabled()) {
		renderModel((EntityArrow) entity, x, y, z, entityYaw, partialTicks);
		OutlineUtils.renderOne(esp.lineWidth.getObject().floatValue());
		renderModel((EntityArrow) entity, x, y, z, entityYaw, partialTicks);
		OutlineUtils.renderTwo();
		renderModel((EntityArrow) entity, x, y, z, entityYaw, partialTicks);
		OutlineUtils.renderThree();
		OutlineUtils.renderFour(esp.color.getObject());
		renderModel((EntityArrow) entity, x, y, z, entityYaw, partialTicks);
		OutlineUtils.renderFive();
	} else {
	    renderModel((EntityArrow) entity, x, y, z, entityYaw, partialTicks);
	}
    }

    private void renderModel(EntityArrow entity, double x, double y, double z, float entityYaw, float partialTicks) {
	this.bindEntityTexture(entity);
	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	GlStateManager.pushMatrix();
	GlStateManager.translate((float) x, (float) y, (float) z);
	GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
	GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
	Tessellator tessellator = Tessellator.getInstance();
	WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	int i = 0;
	float f = 0.0F;
	float f1 = 0.5F;
	float f2 = (float) (0 + i * 10) / 32.0F;
	float f3 = (float) (5 + i * 10) / 32.0F;
	float f4 = 0.0F;
	float f5 = 0.15625F;
	float f6 = (float) (5 + i * 10) / 32.0F;
	float f7 = (float) (10 + i * 10) / 32.0F;
	float f8 = 0.05625F;
	GlStateManager.enableRescaleNormal();
	float f9 = (float) entity.arrowShake - partialTicks;

	if (f9 > 0.0F) {
	    float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
	    GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
	}

	GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
	GlStateManager.scale(f8, f8, f8);
	GlStateManager.translate(-4.0F, 0.0F, 0.0F);
	GL11.glNormal3f(f8, 0.0F, 0.0F);
	worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
	worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex((double) f4, (double) f6).endVertex();
	worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex((double) f5, (double) f6).endVertex();
	worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex((double) f5, (double) f7).endVertex();
	worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex((double) f4, (double) f7).endVertex();
	tessellator.draw();
	GL11.glNormal3f(-f8, 0.0F, 0.0F);
	worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
	worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex((double) f4, (double) f6).endVertex();
	worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex((double) f5, (double) f6).endVertex();
	worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex((double) f5, (double) f7).endVertex();
	worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex((double) f4, (double) f7).endVertex();
	tessellator.draw();

	for (int j = 0; j < 4; ++j) {
	    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
	    GL11.glNormal3f(0.0F, 0.0F, f8);
	    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
	    worldrenderer.pos(-8.0D, -2.0D, 0.0D).tex((double) f, (double) f2).endVertex();
	    worldrenderer.pos(8.0D, -2.0D, 0.0D).tex((double) f1, (double) f2).endVertex();
	    worldrenderer.pos(8.0D, 2.0D, 0.0D).tex((double) f1, (double) f3).endVertex();
	    worldrenderer.pos(-8.0D, 2.0D, 0.0D).tex((double) f, (double) f3).endVertex();
	    tessellator.draw();
	}

	GlStateManager.disableRescaleNormal();
	GlStateManager.popMatrix();
	super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

}
