package com.demigodsrpg.game.model;

public interface Model<P> {
    Type getType();

    // -- ENUMS -- //
    public enum Type {
        TRANSIENT, PERSISTENT
    }
}
