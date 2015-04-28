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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Object representing a section of a json file.
 */
@SuppressWarnings("unchecked")
public class FJsonSection implements DataSection, Serializable {
    private static final long serialVersionUID = -5020712662755828168L;

    // -- PRIVATE FIELDS -- //

    private Map<String, Object> SECTION_DATA = new HashMap<>();

    // -- CONSTRUCTORS -- //

    /**
     * Default constructor.
     */
    private FJsonSection() {
    }

    /**
     * Constructor accepting default data.
     *
     * @param data Default data.
     */
    public FJsonSection(Map<String, Object> data) {
        if (data != null) {
            SECTION_DATA = data;
        }
    }

    // -- UTILITY METHODS -- //

    /**
     * Save this section to a json file.
     *
     * @param dataFile The file to hold the section data.
     * @return Save success or failure.
     */
    public boolean save(File dataFile) {
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String json = gson.toJson(SECTION_DATA, Map.class);
            PrintWriter writer = new PrintWriter(dataFile);
            writer.print(json);
            writer.close();
            return true;
        } catch (Exception oops) {
            oops.printStackTrace();
        }
        return false;
    }

    /**
     * Save this section to a json file in a pretty format.
     *
     * @param dataFile The file to hold the section data.
     * @return Save success or failure.
     */
    public boolean savePretty(File dataFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
            String json = gson.toJson(SECTION_DATA, Map.class);
            PrintWriter writer = new PrintWriter(dataFile);
            writer.print(json);
            writer.close();
            return true;
        } catch (Exception oops) {
            oops.printStackTrace();
        }
        return false;
    }

    // -- GETTERS -- //

    // TODO Add documentation for the methods below.

    public Set<String> getKeys() {
        return SECTION_DATA.keySet();
    }

    public Map<String, Object> getValues() {
        return SECTION_DATA;
    }

    public boolean contains(String s) {
        return SECTION_DATA.containsKey(s);
    }

    public boolean isSet(String s) {
        return SECTION_DATA.containsKey(s) && SECTION_DATA.get(s) != null;
    }

    public Object get(String s) {
        return SECTION_DATA.get(s);
    }

    public Object get(String s, Object o) {
        if (contains(s)) {
            return get(s);
        }
        return o;
    }

    public Object getNullable(String s) {
        if (contains(s)) {
            return get(s);
        }
        return null;
    }

    public String getString(String s) {
        return get(s).toString();
    }

    public String getString(String s, String s2) {
        return get(s, s2).toString();
    }

    public String getStringNullable(String s) {
        return contains(s) ? getString(s) : null;
    }

    public boolean isString(String s) {
        return get(s) instanceof String;
    }

    public int getInt(String s) {
        return getDouble(s).intValue();
    }

    public int getInt(String s, int i) {
        return getDouble(s, i).intValue();
    }

    public Integer getIntNullable(String s) {
        return contains(s) ? getInt(s) : null;
    }

    public boolean isInt(String s) {
        return get(s) instanceof Integer;
    }

    public boolean getBoolean(String s) {
        return Boolean.parseBoolean(getString(s));
    }

    public boolean getBoolean(String s, boolean b) {
        return Boolean.parseBoolean(get(s, b).toString());
    }

    public Boolean getBooleanNullable(String s) {
        return contains(s) ? getBoolean(s) : null;
    }

    public boolean isBoolean(String s) {
        return get(s) instanceof Boolean;
    }

    public Double getDouble(String s) {
        return Double.parseDouble(get(s).toString());
    }

    public Double getDouble(String s, double v) {
        return Double.parseDouble(get(s, v).toString());
    }

    public Double getDoubleNullable(String s) {
        return contains(s) ? getDouble(s) : null;
    }

    public boolean isDouble(String s) {
        return get(s) instanceof Double;
    }

    public long getLong(String s) {
        return getDouble(s).longValue();
    }

    public long getLong(String s, long l) {
        return getDouble(s, l).longValue();
    }

    public Long getLongNullable(String s) {
        return contains(s) ? getLong(s) : null;
    }

    public boolean isLong(String s) {
        return get(s) instanceof Long;
    }

    public List<Object> getList(String s) {
        return (List) get(s);
    }

    public List<Object> getList(String s, List<Object> objects) {
        return (List) get(s, objects);
    }

    public List<Object> getListNullable(String s) {
        return contains(s) ? getList(s) : null;
    }

    public boolean isList(String s) {
        return get(s) instanceof List;
    }

    public List<String> getStringList(String s) {
        return (List) get(s);
    }

    public List<Double> getDoubleList(String s) {
        return (List) get(s);
    }

    public List<Double> getDoubleListNullable(String s) {
        return contains(s) ? getDoubleList(s) : null;
    }

    public List<Map<String, Object>> getMapList(String s) {
        return (List) get(s);
    }

    public List<Map<String, Object>> getMapListNullable(String s) {
        return contains(s) ? getMapList(s) : null;
    }

    public FJsonSection getSectionNullable(String s) {
        try {
            FJsonSection section = new FJsonSection();
            section.SECTION_DATA = (Map) get(s);
            return section;
        } catch (Exception ignored) {
        }
        return null;
    }

    public boolean isSection(String s) {
        return get(s) instanceof Map;
    }

    // -- MUTATORS -- //

    public void set(String s, Object o) {
        SECTION_DATA.put(s, o);
    }

    public void remove(String s) {
        SECTION_DATA.put(s, null);
    }

    public FJsonSection createSection(String s) {
        FJsonSection section = new FJsonSection();
        SECTION_DATA.put(s, section.SECTION_DATA);
        return section;
    }

    public FJsonSection createSection(String s, Map<String, Object> map) {
        FJsonSection section = new FJsonSection();
        section.SECTION_DATA = map;
        SECTION_DATA.put(s, section.SECTION_DATA);
        return section;
    }

    @Override
    public FJsonSection toFJsonSection() {
        return this;
    }

    @Override
    public PJsonSection toPJsonSection() {
        return new PJsonSection(SECTION_DATA);
    }
}
