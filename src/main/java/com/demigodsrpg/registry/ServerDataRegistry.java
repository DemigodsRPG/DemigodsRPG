package com.demigodsrpg.registry;

import com.demigodsrpg.ability.CooldownHandler;
import com.demigodsrpg.model.ServerDataModel;
import com.demigodsrpg.util.datasection.DataSection;
import com.demigodsrpg.util.datasection.Registry;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public interface ServerDataRegistry extends Registry<ServerDataModel>, CooldownHandler {
    String NAME = "misc";

    default void put(String row, String column, String value) {
        putRaw(row, column, value);
    }

    default void put(String row, String column, boolean value) {
        putRaw(row, column, value);
    }

    default void put(String row, String column, Number value) {
        putRaw(row, column, value);
    }

    @Deprecated
    default void putRaw(String row, String column, Object obj) {
        // Remove the value if it exists already
        remove(row, column);

        // Create and save the timed value
        ServerDataModel timedData = new ServerDataModel();
        timedData.generateId();
        timedData.setDataType(ServerDataModel.DataType.PERSISTENT);
        timedData.setRow(row);
        timedData.setColumn(column);
        timedData.setValue(obj);
        register(timedData);
    }

    /*
     * Timed value
     */
    default void put(String row, String column, Object value, long time, TimeUnit unit) {
        // Remove the value if it exists already
        remove(row, column);

        // Create and save the timed value
        ServerDataModel timedData = new ServerDataModel();
        timedData.generateId();
        timedData.setDataType(ServerDataModel.DataType.TIMED);
        timedData.setRow(row);
        timedData.setColumn(column);
        timedData.setValue(value);
        timedData.setExpiration(unit, time);
        register(timedData);
    }

    default boolean contains(String row, String column) {
        return find(row, column) != null;
    }

    default Object get(String row, String column) {
        return find(row, column).getValue();
    }

    default long getExpiration(String row, String column) throws NullPointerException {
        return find(row, column).getExpiration();
    }

    default ServerDataModel find(String row, String column) {
        if (findByRow(row) == null) return null;

        for (ServerDataModel data : findByRow(row)) { if (data.getColumn().equals(column)) return data; }

        return null;
    }

    default Set<ServerDataModel> findByRow(final String row) {
        return getRegisteredData().values().stream().filter(model -> model.getRow().equals(row))
                .collect(Collectors.toSet());
    }

    default void remove(String row, String column) {
        if (find(row, column) != null) remove(find(row, column).getKey());
    }

    /**
     * Clears all expired timed value.
     */
    default void clearExpired() {
        getRegisteredData().values().stream()
                .filter(model -> ServerDataModel.DataType.TIMED.equals(model.getDataType()) &&
                        model.getExpiration() <= System.currentTimeMillis()).map(ServerDataModel::getKey)
                .collect(Collectors.toList()).forEach(this::remove);
    }

    @Override
    default ServerDataModel fromDataSection(String stringKey, DataSection data) {
        return new ServerDataModel(stringKey, data);
    }
}
