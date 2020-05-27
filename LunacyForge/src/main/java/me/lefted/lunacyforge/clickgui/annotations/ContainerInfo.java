package me.lefted.lunacyforge.clickgui.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContainerInfo {

    /**
     * @return the text displayed when the mouse is over this container
     */
    String hoverText() default "";
    
    int groupID() default -1;
}
