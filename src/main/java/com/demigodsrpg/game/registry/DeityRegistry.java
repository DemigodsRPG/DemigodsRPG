package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.deity.Faction;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class DeityRegistry {

    // -- REGISTRY DATA -- //

    private static final Multimap<Faction, Deity> REGISTERED_DEITIES = Multimaps.newListMultimap(new ConcurrentHashMap<>(), () -> new ArrayList<>(0));

    public Deity fromName(String name) {
        return REGISTERED_DEITIES.values().stream().filter(deity -> deity.getName().equals(name)).findAny().get();
    }
}
