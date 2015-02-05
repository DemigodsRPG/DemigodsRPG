package com.demigodsrpg.game.ability;

import com.demigodsrpg.game.aspect.Aspect;

import java.lang.reflect.Method;

public class AbilityMetaData {
    // -- PRIVATE FIELDS -- //

    private final String aspect;
    private final Method method;
    private final Ability ability;

    // -- CONSTRUCTOR -- //

    public AbilityMetaData(Aspect aspect, Method method, Ability ability) {
        this.aspect = aspect.name();
        this.method = method;
        this.ability = ability;
    }

    // -- GETTERS -- //

    public Aspect getAspect() {
        return Aspect.valueOf(aspect);
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
