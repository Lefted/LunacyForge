package me.lefted.lunacyforge.injection.mixins;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.lefted.lunacyforge.models.LayerPlayerBlaze;
import me.lefted.lunacyforge.models.LayerPlayerCharge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity<AbstractClientPlayer> {

    // CONSTRUCTOR
    public MixinRenderPlayer(RenderManager p_i46156_1_, ModelBase p_i46156_2_, float p_i46156_3_) {
	super(p_i46156_1_, p_i46156_2_, p_i46156_3_);
    }

    // METHODS
    @Inject(method = "<init>", at = @At("RETURN"))
    public void addLayers(CallbackInfo callbackInfo) {
	this.addLayer(new LayerPlayerCharge((RenderPlayer) (Object) this));
	this.addLayer(new LayerPlayerBlaze((RenderPlayer) (Object) this));
	
	// TODO FIXME wont affect alex skins
    }

}
