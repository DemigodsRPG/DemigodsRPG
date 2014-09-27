package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.model.AbstractPersistentModel;
import com.demigodsrpg.demigods.classic.util.JsonFileUtil;
import com.demigodsrpg.demigods.classic.util.JsonSection;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public abstract class AbstractRegistry<T extends AbstractPersistentModel<String>> {
    private final ConcurrentMap<String, T> REGISTERED_DATA = new ConcurrentHashMap<>();

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
        REGISTERED_DATA.put(data.getPersistantId(), data);
        synchronized (data) {
            addToFile(data.getPersistantId(), data);
        }
    }

    public final void register(T[] data) {
        register(Arrays.asList(data));
    }

    final void register(Collection<T> data) {
        for (T data_ : data) {
            register(data_);
        }
    }

    public final synchronized void registerFromFile() {
        JsonSection currentFile = getFile();
        if (currentFile != null) {
            synchronized (currentFile) {
                for (String key : currentFile.getKeys()) {
                    REGISTERED_DATA.put(key, valueFromData(key, currentFile.getSection(key)));
                }
            }
        }
    }

    void unregister(T data) {
        REGISTERED_DATA.remove(data.getPersistantId());
        synchronized (data) {
            deleteFromFile(data.getPersistantId());
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
            return JsonFileUtil.saveFile(DGClassic.SAVE_PATH, getFileName(), currentFile);
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
            return JsonFileUtil.saveFile(DGClassic.SAVE_PATH, getFileName(), currentFile);
        }

        return false;
    }

    final JsonSection getFile() {
        try {
            return JsonFileUtil.getSection(DGClassic.SAVE_PATH, getFileName());
        } catch (Exception ignored) {
            DGClassic.CONSOLE.warning("File corrupt: " + getFileName());
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
}
