package com.demigodsrpg.demigods.classic.registry;


import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.model.AbstractPersistentModel;
import com.demigodsrpg.demigods.classic.util.YamlFileUtil;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Collections2;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public abstract class AbstractRegistry<K, T extends AbstractPersistentModel<K>> {
    protected Cache<K, T> REGISTERED_DATA_CACHE = CacheBuilder.newBuilder().concurrencyLevel(4).maximumSize(1000).expireAfterWrite(5, TimeUnit.SECONDS).build(new CacheLoader<K, T>() {
        @Override
        public T load(K key) throws Exception {
            return valueFromData(key.toString(), getFileData().getConfigurationSection(key.toString()));
        }
    });

    public T fromId(K id) {
        try {
            if (REGISTERED_DATA_CACHE.get(id) != null) {
                return REGISTERED_DATA_CACHE.get(id);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public void register(T data) {
        FileConfiguration file = getFileData();
        file.set(data.getPersistantId().toString(), data.serialize());
        YamlFileUtil.saveFile(DGClassic.SAVE_PATH, getFileName(), file);
        REGISTERED_DATA_CACHE.invalidate(data.getPersistantId());
    }

    public final void register(T[] data) {
        register(Arrays.asList(data));
    }

    public final void register(Collection<T> data) {
        for (T data_ : data) {
            register(data_);
        }
    }

    public void unregister(T data) {
        unregister(data.getPersistantId());
    }

    public void unregister(K key) {
        FileConfiguration file = getFileData();
        file.set(key.toString(), null);
        YamlFileUtil.saveFile(DGClassic.SAVE_PATH, getFileName(), file);
        REGISTERED_DATA_CACHE.invalidate(key);
    }

    public Collection<T> getRegistered() {
        return Collections2.filter(Collections2.transform(getRegisteredKeys(), new Function<K, T>() {
            @Override
            public T apply(K k) {
                return fromId(k);
            }
        }), new Predicate<T>() {
            @Override
            public boolean apply(@Nullable T t) {
                return t != null;
            }
        });
    }

    public Collection<K> getRegisteredKeys() {
        return Collections2.transform(getFileData().getKeys(false), new Function<String, K>() {
            @Override
            public K apply(String s) {
                return keyFromString(s);
            }
        });
    }

    public Cache<K, T> getCache() {
        return REGISTERED_DATA_CACHE;
    }

    public final FileConfiguration getFileData() {
        // Grab the current file
        return YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, getFileName());
    }

    public final Map<K, T> getFileDataAsMap() {
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
