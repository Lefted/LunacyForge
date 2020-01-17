package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.lefted.lunacyforge.modules.FastBridge;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Also see: FastBridge.java
 */
@SideOnly(Side.CLIENT)
@Mixin(ItemStack.class)
public class MixinItemStack {

    @Shadow
    public int stackSize;
    
    @Inject(method = "onItemUse", at = @At("HEAD"))
    public void onItemUse(CallbackInfoReturnable<Boolean> callback) {
	if (this.stackSize == 1) {
	    ((FastBridge) ModuleManager.getModule(FastBridge.class)).allBlocksUsed();
	}
    }
    
}
