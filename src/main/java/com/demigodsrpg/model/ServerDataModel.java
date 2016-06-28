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

package com.demigodsrpg.model;

import com.demigodsrpg.util.datasection.AbstractPersistentModel;
import com.demigodsrpg.util.datasection.DataSection;

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

    public ServerDataModel(String id, DataSection conf) {
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
    public String getPersistentId() {
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
