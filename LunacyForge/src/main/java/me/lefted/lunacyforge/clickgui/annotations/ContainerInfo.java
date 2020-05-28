package me.lefted.lunacyforge.clickgui.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContainerInfo {

    /**
     * @return the text displayed when the mouse is over this container
     */
    String hoverText() default "";
    
    int groupID() default -1;
    
    /**
     * @return the valuenames of the children
     */
    // TODO FIXME
    String[] children() default {};
}
