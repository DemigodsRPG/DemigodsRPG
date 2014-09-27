package com.demigodsrpg.demigods.classic.util;

import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Object representing a section of a json file.
 */
@SuppressWarnings("unchecked")
public class JsonSection {
    // -- PRIVATE FIELDS -- //

    private Map<String, Object> SECTION_DATA = new HashMap<>();

    // -- CONSTRUCTORS -- //

    /**
     * Default constructor.
     */
    private JsonSection() {
    }

    /**
     * Constructor accepting default data.
     *
     * @param data Default data.
     */
    public JsonSection(Map<String, Object> data) {
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

    // -- GETTERS -- //

    // TODO Add documentation for the methods below.

    public Set<String> getKeys() {
        return SECTION_DATA.keySet();
    }

    public Map<String, Object> getValues() {
        return SECTION_DATA;
    }

    boolean contains(String s) {
        return SECTION_DATA.containsKey(s);
    }

    public boolean isSet(String s) {
        return SECTION_DATA.containsKey(s) && SECTION_DATA.get(s) != null;
    }

    public Object get(String s) {
        return SECTION_DATA.get(s);
    }

    Object get(String s, Object o) {
        if (contains(s)) {
            return get(s);
        }
        return o;
    }

    public String getString(String s) {
        return get(s).toString();
    }

    public String getString(String s, String s2) {
        return get(s, s2).toString();
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

    public boolean isInt(String s) {
        return get(s) instanceof Integer;
    }

    public boolean getBoolean(String s) {
        return Boolean.parseBoolean(getString(s));
    }

    public boolean getBoolean(String s, boolean b) {
        return Boolean.parseBoolean(get(s, b).toString());
    }

    public boolean isBoolean(String s) {
        return get(s) instanceof Boolean;
    }

    public Double getDouble(String s) {
        return Double.parseDouble(get(s).toString());
    }

    Double getDouble(String s, double v) {
        return Double.parseDouble(get(s, v).toString());
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

    public boolean isLong(String s) {
        return get(s) instanceof Long;
    }

    public List<Object> getList(String s) {
        return (List) get(s);
    }

    public List<Object> getList(String s, List<Object> objects) {
        return (List) get(s, objects);
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

    public List<Map<String, Object>> getMapList(String s) {
        return (List) get(s);
    }

    public JsonSection getSection(String s) {
        try {
            JsonSection section = new JsonSection();
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

    public JsonSection createSection(String s) {
        JsonSection section = new JsonSection();
        SECTION_DATA.put(s, section.SECTION_DATA);
        return section;
    }

    public JsonSection createSection(String s, Map<String, Object> map) {
        JsonSection section = new JsonSection();
        section.SECTION_DATA = map;
        SECTION_DATA.put(s, section.SECTION_DATA);
        return section;
    }
}
