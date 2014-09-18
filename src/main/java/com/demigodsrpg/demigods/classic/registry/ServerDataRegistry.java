package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.model.ServerDataModel;
import com.demigodsrpg.demigods.classic.util.JsonSection;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ServerDataRegistry extends AbstractRegistry<ServerDataModel> {
    private static final String FILE_NAME = "misc.dgc";

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

    public void put(String row, String column, Boolean value) {
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

    public Long getExpiration(String row, String column) throws NullPointerException {
        return find(row, column).getExpiration();
    }

    public ServerDataModel find(String row, String column) {
        if (findByRow(row) == null) return null;

        for (ServerDataModel data : findByRow(row))
            if (data.getColumn().equals(column)) return data;

        return null;
    }

    public Set<ServerDataModel> findByRow(final String row) {
        return Sets.newHashSet(Collections2.filter(getRegistered(), new Predicate<ServerDataModel>() {
            @Override
            public boolean apply(ServerDataModel ServerDataModel) {
                return ServerDataModel.getRow().equals(row);
            }
        }));
    }

    public void remove(String row, String column) {
        if (find(row, column) != null) unregister(find(row, column));
    }

    /**
     * Clears all expired timed value.
     */
    public void clearExpired() {
        for (ServerDataModel data : Collections2.filter(getRegistered(), new Predicate<ServerDataModel>() {
            @Override
            public boolean apply(ServerDataModel data) {
                return ServerDataModel.DataType.TIMED.equals(data.getDataType()) && data.getExpiration() <= System.currentTimeMillis();
            }
        }))
            unregister(data);
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
