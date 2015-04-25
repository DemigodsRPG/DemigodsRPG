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
import com.demigodsrpg.data.model.AbstractPersistentModel;
import com.demigodsrpg.util.JsonFileUtil;
import com.demigodsrpg.util.JsonSection;
import com.demigodsrpg.util.Setting;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public abstract class AbstractRegistry<T extends AbstractPersistentModel<String>> {
    protected final ConcurrentMap<String, T> REGISTERED_DATA = new ConcurrentHashMap<>();

    public T fromId(String id) {
        if (REGISTERED_DATA.get(id) == null) {
            JsonSection currentFile = getFile();
            if (currentFile != null) {
                synchronized (currentFile) {
                    if (currentFile.isSection(id)) {
                        registerFromFile();
                    }
                }
            }
        }
        return REGISTERED_DATA.get(id);
    }

    public void register(T data) {
        REGISTERED_DATA.put(data.getPersistentId(), data);
        synchronized (data) {
            addToFile(data.getPersistentId(), data);
        }
    }

    public final void register(T[] data) {
        register(Arrays.asList(data));
    }

    final void register(Collection<T> data) {
        data.forEach(this::register);
    }

    public final synchronized void registerFromFile() {
        JsonSection currentFile = getFile();
        if (currentFile != null) {
            synchronized (currentFile) {
                for (String key : currentFile.getKeys()) {
                    REGISTERED_DATA.put(key, valueFromData(key, currentFile.getSectionNullable(key)));
                }
            }
        }
    }

    public void unregister(T data) {
        REGISTERED_DATA.remove(data.getPersistentId());
        synchronized (data) {
            deleteFromFile(data.getPersistentId());
        }
    }

    public Collection<T> getRegistered() {
        if (REGISTERED_DATA.isEmpty()) {
            registerFromFile();
        }
        return REGISTERED_DATA.values();
    }

    synchronized boolean deleteFromFile(String key) {
        // Grab the current file, and its data as a usable map.
        JsonSection currentFile = getFile();

        if (currentFile != null) {
            // Remove data.
            currentFile.remove(key);

            // Save the file!
            if (isPretty() || Setting.SAVE_PRETTY) {
                return JsonFileUtil.saveFilePretty(DGData.SAVE_PATH, getFileName(), currentFile);
            }
            return JsonFileUtil.saveFile(DGData.SAVE_PATH, getFileName(), currentFile);
        }

        return false;
    }

    synchronized boolean addToFile(String key, T data) {
        // Grab the current file, and its data as a usable map.
        JsonSection currentFile = getFile();

        if (currentFile != null) {
            // Create/overwrite a configuration section.
            currentFile.createSection(key, data.serialize());

            // Save the file!
            if (isPretty() || Setting.SAVE_PRETTY) {
                return JsonFileUtil.saveFilePretty(DGData.SAVE_PATH, getFileName(), currentFile);
            }
            return JsonFileUtil.saveFile(DGData.SAVE_PATH, getFileName(), currentFile);
        }

        return false;
    }

    final JsonSection getFile() {
        try {
            return JsonFileUtil.getSection(DGData.SAVE_PATH, getFileName());
        } catch (Exception oops) {
            DGData.CONSOLE.warning("File corrupt: " + getFileName());
            oops.printStackTrace();
        }
        return null;
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
    protected abstract T valueFromData(String stringKey, JsonSection data);

    protected abstract String getFileName();

    protected boolean isPretty() {
        return false;
    }
}
