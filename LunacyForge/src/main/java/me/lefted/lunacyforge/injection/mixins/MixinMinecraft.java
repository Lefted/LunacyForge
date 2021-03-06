package me.lefted.lunacyforge.injection.mixins;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkmagician6.eventapi.EventManager;
import com.mojang.authlib.minecraft.MinecraftSessionService;

import me.lefted.lunacyforge.LunacyForge;
import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.events.AimAssistTimerEvent;
import me.lefted.lunacyforge.events.BowAimbotTimerEvent;
import me.lefted.lunacyforge.events.KeyPressEvent;
import me.lefted.lunacyforge.events.TickEvent;
import me.lefted.lunacyforge.implementations.ISession;
import me.lefted.lunacyforge.implementations.ITimer;
import me.lefted.lunacyforge.modules.ClickGui;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.modules.NoLeftClickDelay;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/* Also see: LunacyForge.java, AimAssisst.java, */
@Mixin(Minecraft.class)
@SideOnly(Side.CLIENT)
public abstract class MixinMinecraft extends Object implements ITimer, ISession {
    
    @Shadow
    public GuiScreen currentScreen;

    @Shadow
    private int rightClickDelayTimer;

    @Shadow
    private Timer timer;

    private Timer aimAssistTimer = new Timer(20.0F);

    private Timer bowAimbotTimer = new Timer(20.0F);

    @Shadow
    private int leftClickCounter;

    @Shadow
    public EntityPlayerSP thePlayer;

    @Shadow
    public MovingObjectPosition objectMouseOver;

    @Shadow
    public PlayerControllerMP playerController;

    @Shadow
    public WorldClient theWorld;

    @Shadow
    private Session session;

    @Override
    public void setSession(Session session) {
	this.session = session;
    }

    @Override
    public Timer getMinecraftTimer() {
	return this.timer;
    }

    @Override
    public net.minecraft.util.Timer getAimAssistTimer() {
	return this.aimAssistTimer;
    }

    @Override
    public Timer getBowAimbotTimer() {
	return this.bowAimbotTimer;
    }

    @Override
    public int getRightClickDelayTimer() {
	return this.rightClickDelayTimer;
    }

    @Override
    public void setRightClickDelayTimer(int rightClickDelayTimer) {
	this.rightClickDelayTimer = rightClickDelayTimer;
    }

    /* create LunacyForge instance*/
    @Inject(method = "<init>", at = @At("RETURN"))
    private void initMinecraft(CallbackInfo callbackInfo) {
	new LunacyForge();
    }

    /* start LunacyForge*/
    @Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
    private void startClient(CallbackInfo callbackInfo) {
	LunacyForge.instance.startClient();
    }

    /* stop LunacyForge*/
    @Inject(method = "shutdown", at = @At("HEAD"))
    private void stopClient(CallbackInfo callbackInfo) {
	LunacyForge.instance.stopClient();
    }

    /* dispatch TickEvent*/
    @Inject(method = "runTick", at = @At("HEAD"))
    private void runTick(CallbackInfo callbackInfo) {
	if (ClientConfig.isEnabled()) {
	    final TickEvent event = new TickEvent();
	    EventManager.call(event);
	}
    }

    /* dispatch KeyPressEvent*/
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
    private void keyPress(CallbackInfo callbackInfo) {
	if (Keyboard.getEventKeyState() && currentScreen == null) {
	    final int key = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
	    if (ClientConfig.isEnabled() || key == ModuleManager.getModule(ClickGui.class).getKeycode()) {
		final KeyPressEvent event = new KeyPressEvent(key);
		EventManager.call(event);
	    }
	}
    }

    /* keep supersecretsettings active when toggling perspective*/
    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;loadEntityShader(Lnet/minecraft/entity/Entity;)V"))
    public void loadEntityShaderProxy(EntityRenderer entityRenderer, Entity renderViewEntity) {
    }

    /* update custom timers*/
    @Inject(method = "runGameLoop", at = @At(value = "FIELD", target = "Lnet/minecraft/util/Timer;renderPartialTicks:F", ordinal = 1, shift = At.Shift.AFTER))
    private void onGameLoop(CallbackInfo callbackInfo) {
	float f1 = aimAssistTimer.renderPartialTicks;
	aimAssistTimer.updateTimer();
	aimAssistTimer.renderPartialTicks = f1;
	final float f3 = this.bowAimbotTimer.renderPartialTicks;
	this.bowAimbotTimer.updateTimer();
	this.bowAimbotTimer.renderPartialTicks = f3;
    }

    /* update custom timers*/
    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Timer;updateTimer()V", ordinal = 1, shift = At.Shift.AFTER))
    private void onGameLoop2(CallbackInfo callbackInfo) {
	aimAssistTimer.updateTimer();
	bowAimbotTimer.updateTimer();
    }

    /* dispatch AimAssistTimerEvent*/
    @Inject(method = "runGameLoop", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", args = {
	    "ldc=tick" }))
    private void onGameLoop3(CallbackInfo callbackInfo) {
	if (ClientConfig.isEnabled()) {
	    for (int j = 0; j < aimAssistTimer.elapsedTicks; ++j) {
		final AimAssistTimerEvent event2 = new AimAssistTimerEvent();
		EventManager.call(event2);
	    }

	    for (int j = 0; j < this.bowAimbotTimer.elapsedTicks; ++j) {
		final BowAimbotTimerEvent event3 = new BowAimbotTimerEvent();
		EventManager.call(event3);
	    }
	}
    }

    // NoLeftClickDelay
    @Overwrite
    private void clickMouse() {
	if (this.leftClickCounter <= 0) {
	    this.thePlayer.swingItem();

	    if (this.objectMouseOver == null) {
		LogManager.getLogger().error("Null returned as \'hitResult\', this shouldn\'t happen!");

		if (this.playerController.isNotCreative()) {
		    this.leftClickCounter = 10;
		}
	    } else {
		switch (this.objectMouseOver.typeOfHit) {
		case ENTITY:
		    this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
		    break;

		case BLOCK:
		    BlockPos blockpos = this.objectMouseOver.getBlockPos();

		    if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
			this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
			break;
		    }

		case MISS:
		    // Credits Tenebrous, Lunar Client, 16_
		    if (ModuleManager.getModule(NoLeftClickDelay.class).isEnabled()) {
			break;
		    }
		default:
		    if (this.playerController.isNotCreative()) {
			this.leftClickCounter = 10;
		    }
		}
	    }
	}
    }
}
