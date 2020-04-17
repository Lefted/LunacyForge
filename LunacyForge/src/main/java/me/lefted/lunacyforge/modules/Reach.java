package me.lefted.lunacyforge.modules;

import me.lefted.lunacyforge.guiscreenold.interpreter.ModuleInterpreter;
import me.lefted.lunacyforge.valuesystem.Value;

/* Also see: MixinEntityRenderer.java */
@ModuleInterpreter(description = "Extends the distance you punch")
public class Reach extends Module {

    private Value<Float> rangeValue = new Value("range", Float.valueOf(3.0F));

    public Reach() {
	super("Reach", Category.COMBAT);
	setTags("range", "hitdistance");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

}
