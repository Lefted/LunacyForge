package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.lefted.lunacyforge.modules.Animations;
import me.lefted.lunacyforge.modules.ModuleManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    // ATTRIBUTES
    @Shadow
    private ItemStack itemToRender;

    // METHODS
    @Overwrite
    private void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer) {

	GlStateManager.rotate(-18.0F, 0.0F, 0.0F, 1.0F);
	GlStateManager.rotate(-12.0F, 0.0F, 1.0F, 0.0F);
	GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
	GlStateManager.translate(-0.9F, 0.2F, 0.0F);
	float f = (float) this.itemToRender.getMaxItemUseDuration() - ((float) clientPlayer.getItemInUseCount() - partialTicks + 1.0F);
	float f1 = f / 20.0F;
	f1 = (f1 * f1 + f1 * 2.0F) / 3.0F;

	if (f1 > 1.0F) {
	    f1 = 1.0F;
	}

	final Animations animations = (Animations) ModuleManager.getModule(Animations.class);
	if (f1 > 0.1F && (!animations.isEnabled() || (animations.isEnabled() && !animations.useStaticBow.getObject().booleanValue()))) {
	    float f2 = MathHelper.sin((f - 0.1F) * 1.3F);
	    float f3 = f1 - 0.1F;
	    float f4 = f2 * f3;
	    GlStateManager.translate(f4 * 0.0F, f4 * 0.01F, f4 * 0.0F);
	}

	GlStateManager.translate(f1 * 0.0F, f1 * 0.0F, f1 * 0.1F);
	GlStateManager.scale(1.0F, 1.0F, 1.0F + f1 * 0.2F);
    }

    @Redirect(method = "renderItemInFirstPerson", at = @At(target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getSwingProgress(F)F", ordinal = 0, value = "INVOKE", args = "log=false"))
    public float getSwingProgressProxy(AbstractClientPlayer owner, float partialTicks) {
	final Animations animatins = (Animations) ModuleManager.getModule(Animations.class);
	if (animatins.isEnabled() && animatins.useNoSwing.getObject().booleanValue()) {
	    return 0F;
	} else {
	    return owner.getSwingProgress(partialTicks);
	}
    }
    

}
