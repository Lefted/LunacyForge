package me.lefted.lunacyforge.injection.mixins;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkmagician6.eventapi.EventManager;

import me.lefted.lunacyforge.LunacyForge;
import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.events.AimAssistTimerEvent;
import me.lefted.lunacyforge.events.KeyPressEvent;
import me.lefted.lunacyforge.events.TickEvent;
import me.lefted.lunacyforge.implementations.ILunacyTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mixin(Minecraft.class)
@SideOnly(Side.CLIENT)
public abstract class MixinMinecraft extends Object implements ILunacyTimer {

    @Shadow
    public GuiScreen currentScreen;

    private Timer aimAssistTimer = new Timer(20.0F);

    @Override
    public net.minecraft.util.Timer getAimAssistTimer() {
	return this.aimAssistTimer;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initMinecraft(CallbackInfo callbackInfo) {
	new LunacyForge();
    }

    @Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
    private void startClient(CallbackInfo callbackInfo) {
	LunacyForge.INSTANCE.startClient();
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void stopClient(CallbackInfo callbackInfo) {
	LunacyForge.INSTANCE.stopClient();
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void runTick(CallbackInfo callbackInfo) {
	if (ClientConfig.isEnabled()) {
	    final TickEvent event = new TickEvent();
	    EventManager.call(event);
	}
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
    private void keyPress(CallbackInfo callbackInfo) {
	if (ClientConfig.isEnabled()) {
	    if (Keyboard.getEventKeyState() && currentScreen == null) {
		final KeyPressEvent event = new KeyPressEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey());
		EventManager.call(event);
	    }
	}
    }

    @Inject(method = "runGameLoop", at = @At(value = "FIELD", target = "Lnet/minecraft/util/Timer;renderPartialTicks:F", ordinal = 1, shift = At.Shift.AFTER))
    private void onGameLoop(CallbackInfo callbackInfo) {
	float f1 = aimAssistTimer.renderPartialTicks;
	aimAssistTimer.updateTimer();
	aimAssistTimer.renderPartialTicks = f1;
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Timer;updateTimer()V", ordinal = 1, shift = At.Shift.AFTER))
    private void onGameLoop2(CallbackInfo callbackInfo) {
	aimAssistTimer.updateTimer();
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", args = {
	    "ldc=tick" }))
    private void onGameLoop3(CallbackInfo callbackInfo) {
	if (ClientConfig.isEnabled()) {
	    // final EventGameLoop event = new EventGameLoop();
	    // EventManager.call(event);

	    for (int j = 0; j < aimAssistTimer.elapsedTicks; ++j) {
		final AimAssistTimerEvent event2 = new AimAssistTimerEvent();
		EventManager.call(event2);
	    }
	    // for (int j = 0; j < this.bowTimer.elapsedTicks; ++j) {
	    // final EventBow event3 = new EventBow();
	    // EventManager.call(event3);
	    // }
	}
    }
}
