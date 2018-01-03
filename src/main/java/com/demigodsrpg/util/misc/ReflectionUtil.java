/*
 * Copyright 2014 Alex Bennett & Alexander Chauncey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.util.misc;

import java.lang.reflect.*;

public class ReflectionUtil {
    /**
     * Set a static value.
     *
     * @param field The field to manipulate.
     * @param value The value being set.
     */
    public static void setStaticValue(Field field, Object value) {
        try {
            Field modifiers = Field.class.getDeclaredField("modifiers");

            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, value);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set a private value.
     *
     * @param instance The object being manipulated.
     * @param name     The name of the field being manipulated.
     * @param value    The value being set.
     */
    public static void setPrivateValue(Object instance, String name, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception ignored) {
        }
    }

    /**
     * Get a private value.
     *
     * @param instance The object being manipulated.
     * @param name     The name of the field being manipulated.
     * @return The value from the manipulated field.
     */
    public static Object getPrivateValue(Object instance, String name) {
        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception ignored) {
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T, V> V getPrivateMethod(Class<T> clazz, String methodName, T instance, Object... args) {
        V value = null;
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            method.setAccessible(true);
            value = (V) method.invoke(instance, args);
        } catch (Exception ignored) {
        }
        return value;
    }
}
