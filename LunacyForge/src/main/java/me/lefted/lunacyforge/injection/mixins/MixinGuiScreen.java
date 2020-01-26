package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.lefted.lunacyforge.LunacyForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Also see: CommandManager.java
 */
@SideOnly(Side.CLIENT)
@Mixin(GuiScreen.class)
public class MixinGuiScreen {

    @Shadow
    public Minecraft mc;

    /* dispatch commands*/
    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void chatMessage(String msg, boolean addToChat, CallbackInfo callbackInfo) {
	if (msg.startsWith("\\") || msg.startsWith(".")) {
	    LunacyForge.instance.commandManager.callCommand(msg);
	    mc.ingameGUI.getChatGUI().addToSentMessages(msg);
	    callbackInfo.cancel();
	}
    }
}
