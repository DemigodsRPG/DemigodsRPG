package com.demigodsrpg.demigods.classic.registry;


import com.demigodsrpg.demigods.classic.model.Model;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public abstract class AbstractRegistry<K, T extends Model> {
    protected ConcurrentMap<K, T> REGISTERED_DATA = new ConcurrentHashMap<>();

    public void register(T data) {
        REGISTERED_DATA.put((K) data.getPersistantId(), data);
    }

    public void register(T[] data) {
        register(Arrays.asList(data));
    }

    public void register(Collection<T> data) {
        for (T data_ : data) {
            register(data_);
        }
    }

    @SuppressWarnings("RedundantCast")
    public void unregister(T data) {
        REGISTERED_DATA.remove((K) data.getPersistantId());
    }

    public Collection<T> getRegistered() {
        return REGISTERED_DATA.values();
    }
}
