package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.lefted.lunacyforge.implementations.IRenderPlayerGetter;
import me.lefted.lunacyforge.modules.Animations;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(ModelBiped.class)
public abstract class MixinModelBiped extends ModelBase {

    @SuppressWarnings("unlikely-arg-type")
    @Inject(method = "setRotationAngles", at = @At("HEAD"))
    public void setRotationAnglesProxy(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor,
	Entity entityIn, CallbackInfo ci) {
	final Animations animations = (Animations) ModuleManager.getModule(Animations.class);
	if (animations.isEnabled() && animations.useNoSwing.getObject().booleanValue()) {
	    final RenderPlayer renderPlayer = ((IRenderPlayerGetter) Minecraft.getMinecraft().getRenderManager()).getPlayerRenderer();

	    if (renderPlayer.getMainModel().equals(this)) {
		super.swingProgress = 0F;
	    }
	}
    }
}
