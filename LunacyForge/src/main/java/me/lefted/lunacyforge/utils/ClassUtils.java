package me.lefted.lunacyforge.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.core.helpers.CachedClock;

public class ClassUtils {

    // ATTRIBUTES
    private static Map<String, Boolean> cachedClasses = new HashMap<String, Boolean>();

    /**
     * Allows you to check for existing classes with the [className]
     */
    public static boolean hasClass(String className) {
	
	if (cachedClasses.containsKey(className)) {
	    return cachedClasses.get(className);
	} else {
	    try {
		Class.forName(className);
		cachedClasses.put(className, true);
		return true;
	    } catch (ClassNotFoundException e) {
		cachedClasses.put(className, false);
		return false;
	    }
	}
    }

}
