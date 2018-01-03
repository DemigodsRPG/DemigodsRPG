package com.demigodsrpg.ability;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Ability {
    enum Type {
        PASSIVE, SUPPORT, OFFENSIVE, ULTIMATE
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

    boolean placeholder() default false;
}
