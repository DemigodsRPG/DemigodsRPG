package com.demigodsrpg.demigods.classic.model;

import com.demigodsrpg.demigods.classic.util.JsonSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerDataModel extends AbstractPersistentModel<String> {
    private String id;
    private DataType type;
    private String row;
    private String column;
    private Object value;
    private Long expiration;

    public ServerDataModel() {
    }

    public ServerDataModel(String id, JsonSection conf) {
        this.id = id;
        this.type = DataType.valueOf(conf.getString("type"));
        this.row = conf.getString("row");
        this.column = conf.getString("column");
        this.value = conf.get("value");
        if (conf.get("expiration") != null) this.expiration = conf.getLong("expiration");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type.name());
        map.put("row", row);
        map.put("column", column);
        map.put("value", value);
        if (expiration != null) map.put("expiration", expiration);
        return map;
    }

    public void generateId() {
        id = UUID.randomUUID().toString();
    }

    public void setDataType(DataType type) {
        this.type = type;
    }

    public void setRow(String key) {
        this.row = key;
    }

    public void setColumn(String subKey) {
        this.column = subKey;
    }

    public void setValue(Object data) {
        if (data instanceof String || data instanceof Integer || data instanceof Boolean || data instanceof Double || data instanceof Map || data instanceof List)
            this.value = data;
        else if (data == null) this.value = "null";
        else this.value = data.toString();
    }

    public void setExpiration(TimeUnit unit, long time) {
        this.expiration = System.currentTimeMillis() + unit.toMillis(time);
    }

    public DataType getDataType() {
        return type;
    }

    @Override
    public String getPersistantId() {
        return id;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    public Long getExpiration() {
        return expiration;
    }

    public enum DataType {
        PERSISTENT, TIMED
    }

}
