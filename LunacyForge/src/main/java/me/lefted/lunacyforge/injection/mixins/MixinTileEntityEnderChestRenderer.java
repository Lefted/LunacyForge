package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.lefted.lunacyforge.modules.ChestESP;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.OutlineUtils;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(TileEntityEnderChestRenderer.class)
public abstract class MixinTileEntityEnderChestRenderer extends TileEntitySpecialRenderer<TileEntityEnderChest> {

    // ATTRIBUTES
    @Shadow
    private ModelChest field_147521_c = new ModelChest();

    // METHODS
    @Inject(method = "renderTileEntityAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;disableRescaleNormal()V", shift = At.Shift.BEFORE, args = {
	    "log=false" }))
    public void renderTileEntityAtInject(TileEntityEnderChest te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {

	if (ModuleManager.getModule(ChestESP.class).isEnabled()) {
	    field_147521_c.renderAll();
	    OutlineUtils.renderOneChest();
	    field_147521_c.renderAll();
	    OutlineUtils.renderTwo();
	    field_147521_c.renderAll();
	    OutlineUtils.renderThree();
	    OutlineUtils.renderFourChest(true);
	    field_147521_c.renderAll();
	    OutlineUtils.renderFive();
	}
    }

}
