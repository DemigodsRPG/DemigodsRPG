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

package com.demigodsrpg.data.registry;

import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.Setting;
import com.demigodsrpg.data.model.AbstractPersistentModel;
import com.demigodsrpg.util.DataSection;
import com.demigodsrpg.util.DataSectionUtil;
import com.demigodsrpg.util.FJsonSection;
import com.demigodsrpg.util.PJsonSection;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public abstract class AbstractDataRegistry<T extends AbstractPersistentModel<String>> {
    protected final ConcurrentMap<String, T> REGISTERED_DATA = new ConcurrentHashMap<>();

    public T fromId(String id) {
        if (REGISTERED_DATA.get(id) == null) {
            DataSection currentDb = getDatabase();
            if (currentDb != null) {
                synchronized (currentDb) {
                    if (currentDb.isSection(id)) {
                        registerFromDatabase();
                    }
                }
            }
        }
        return REGISTERED_DATA.get(id);
    }

    public void register(T data) {
        REGISTERED_DATA.put(data.getPersistentId(), data);
        synchronized (data) {
            addToDatabase(data.getPersistentId(), data);
        }
    }

    public final void register(T[] data) {
        register(Arrays.asList(data));
    }

    final void register(Collection<T> data) {
        data.forEach(this::register);
    }

    public final synchronized void registerFromDatabase() {
        DataSection currentDb = getDatabase();
        if (currentDb != null) {
            synchronized (currentDb) {
                for (String key : currentDb.getKeys()) {
                    REGISTERED_DATA.put(key, valueFromData(key, currentDb.getSectionNullable(key)));
                }
            }
        }
    }

    public void unregister(T data) {
        REGISTERED_DATA.remove(data.getPersistentId());
        synchronized (data) {
            deleteFromDatabase(data.getPersistentId());
        }
    }

    public Collection<T> getRegistered() {
        if (REGISTERED_DATA.isEmpty()) {
            registerFromDatabase();
        }
        return REGISTERED_DATA.values();
    }

    synchronized boolean deleteFromDatabase(String key) {
        // Grab the current db, and its data as a usable map.
        DataSection currentDb = getDatabase();
        if (currentDb != null) {
            // Remove data.
            currentDb.remove(key);

            if (!Setting.PSQL_PERSISTENCE) {
                // Save the file!
                if (isPretty() || Setting.SAVE_PRETTY) {
                    return DataSectionUtil.saveFilePretty(DGData.SAVE_PATH + getName() + getExtention(), currentDb);
                }
                return DataSectionUtil.saveFile(DGData.SAVE_PATH + getName() + getExtention(), currentDb);
            } else {
                return DataSectionUtil.savePSQL(getName(), Setting.PSQL_CONNECTION, currentDb);
            }
        }

        return false;
    }

    synchronized boolean addToDatabase(String key, T data) {
        // Grab the current file, and its data as a usable map.
        DataSection currentDb = getDatabase();

        if (currentDb != null) {
            // Create/overwrite a configuration section.
            currentDb.createSection(key, data.serialize());

            if (!Setting.PSQL_PERSISTENCE) {
                // Save the file!
                if (isPretty() || Setting.SAVE_PRETTY) {
                    return DataSectionUtil.saveFilePretty(DGData.SAVE_PATH + getName() + getExtention(), currentDb);
                }
                return DataSectionUtil.saveFile(DGData.SAVE_PATH + getName() + getExtention(), currentDb);
            } else {
                return DataSectionUtil.savePSQL(getName(), Setting.PSQL_CONNECTION, currentDb);
            }
        }

        return false;
    }

    final DataSection getDatabase() {
        if (Setting.PSQL_PERSISTENCE) {
            try {
                return DataSectionUtil.loadSectionFromPSQL(getName(), Setting.PSQL_CONNECTION).get();
            } catch (Exception ignored) {
            }
        } else {
            try {
                return DataSectionUtil.loadSectionFromFile(DGData.SAVE_PATH + getName() + getExtention()).get();
            } catch (Exception ignored) {
            }
        }
        if (Setting.PSQL_PERSISTENCE) {
            return new PJsonSection(new ConcurrentHashMap<>());
        }
        return new FJsonSection(new ConcurrentHashMap<>());
    }

    public synchronized final void clearCache() {
        REGISTERED_DATA.clear();
    }

    /**
     * Convert to a get from a number of objects representing the data.
     *
     * @param stringKey The string key for the data.
     * @param data      The provided data object.
     * @return The converted get.
     */
    protected abstract T valueFromData(String stringKey, DataSection data);

    protected abstract String getName();

    protected String getExtention() {
        return ".dgdat";
    }

    protected boolean isPretty() {
        return false;
    }
}
