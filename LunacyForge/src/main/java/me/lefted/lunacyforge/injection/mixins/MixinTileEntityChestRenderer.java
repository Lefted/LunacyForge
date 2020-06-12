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
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(TileEntityChestRenderer.class)
public abstract class MixinTileEntityChestRenderer extends TileEntitySpecialRenderer<TileEntityChest> {

    // ATTRIBUTES
    @Shadow
    private ModelChest simpleChest = new ModelChest();

    @Shadow
    private ModelChest largeChest = new ModelLargeChest();

    // METHODS
    @Inject(method = "renderTileEntityAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;disableRescaleNormal()V", shift = At.Shift.BEFORE, args = {
	    "log=false" }))
    public void renderTileEntityAtInject(TileEntityChest te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {

	ModelChest modelchest = (te.adjacentChestXPos == null && te.adjacentChestZPos == null) ? simpleChest : largeChest;

	final ChestESP esp = (ChestESP) ModuleManager.getModule(ChestESP.class);
	if (esp.isEnabled()) {
	    modelchest.renderAll();
	    OutlineUtils.renderOne(esp.lineWidth.getObject().floatValue());
	    modelchest.renderAll();
	    OutlineUtils.renderTwo();
	    modelchest.renderAll();
	    OutlineUtils.renderThree();
	    OutlineUtils.renderFour(esp.chestColor.getObject());
	    modelchest.renderAll();
	    OutlineUtils.renderFive();
	}
    }
}
