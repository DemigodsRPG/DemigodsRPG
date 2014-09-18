package com.demigodsrpg.demigods.classic.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class JsonSection {
    private Map<String, Object> SECTION_DATA = new HashMap<>();

    public JsonSection() {
    }

    public JsonSection(Map<String, Object> data) {
        if (data != null) {
            SECTION_DATA = data;
        }
    }

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

    public void set(String s, Object o) {
        SECTION_DATA.put(s, o);
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

    public Double getDouble(String s, double v) {
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
}
