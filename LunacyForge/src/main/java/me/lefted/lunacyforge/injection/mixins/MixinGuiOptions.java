package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.lefted.lunacyforge.clickgui.elements.GuiSecurity;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/* - Adds Shader reset button next to supersecrets */
@SideOnly(Side.CLIENT)
@Mixin(GuiOptions.class)
public class MixinGuiOptions extends GuiScreen {

    // ATTRIBUTES
    private GuiButton btnResetShader;

    // METHODS
    @Inject(method = "initGui", at = @At("RETURN"))
    private void addResetShaderButton(CallbackInfo ci) {
	btnResetShader = new GuiButton(8675310, this.width / 2 + 5 + 150 + 2, this.height / 6 + 48 - 6, 40, 20, "Reset");
	this.buttonList.add(btnResetShader);
	btnResetShader.visible = mc.entityRenderer.getShaderGroup() != null;
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void pressed(GuiButton button, CallbackInfo ci) {
	if (button.id == 8675310) {
	    if (mc.entityRenderer.getShaderGroup() != null) {
		mc.entityRenderer.getShaderGroup().deleteShaderGroup();
		mc.entityRenderer.stopUseShader();// = null;
		GuiSecurity.setTripping(false);
		btnResetShader.visible = false;
	    }
	}
	if (button.id == 8675309) {
	    btnResetShader.visible = true;
	}
    }

    public GuiButton getBtnResetShader() {
	return btnResetShader;
    }

    public void setBtnResetShader(GuiButton btnResetShader) {
	this.btnResetShader = btnResetShader;
    }
}
