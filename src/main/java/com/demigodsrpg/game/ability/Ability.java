/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.game.ability;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
