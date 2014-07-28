package com.demigodsrpg.demigods.classic.ability;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Ability {
    public enum Type {
        PASSIVE, SUPPORT, OFFENSIVE, ULTIMATE, PLACEHOLDER
    }

    String name();

    String command() default "";

    String[] info();

    Type type() default Type.OFFENSIVE;

    /**
     * Favor cost.
     */
    double cost() default 0.0;

    /**
     * Times are in milliseconds.
     */
    long delay() default 0;

    long cooldown() default 0;
}
