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
public abstract class AbstractRegistry<T extends AbstractPersistentModel<String>> {
    protected ConcurrentMap<String, T> REGISTERED_DATA = new ConcurrentHashMap<>();

    public T fromId(String id) {
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
        REGISTERED_DATA.remove(data.getPersistantId());
    }

    public Collection<T> getRegistered() {
        return REGISTERED_DATA.values();
    }

    public boolean saveToFile() {
        // Grab the current file, and its data as a usable map.
        FileConfiguration currentFile = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, getFileName());
        final Map<String, T> currentFileMap = getFileData();

        // Create/overwrite a configuration section if new data exists.
        for (Map.Entry<String, T> data : Collections2.filter(REGISTERED_DATA.entrySet(), new Predicate<Map.Entry<String, T>>() {
            @Override
            public boolean apply(Map.Entry<String, T> entry) {
                return !currentFileMap.containsKey(entry.getKey()) || !currentFileMap.get(entry.getKey()).equals(entry.getValue());
            }
        }))
            currentFile.createSection(data.getKey(), data.getValue().serialize());

        // Remove old unneeded data.
        for (String key : Collections2.filter(currentFileMap.keySet(), new Predicate<String>() {
            @Override
            public boolean apply(String key) {
                return !REGISTERED_DATA.keySet().contains(key);
            }
        }))
            currentFile.set(key, null);

        // Save the file!
        return YamlFileUtil.saveFile(DGClassic.SAVE_PATH, getFileName(), currentFile);
    }

    public final Map<String, T> getFileData() {
        // Grab the current file.
        FileConfiguration data = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, getFileName());

        // Convert the raw file data into more usable data, in map form.
        ConcurrentMap<String, T> map = new ConcurrentHashMap<>();
        for (String stringId : data.getKeys(false)) {
            try {
                T model = valueFromData(stringId, data.getConfigurationSection(stringId));
                if (stringId.equals("null")) {
                    DGClassic.CONSOLE.warning("Corrupt: " + stringId + ", in file: " + getFileName());
                    continue;
                }
                map.put(stringId, model);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public final Map<String, Object> serialize(String id) {
        return (REGISTERED_DATA.get(id)).serialize();
    }

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
