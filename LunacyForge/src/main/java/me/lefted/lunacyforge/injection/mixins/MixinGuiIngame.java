/* LiquidBounce Hacked Client A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/ */
package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkmagician6.eventapi.EventManager;

import me.lefted.lunacyforge.events.Render2DEvent;
import me.lefted.lunacyforge.utils.ClassUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/* Also see: Render2DEvent */
@SideOnly(Side.CLIENT)
@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {

    // HOTBAR
    // @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    // private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
    // // final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
    //
    // if (Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer && false/* && hud.getState() && hud.blackHotbarValue.get()*/) {
    // EntityPlayer entityPlayer = (EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity();
    //
    // int middleScreen = sr.getScaledWidth() / 2;
    //
    // GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    // GuiIngame.drawRect(middleScreen - 91, sr.getScaledHeight() - 24, middleScreen + 90, sr.getScaledHeight(), Integer.MIN_VALUE);
    // GuiIngame.drawRect(middleScreen - 91 - 1 + entityPlayer.inventory.currentItem * 20 + 1, sr.getScaledHeight() - 24, middleScreen - 91 - 1
    // + entityPlayer.inventory.currentItem * 20 + 22, sr.getScaledHeight() - 22 - 1 + 24, Integer.MAX_VALUE);
    //
    // GlStateManager.enableRescaleNormal();
    // GlStateManager.enableBlend();
    // GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    // RenderHelper.enableGUIStandardItemLighting();
    //
    // for (int j = 0; j < 9; ++j) {
    // int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
    // int l = sr.getScaledHeight() - 16 - 3;
    // this.renderHotbarItem(j, k, l, partialTicks, entityPlayer);
    // }
    //
    // RenderHelper.disableStandardItemLighting();
    // GlStateManager.disableRescaleNormal();
    // GlStateManager.disableBlend();
    //
    // EventManager.call(new Render2DEvent(partialTicks));
    // // LiquidBounce.eventManager.callEvent(new Render2DEvent(partialTicks));
    // // AWTFontRenderer.Companion.garbageCollectionTick();
    // callbackInfo.cancel();
    // }
    // }

    @Inject(method = "renderTooltip", at = @At("RETURN"))
    private void renderTooltipPost(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
	if (!ClassUtils.hasClass("net.labymod.api.LabyModAPI")) {
	    EventManager.call(new Render2DEvent(partialTicks));
	    // LiquidBounce.eventManager.callEvent(new Render2DEvent(partialTicks));
	    // AWTFontRenderer.Companion.garbageCollectionTick();
	}
    }

    // HOTBAR
    // @Shadow
    // protected abstract void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player);
}
