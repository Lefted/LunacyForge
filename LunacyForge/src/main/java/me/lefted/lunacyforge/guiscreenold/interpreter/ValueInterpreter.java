package me.lefted.lunacyforge.guiscreenold.interpreter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValueInterpreter {

    ValueType type();
    String descrption();
    String tooltip();
    String[] selectionNames() default {};
    
}
