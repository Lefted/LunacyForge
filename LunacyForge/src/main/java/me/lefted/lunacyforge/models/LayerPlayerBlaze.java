package me.lefted.lunacyforge.models;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.modules.Cosmetics;
import me.lefted.lunacyforge.modules.ModuleManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerPlayerBlaze implements LayerRenderer<AbstractClientPlayer> {

    // CONSTANTS
    private static final ResourceLocation BLAZE_TEXTURE = new ResourceLocation("textures/entity/blaze.png");

    // ATTRIBUTES
    private final RenderPlayer renderPlayer;
    private ModelBlazeSticks blazeModel;

    // CONSTRUCTOR
    public LayerPlayerBlaze(RenderPlayer renderPlayer) {
	this.renderPlayer = renderPlayer;
	this.blazeModel = new ModelBlazeSticks();
    }

    // METHODS
    @Override
    public void doRenderLayer(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks,
	final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {

	final Cosmetics cosmetics = (Cosmetics) ModuleManager.getModule(Cosmetics.class);
	if (!cosmetics.isEnabled() || (cosmetics.isEnabled() && !cosmetics.useBlaze.getObject().booleanValue()) || !player.hasPlayerInfo() || player
	    .isInvisible()) {
	    return;
	}

	GlStateManager.pushMatrix();
	this.renderPlayer.bindTexture(BLAZE_TEXTURE);
	this.blazeModel.setModelAttributes(this.renderPlayer.getMainModel());
	GL11.glColor3f(1.0f, 1.0f, 1.0f);
	this.blazeModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
	GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
	return false;
    }

}
