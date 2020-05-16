package me.lefted.lunacyforge.utils;

import java.lang.annotation.Annotation;

import me.lefted.lunacyforge.clickgui.annotations.CheckboxInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.valuesystem.Value;

public class AnnotationUtils {

    // returns the ModuleInfo annotation if present
    public static ModuleInfo getModuleInfo(Module module) {
	final Class moduleClass = module.getClass();

	for (Annotation annotation : moduleClass.getDeclaredAnnotations()) {
	    if (annotation instanceof ModuleInfo) {
		return (ModuleInfo) annotation;
	    }
	}
	return null;
    }
}
