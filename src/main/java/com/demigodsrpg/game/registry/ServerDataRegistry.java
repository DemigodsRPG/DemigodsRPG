package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.model.ServerDataModel;
import com.demigodsrpg.game.util.JsonSection;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ServerDataRegistry extends AbstractRegistry<ServerDataModel> {
    private static final String FILE_NAME = "misc.dgdat";

    public void put(String row, String column, String value) {
        // Remove the value if it exists already
        remove(row, column);

        // Create and save the timed value
        ServerDataModel timedData = new ServerDataModel();
        timedData.generateId();
        timedData.setDataType(ServerDataModel.DataType.PERSISTENT);
        timedData.setRow(row);
        timedData.setColumn(column);
        timedData.setValue(value);
        register(timedData);
    }

    public void put(String row, String column, boolean value) {
        // Remove the value if it exists already
        remove(row, column);

        // Create and save the timed value
        ServerDataModel timedData = new ServerDataModel();
        timedData.generateId();
        timedData.setDataType(ServerDataModel.DataType.PERSISTENT);
        timedData.setRow(row);
        timedData.setColumn(column);
        timedData.setValue(value);
        register(timedData);
    }

    public void put(String row, String column, Number value) {
        // Remove the value if it exists already
        remove(row, column);

        // Create and save the timed value
        ServerDataModel timedData = new ServerDataModel();
        timedData.generateId();
        timedData.setDataType(ServerDataModel.DataType.PERSISTENT);
        timedData.setRow(row);
        timedData.setColumn(column);
        timedData.setValue(value);
        register(timedData);
    }

    /*
     * Timed value
     */
    public void put(String row, String column, Object value, long time, TimeUnit unit) {
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

    public boolean contains(String row, String column) {
        return find(row, column) != null;
    }

    public Object get(String row, String column) {
        return find(row, column).getValue();
    }

    public long getExpiration(String row, String column) throws NullPointerException {
        return find(row, column).getExpiration();
    }

    ServerDataModel find(String row, String column) {
        if (findByRow(row) == null) return null;

        for (ServerDataModel data : findByRow(row))
            if (data.getColumn().equals(column)) return data;

        return null;
    }

    Set<ServerDataModel> findByRow(final String row) {
        return getRegistered().stream().filter(model -> model.getRow().equals(row)).collect(Collectors.toSet());
    }

    public void remove(String row, String column) {
        if (find(row, column) != null) unregister(find(row, column));
    }

    /**
     * Clears all expired timed value.
     */
    public void clearExpired() {
        getRegistered().stream().filter(model -> ServerDataModel.DataType.TIMED.equals(model.getDataType()) && model.getExpiration() <= System.currentTimeMillis()).collect(Collectors.toList()).forEach(this::unregister);
    }

    @Override
    public ServerDataModel valueFromData(String stringKey, JsonSection data) {
        return new ServerDataModel(stringKey, data);
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }
}
