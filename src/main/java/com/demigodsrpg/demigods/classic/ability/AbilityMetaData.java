package com.demigodsrpg.demigods.classic.ability;

import com.demigodsrpg.demigods.classic.deity.Deity;

import java.lang.reflect.Method;

public class AbilityMetaData {
    // -- PRIVATE FIELDS -- //

    private final Deity deity;
    private final Method method;
    private final Ability ability;

    // -- CONSTRUCTOR -- //

    public AbilityMetaData(Deity deity, Method method, Ability ability) {
        this.deity = deity;
        this.method = method;
        this.ability = ability;
    }

    // -- GETTERS -- //

    public Deity getDeity() {
        return deity;
    }

    public String getName() {
        return ability.name();
    }

    public String getCommand() {
        return ability.command();
    }

    public String[] getInfo() {
        return ability.info();
    }

    public Ability.Type getType() {
        return ability.type();
    }

    public double getCost() {
        return ability.cost();
    }

    public long getDelay() {
        return ability.delay();
    }

    public long getCooldown() {
        return ability.cooldown();
    }

    public Method getMethod() {
        return method;
    }

    public Ability getAbility() {
        return ability;
    }
}
