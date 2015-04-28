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

package com.demigodsrpg.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Object representing a section of a json file.
 */
@SuppressWarnings("unchecked")
public interface DataSection {

    // -- GETTERS -- //

    Set<String> getKeys();

    Map<String, Object> getValues();

    boolean contains(String s);

    boolean isSet(String s);

    Object get(String s);

    Object get(String s, Object o);

    Object getNullable(String s);

    String getString(String s);

    String getString(String s, String s2);

    String getStringNullable(String s);

    boolean isString(String s);

    int getInt(String s);

    int getInt(String s, int i);

    Integer getIntNullable(String s);

    boolean isInt(String s);

    boolean getBoolean(String s);

    boolean getBoolean(String s, boolean b);

    Boolean getBooleanNullable(String s);

    boolean isBoolean(String s);

    Double getDouble(String s);

    Double getDouble(String s, double v);

    Double getDoubleNullable(String s);

    boolean isDouble(String s);

    long getLong(String s);

    long getLong(String s, long l);

    Long getLongNullable(String s);

    boolean isLong(String s);

    List<Object> getList(String s);

    List<Object> getList(String s, List<Object> objects);

    List<Object> getListNullable(String s);

    boolean isList(String s);

    List<String> getStringList(String s);

    List<Double> getDoubleList(String s);

    List<Double> getDoubleListNullable(String s);

    List<Map<String, Object>> getMapList(String s);

    List<Map<String, Object>> getMapListNullable(String s);

    DataSection getSectionNullable(String s);

    boolean isSection(String s);

    // -- MUTATORS -- //

    void set(String s, Object o);

    void remove(String s);

    DataSection createSection(String s);

    DataSection createSection(String s, Map<String, Object> map);

    FJsonSection toFJsonSection();

    PJsonSection toPJsonSection();
}
