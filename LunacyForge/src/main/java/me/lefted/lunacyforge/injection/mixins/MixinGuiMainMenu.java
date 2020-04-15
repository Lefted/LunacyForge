package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.lefted.lunacyforge.testgui.Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen {

    @Inject(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, opcode = Opcodes.PUTFIELD, args = "log=false"))
    private void addButton(CallbackInfo ci) {
	this.buttonList.add(new GuiButton(100, this.width / 2 - 100, this.height / 2 - 100, "Screen"));
    }
    
    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void pressed(GuiButton button, CallbackInfo ci) {
	if (button.id == 100) {
	    Minecraft.getMinecraft().displayGuiScreen(new Screen());
	}
    }
}
