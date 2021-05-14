package me.lefted.lunacyforge.models;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.modules.Cosmetics;
import me.lefted.lunacyforge.modules.ModuleManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerPlayerCharge implements LayerRenderer<AbstractClientPlayer> {

    // CONSTANTS
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    // ATTRIBUTES
    private final RenderPlayer playerRenderer;
    private ModelPlayer playerModel;

    // CONSTRUCTOR
    public LayerPlayerCharge(RenderPlayer renderPlayer) {
	this.playerRenderer = renderPlayer;
	this.playerModel = renderPlayer.getMainModel();
    }

    // METHODS
    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_,
	float p_177141_6_, float p_177141_7_, float scale) {
	final Cosmetics cosmetics = (Cosmetics) ModuleManager.getModule(Cosmetics.class);
	if (!cosmetics.isEnabled() || !entitylivingbaseIn.hasPlayerInfo() || (cosmetics.isEnabled() && !cosmetics.useCharge.getObject().booleanValue())) {
	    return;
	}

	boolean flag = entitylivingbaseIn.isInvisible();
	GlStateManager.depthMask(!flag);
	this.playerModel = this.playerRenderer.getMainModel();
	this.playerRenderer.bindTexture(LIGHTNING_TEXTURE);
	GlStateManager.matrixMode(5890);
	GlStateManager.loadIdentity();
	float f = (float) entitylivingbaseIn.ticksExisted + partialTicks;

	// SPEED
	// float velX = 0F;
	// float velY = -0.01F;
	float velX = cosmetics.chargeVelX.getObject().intValue() / 500F;
	float velY = cosmetics.chargeVelY.getObject().intValue() / 500F;

	GlStateManager.translate(f * velX, f * velY, 0.0F);
	GlStateManager.matrixMode(5888);
	GlStateManager.enableBlend();

	// COLOR
	float[] color = cosmetics.chargeColor.getObject();

	GL11.glColor4f(color[0], color[1], color[2], color[3]);
	GlStateManager.disableLighting();
	GlStateManager.blendFunc(1, 1);
	this.playerModel.setModelAttributes(this.playerRenderer.getMainModel());
	this.playerModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
	GlStateManager.matrixMode(5890);
	GlStateManager.loadIdentity();
	GlStateManager.matrixMode(5888);
	GlStateManager.enableLighting();
	GlStateManager.disableBlend();
	GlStateManager.depthMask(flag);
    }

    public boolean shouldCombineTextures() {
	return false;
    }

}
