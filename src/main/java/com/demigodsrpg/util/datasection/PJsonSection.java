package com.demigodsrpg.util.datasection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iciql.Db;
import com.iciql.Iciql;
import com.iciql.util.Utils;
import org.postgresql.util.PGobject;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

/**
 * Object representing a section of a json db.
 */
@SuppressWarnings("unchecked")
public class PJsonSection implements DataSection, Serializable {
    private static final long serialVersionUID = -8256135535498326597L;

    // -- PRIVATE FIELDS -- //

    Map<String, Object> SECTION_DATA = new HashMap<>();

    // -- CONSTRUCTORS -- //

    /**
     * Default constructor.
     */
    public PJsonSection() {
    }

    /**
     * Constructor accepting default data.
     *
     * @param data Default data.
     */
    public PJsonSection(Map<String, Object> data) {
        if (data != null) {
            SECTION_DATA = data;
        } else {
            throw new NullPointerException("PJsonSection will not hold null.");
        }
    }

    // -- UTILITY METHODS -- //

    /**
     * Save this section to a json db.
     *
     * @param name       The section name.
     * @param connection The db to hold the section data.
     * @return Save success or failure.
     */
    public boolean save(String name, String connection) {
        ThreadLocal<Table> tableSafe = Utils.newThreadLocal(Table.class);
        Db db = Db.open(connection);
        try {
            if (db.from(tableSafe.get()).where(tableSafe.get().id).is(name).select().size() > 0) {
                db.update(new Table(name, this));
            } else {
                db.insert(new Table(name, this));
            }
        } catch (Exception oops) {
            oops.printStackTrace();
            return false;
        } finally {
            db.close();
        }
        return true;
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

    public PJsonSection getSectionNullable(String s) {
        if (contains(s) && isSection(s)) {
            try {
                PJsonSection section = new PJsonSection();
                section.SECTION_DATA = (Map) get(s);
                return section;
            } catch (Exception ignored) {
            }
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

    public PJsonSection createSection(String s) {
        PJsonSection section = new PJsonSection();
        SECTION_DATA.put(s, section.SECTION_DATA);
        return section;
    }

    public PJsonSection createSection(String s, Map<String, Object> map) {
        PJsonSection section = new PJsonSection();
        section.SECTION_DATA = map;
        SECTION_DATA.put(s, section.SECTION_DATA);
        return section;
    }

    @Override
    public FJsonSection toFJsonSection() {
        return new FJsonSection(SECTION_DATA);
    }

    @Override
    public PJsonSection toPJsonSection() {
        return this;
    }

    public static class Adapter implements Iciql.DataTypeAdapter<PJsonSection> {
        private Iciql.Mode mode;

        @Override
        public String getDataType() {
            return "json";
        }

        @Override
        public Class<PJsonSection> getJavaType() {
            return PJsonSection.class;
        }

        @Override
        public void setMode(Iciql.Mode mode) {
            this.mode = mode;
        }

        @Override
        public Object serialize(PJsonSection section) {
            String json = gson().toJson(section.SECTION_DATA, Map.class);
            PGobject pg = new PGobject();
            pg.setType(getDataType());
            try {
                pg.setValue(json);
            } catch (SQLException ignored) {
            }
            return pg;
        }

        @Override
        public PJsonSection deserialize(Object value) {
            return new PJsonSection(gson().fromJson(value.toString(), Map.class));
        }

        private Gson gson() {
            return new GsonBuilder().create();
        }
    }

    @Iciql.IQTable(name = "json_sections")
    public static class Table {
        @Iciql.IQColumn(primaryKey = true)
        public String id;

        @Iciql.IQColumn(nullable = false)
        @Iciql.TypeAdapter(Adapter.class)
        PJsonSection section;

        public Table() {
        }

        public Table(String id, PJsonSection section) {
            this.id = id;
            this.section = section;
        }
    }
}
