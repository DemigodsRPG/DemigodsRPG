/*
 * Copyright (c) 2015 Demigods RPG
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.demigodsrpg.util.datasection;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class AbstractFileRegistry<T extends Model> implements Registry<T> {
    protected final Cache<String, T> REGISTERED_DATA;

    // -- FILE -- //
    private File FOLDER;
    private boolean PRETTY;

    public AbstractFileRegistry(String path, String folder, boolean pretty, int expireMins) {
        if (expireMins > 0) {
            REGISTERED_DATA =
                    CacheBuilder.newBuilder().concurrencyLevel(4).expireAfterAccess(expireMins, TimeUnit.MINUTES)
                            .build();
        } else {
            REGISTERED_DATA = CacheBuilder.newBuilder().concurrencyLevel(4).build();
        }
        FOLDER = new File(path + "/" + folder + "/");
        FOLDER.mkdirs();
        PRETTY = pretty;
    }

    public Optional<T> fromKey(String key) {
        if (!REGISTERED_DATA.asMap().containsKey(key)) {
            loadFromDb(key);
        }
        return Optional.ofNullable(REGISTERED_DATA.asMap().getOrDefault(key, null));
    }

    public T register(T value) {
        REGISTERED_DATA.put(value.getKey(), value);
        saveToDb(value.getKey());
        return value;
    }

    public T put(String key, T value) {
        REGISTERED_DATA.put(key, value);
        saveToDb(key);
        return value;
    }

    public void remove(String key) {
        REGISTERED_DATA.asMap().remove(key);
        removeFile(key);
    }

    public void saveToDb(String key) {
        if (REGISTERED_DATA.asMap().containsKey(key)) {
            File file = new File(FOLDER.getPath() + "/" + key + ".json");
            if (!(file.exists())) {
                createFile(file);
            }
            Gson gson = PRETTY ? new GsonBuilder().setPrettyPrinting().create() : new GsonBuilder().create();
            String json = gson.toJson(REGISTERED_DATA.asMap().get(key).serialize());
            try {
                PrintWriter writer = new PrintWriter(file);
                writer.print(json);
                writer.close();
            } catch (Exception oops) {
                oops.printStackTrace();
            }

        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromDb(String key) {
        Gson gson = new GsonBuilder().create();
        try {
            File file = new File(FOLDER.getPath() + "/" + key + ".json");
            synchronized (file) {
                if (file.exists()) {
                    FileInputStream inputStream = new FileInputStream(file);
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    FJsonSection section = new FJsonSection(gson.fromJson(reader, Map.class));
                    REGISTERED_DATA.put(key, fromDataSection(key, section));
                    reader.close();
                }
            }
        } catch (Exception oops) {
            oops.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public ConcurrentMap<String, T> loadAllFromDb() {
        for (File file : FOLDER.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                String key = file.getName().replace(".json", "");
                loadFromDb(key);
            }
        }
        return REGISTERED_DATA.asMap();
    }

    public void purge() {
        loadAllFromDb();
        REGISTERED_DATA.asMap().keySet().forEach(this::removeFile);
        REGISTERED_DATA.asMap().clear();
    }

    @Override
    public Map<String, T> getRegisteredData() {
        return REGISTERED_DATA.asMap();
    }

    private void createFile(File file) {
        try {
            FOLDER.mkdirs();
            file.createNewFile();
        } catch (Exception oops) {
            oops.printStackTrace();
        }
    }

    public void removeFile(String key) {
        File file = new File(FOLDER.getPath() + "/" + key + ".json");
        if (file.exists()) {
            file.delete();
        }
    }
}
