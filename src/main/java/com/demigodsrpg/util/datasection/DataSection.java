package com.demigodsrpg.util.datasection;

import java.util.List;
import java.util.Map;

/**
 * Object representing a section of a json file.
 */
@SuppressWarnings("unchecked")
public interface DataSection extends Map<String, Object> {

    // -- GETTERS -- //

    default boolean isSet(String s) {
        return containsKey(s) && get(s) != null;
    }

    default Object get(String s, Object o) {
        if (containsKey(s)) {
            return get(s);
        }
        return o;
    }

    DataSection getSectionNullable(String s);

    default boolean isSection(String s) {
        return get(s) instanceof Map;
    }

    default Object getNullable(String s) {
        if (containsKey(s)) {
            return get(s);
        }
        return null;
    }

    default String getString(String s) {
        return get(s).toString();
    }

    default String getString(String s, String s2) {
        return get(s, s2).toString();
    }

    default String getStringNullable(String s) {
        return containsKey(s) ? getString(s) : null;
    }

    default boolean isString(String s) {
        return get(s) instanceof String;
    }

    default int getInt(String s) {
        return getDouble(s).intValue();
    }

    default int getInt(String s, int i) {
        return getDouble(s, i).intValue();
    }

    default Integer getIntNullable(String s) {
        return containsKey(s) ? getInt(s) : null;
    }

    default boolean isInt(String s) {
        return get(s) instanceof Integer;
    }

    default boolean getBoolean(String s) {
        return Boolean.parseBoolean(getString(s));
    }

    default boolean getBoolean(String s, boolean b) {
        return Boolean.parseBoolean(get(s, b).toString());
    }

    default Boolean getBooleanNullable(String s) {
        return containsKey(s) ? getBoolean(s) : null;
    }

    default boolean isBoolean(String s) {
        return get(s) instanceof Boolean;
    }

    default Double getDouble(String s) {
        return Double.parseDouble(get(s).toString());
    }

    default Double getDouble(String s, double v) {
        return Double.parseDouble(get(s, v).toString());
    }

    default Double getDoubleNullable(String s) {
        return containsKey(s) ? getDouble(s) : null;
    }

    default boolean isDouble(String s) {
        return get(s) instanceof Double;
    }

    default long getLong(String s) {
        return getDouble(s).longValue();
    }

    default long getLong(String s, long l) {
        return getDouble(s, l).longValue();
    }

    default Long getLongNullable(String s) {
        return containsKey(s) ? getLong(s) : null;
    }

    default boolean isLong(String s) {
        return get(s) instanceof Long;
    }

    default List<Object> getList(String s) {
        return (List) get(s);
    }

    default List<Object> getList(String s, List<Object> objects) {
        return (List) get(s, objects);
    }

    default List<Object> getListNullable(String s) {
        return containsKey(s) ? getList(s) : null;
    }

    default boolean isList(String s) {
        return get(s) instanceof List;
    }

    default List<String> getStringList(String s) {
        return (List) get(s);
    }

    default List<Double> getDoubleList(String s) {
        return (List) get(s);
    }

    default List<Double> getDoubleListNullable(String s) {
        return containsKey(s) ? getDoubleList(s) : null;
    }

    default List<Map<String, Object>> getMapList(String s) {
        return (List) get(s);
    }

    default List<Map<String, Object>> getMapListNullable(String s) {
        return containsKey(s) ? getMapList(s) : null;
    }

    // -- MUTATORS -- //

    DataSection createSection(String s);

    DataSection createSection(String s, Map<String, Object> map);

    FJsonSection toFJsonSection();

    MJsonSection toMJsonSection();
}
