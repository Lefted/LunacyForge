package me.lefted.lunacyforge.modules;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import me.lefted.lunacyforge.guiscreenold.interpreter.ModuleInterpreter;
import me.lefted.lunacyforge.utils.Logger;
import me.lefted.lunacyforge.valuesystem.Value;

/*
 * Also see: MixinEntityPlayer.java
 */
@ModuleInterpreter(description = "Prevents you from slowing down when hitting an enemy")
public class KeepSprint extends Module{

    private Value<Boolean> firstHitBypassesValue = new Value<Boolean>("firstHitBypasses", Boolean.valueOf(true));
    private Value<Integer> randomizerValue = new Value<Integer>("randomizer", Integer.valueOf(30));
    private Value<Integer> firstHitDelayValue = new Value<Integer>("firstHitDelay", Integer.valueOf(2));
    
    private static boolean timerRunning = false;
    private static int secondsSinceLastHit;
    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    
    public KeepSprint() {
	super("KeepSprint", Category.MOVEMENT);
    }

    public void onKeepSprint() {
	Logger.logChatMessage("keepsprint");
    }
    
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

}
