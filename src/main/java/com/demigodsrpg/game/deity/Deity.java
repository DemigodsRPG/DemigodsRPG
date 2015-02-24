package com.demigodsrpg.game.deity;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.AbstractPersistentModel;
import com.demigodsrpg.game.util.JsonSection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Deity extends AbstractPersistentModel<String> {
    // -- DEBUG DEITIES -- //

    public static final Deity LOREM = new Deity(DeityType.GOD, "Lorem", Faction.NEUTRAL, Arrays.asList(Groups.WATER_ASPECT));
    public static final Deity IPSUM = new Deity(DeityType.HERO, "Ipsum", Faction.NEUTRAL, Arrays.asList(Groups.BLOODLUST_ASPECT));

    // -- DEITY META -- //

    private DeityType deityType;
    private String name;
    private Faction faction;
    private List<Aspect.Group> aspectGroups;

    // -- CONSTRUCTORS -- //

    public Deity(DeityType deityType, String name, Faction faction, List<Aspect.Group> aspectGroups) {
        this.deityType = deityType;
        this.name = name;
        this.faction = faction;
        this.aspectGroups = aspectGroups;
    }

    public Deity(String stringKey, JsonSection conf) {
        name = stringKey;
        deityType = DeityType.valueOf(conf.getString("type"));
        faction = DGGame.FACTION_R.factionFromName("faction");
        aspectGroups = conf.getStringList("aspect-groups").stream().map(Groups::valueOf).collect(Collectors.toList());
    }

    // -- GETTERS -- //

    @Override
    public String getPersistentId() {
        return name;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", deityType.name());
        map.put("faction", faction.getName());
        map.put("aspect-groups", aspectGroups.stream().map(Aspect.Group::getName).collect(Collectors.toList()));
        return map;
    }

    public DeityType getDeityType() {
        return deityType;
    }

    public String getName() {
        return name;
    }

    public Faction getFaction() {
        return faction;
    }

    public List<Aspect.Group> getAspectGroups() {
        return aspectGroups;
    }
}
