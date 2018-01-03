package com.demigodsrpg.ability;

import java.util.concurrent.TimeUnit;

public interface CooldownHandler {
    default boolean hasDelay(AbilityCaster model, AbilityMetaData ability) {
        return contains(model.getMojangId(), ability.getName() + ":delay");
    }

    default boolean hasCooldown(AbilityCaster model, AbilityMetaData ability) {
        return contains(model.getMojangId(), ability.getName() + ":cooldown");
    }

    default void delay(AbilityCaster model, AbilityMetaData ability) {
        put(model.getMojangId(), ability.getName() + ":delay", true, ability.getDelay(), TimeUnit.MILLISECONDS);
    }

    default void cooldown(AbilityCaster model, AbilityMetaData ability) {
        put(model.getMojangId(), ability.getName() + ":cooldown", true, ability.getCooldown(), TimeUnit.MILLISECONDS);
    }

    boolean contains(String row, String column);

    void put(String row, String column, Object value, long time, TimeUnit unit);
}
