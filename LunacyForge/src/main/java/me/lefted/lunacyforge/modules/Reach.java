package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.guiscreenold.interpreter.ModuleInterpreter;
import me.lefted.lunacyforge.valuesystem.Value;

/* Also see: MixinEntityRenderer.java */
@ModuleInfo(description = "Extends the distance from where you can hit things")
public class Reach extends Module {

    // ATTRIBUTES
    private Value<Float> rangeValue = new Value("range", Float.valueOf(3.0F));

    // CONSTRUCTOR
    public Reach() {
	super("Reach", Category.COMBAT);
	setTags("range", "hitdistance");
    }

    // METHODS
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

}
