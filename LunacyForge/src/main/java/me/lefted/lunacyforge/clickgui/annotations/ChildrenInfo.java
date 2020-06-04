package me.lefted.lunacyforge.clickgui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Enumeration;
import java.util.function.Predicate;

import me.lefted.lunacyforge.modules.Category;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChildrenInfo {

    /**
     * @return the id of this container
     */
    int containerID() default 0;
    
    /**
     * @return the ids of the respective children containers
     */
    int[] childrenIDS() default {};
}
