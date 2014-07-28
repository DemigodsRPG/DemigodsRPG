package com.demigodsrpg.demigods.classic.registry;


import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.model.AbstractPersistentModel;
import com.demigodsrpg.demigods.classic.util.YamlFileUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public abstract class AbstractRegistry<K, T extends AbstractPersistentModel<K>> {
    protected ConcurrentMap<K, T> REGISTERED_DATA = new ConcurrentHashMap<>();

    public T fromId(K id) {
        if (REGISTERED_DATA.get(id) != null) {
            return REGISTERED_DATA.get(id);
        }
        return null;
    }

    public void register(T data) {
        REGISTERED_DATA.put(data.getPersistantId(), data);
    }

    public final void register(T[] data) {
        register(Arrays.asList(data));
    }

    public final void register(Collection<T> data) {
        for (T data_ : data) {
            register(data_);
        }
    }

    public final void registerFromFile() {
        register(getFileData().values());
    }

    @SuppressWarnings("RedundantCast")
    public void unregister(T data) {
        REGISTERED_DATA.remove((K) data.getPersistantId());
    }

    public Collection<T> getRegistered() {
        return REGISTERED_DATA.values();
    }

    public boolean saveToFile() {
        // Grab the current file, and its data as a usable map.
        FileConfiguration currentFile = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, getFileName());
        final Map<K, T> currentFileMap = getFileData();

        // Create/overwrite a configuration section if new data exists.
        for (Map.Entry<K, T> data : Collections2.filter(REGISTERED_DATA.entrySet(), new Predicate<Map.Entry<K, T>>() {
            @Override
            public boolean apply(Map.Entry<K, T> entry) {
                return !currentFileMap.containsKey(entry.getKey()) || !currentFileMap.get(entry.getKey()).equals(entry.getValue());
            }
        }))
            currentFile.createSection(data.getKey().toString(), data.getValue().serialize());

        // Remove old unneeded data.
        for (K key : Collections2.filter(currentFileMap.keySet(), new Predicate<K>() {
            @Override
            public boolean apply(K key) {
                return !REGISTERED_DATA.keySet().contains(key);
            }
        }))
            currentFile.set(key.toString(), null);

        // Save the file!
        return YamlFileUtil.saveFile(DGClassic.SAVE_PATH, getFileName(), currentFile);
    }

    public final Map<K, T> getFileData() {
        // Grab the current file.
        FileConfiguration data = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, getFileName());

        // Convert the raw file data into more usable data, in map form.
        ConcurrentMap<K, T> map = new ConcurrentHashMap<>();
        for (String stringId : data.getKeys(false)) {
            try {
                T model = valueFromData(stringId, data.getConfigurationSection(stringId));
                if (stringId.equals("null")) {
                    DGClassic.CONSOLE.warning("Corrupt: " + stringId + ", in file: " + getFileName());
                    continue;
                }
                map.put(keyFromString(stringId), model);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public final Map<String, Object> serialize(K id) {
        return (REGISTERED_DATA.get(id)).serialize();
    }

    /**
     * Convert a key from a string.
     *
     * @param stringKey The provided string.
     * @return The converted key.
     */
    public abstract K keyFromString(String stringKey);

    /**
     * Convert to a get from a number of objects representing the data.
     *
     * @param stringKey The string key for the data.
     * @param data      The provided data object.
     * @return The converted get.
     */
    public abstract T valueFromData(String stringKey, ConfigurationSection data);

    public abstract String getFileName();
}
