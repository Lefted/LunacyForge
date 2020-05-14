package me.lefted.lunacyforge.modules;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import me.lefted.lunacyforge.clickgui.annotations.CheckboxInfo;
import me.lefted.lunacyforge.clickgui.annotations.ContainerInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider.NumberType;
import me.lefted.lunacyforge.utils.Logger;
import me.lefted.lunacyforge.valuesystem.Value;

/* Also see: MixinEntityPlayer.java */
@ModuleInfo(description = "Prevents you from slowing down when hitting an enemy")
public class KeepSprint extends Module {

    // VALUES
    @ContainerInfo(hoverText = "If enabled, it will seem more legit")
    @SliderInfo(description = "Chance KeepSprint fails", min = 0, max = 100, step = 5D, numberType = NumberType.PERCENT)
    private Value<Integer> randomizerValue = new Value<Integer>("randomizer", Integer.valueOf(30));

    @ContainerInfo(hoverText = "If you have a chance that KeepSprint will fail set\nNomatter the change on the firsthit KeepSprint will work")
    @CheckboxInfo(description = "KeepSprint always works on first hit")
    private Value<Boolean> firstHitBypassesValue = new Value<Boolean>("firstHitBypasses", Boolean.valueOf(true));

    @ContainerInfo(hoverText = "The seconds that need to pass without hitting someone in order for the next hit to count as the firsthit")
    @SliderInfo(min = 1, max = 10, step = 1, description = "What counts as 'firsthit'", numberType = NumberType.SECONDS)
    private Value<Integer> firstHitDelayValue = new Value<Integer>("firstHitDelay", Integer.valueOf(2));

    // ATTRIBUTES
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
