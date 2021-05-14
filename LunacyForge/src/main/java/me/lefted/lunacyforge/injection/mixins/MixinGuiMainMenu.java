package me.lefted.lunacyforge.injection.mixins;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.lefted.lunacyforge.command.commands.LoginCommand;
import me.lefted.lunacyforge.thealtening.GuiTheAltening;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.hemdgang.autoreward.TestMain;

@SideOnly(Side.CLIENT)
@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    // ATTRIBUTES
    public GuiButton btnFastLogin;
    public GuiButton btnLabyConnect;

    // METHODS
    @Inject(method = "initGui", at = @At("RETURN"))
    public void addAlteningButton(CallbackInfo ci) {
	addButton();
    }

    private void addButton() {
	buttonList.add(btnFastLogin = new GuiButton(420, 4, 4, 80, 20, "Login"));
	buttonList.add(btnLabyConnect = new GuiButton(421, 4, 28, 80, 20, "LabyConnect"));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
	delegate(button);
    }

    private void delegate(GuiButton button) {
	if (button.id == btnFastLogin.id) {
	    // mc.displayGuiScreen(new GuiTheAltening((GuiScreen) (Object) this));
	    LoginCommand.logIn();
	} else if (button.id == btnLabyConnect.id) {
	    new Thread(() -> {
		try {
		    TestMain.entry(null);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }).start();
	}
    }
}
