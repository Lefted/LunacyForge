package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.lefted.lunacyforge.modules.FastBridge;
import me.lefted.lunacyforge.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/* Also see: FastBridge.java */
@SideOnly(Side.CLIENT)
@Mixin(MovementInputFromOptions.class)
public class MixinMovementInputFromOptions extends MovementInput {

    @Shadow
    private GameSettings gameSettings;

    @Redirect(method = "updatePlayerMoveState", at = @At(value = "FIELD", target = "Lnet/minecraft/util/MovementInputFromOptions;jump:Z", ordinal = 0, opcode = Opcodes.PUTFIELD, args = "log=false"))
    private void setFieldValue(MovementInputFromOptions owner, boolean value) {
	if (ModuleManager.getModule(FastBridge.class).isEnabled()) {
	    this.jump = (this.gameSettings.keyBindJump.isKeyDown() && ((FastBridge) ModuleManager.getModule(FastBridge.class)).safeSneakDelay == 0);
	} /*else if (ModuleManager.getModule(Strafe.class).isEnabled()) {
	    this.jump = (this.gameSettings.keyBindJump.isKeyDown() && Minecraft.getMinecraft().thePlayer.isCollidedVertically || !(Minecraft
	  .getMinecraft().thePlayer.isAirBorne || (Minecraft.getMinecraft().thePlayer.isInWater())));
	  } */else {
	    this.jump = this.gameSettings.keyBindJump.isKeyDown();
	}
    }

    @Redirect(method = "updatePlayerMoveState", at = @At(value = "FIELD", target = "Lnet/minecraft/util/MovementInputFromOptions;sneak:Z", ordinal = 0, opcode = Opcodes.PUTFIELD, args = "log=false"))
    private void setFieldValue2(MovementInputFromOptions owner, boolean value) {
	this.sneak = ModuleManager.getModule(FastBridge.class).isEnabled() ? ((FastBridge) ModuleManager.getModule(FastBridge.class)).isSneaking()
	    : this.gameSettings.keyBindSneak.isKeyDown();
    }
}
