package me.lefted.lunacyforge.clickgui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.lefted.lunacyforge.clickgui.elements.ContainerSlider;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SliderInfo {

    String description();
    
    ContainerSlider.NumberType numberType();
    
    int min();

    int max();

    double step();
}
