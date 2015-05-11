/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.data.deity;

import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.model.AbstractPersistentModel;
import com.demigodsrpg.util.DataSection;

import java.util.*;
import java.util.stream.Collectors;

public class Deity extends AbstractPersistentModel<String> {
    // -- DEITY META -- //

    private DeityType deityType;
    private String name;
    private Gender gender;
    private String[] pronouns;
    private List<Faction> factions;
    private List<Aspect.Group> aspectGroups;

    // -- CONSTRUCTORS -- //

    public Deity(DeityType deityType, String name, Gender gender, Faction faction, Aspect.Group... aspectGroups) {
        this(deityType, name, gender, faction, Arrays.asList(aspectGroups));
    }

    public Deity(DeityType deityType, String name, Gender gender, Faction faction, List<Aspect.Group> aspectGroups) {
        this.deityType = deityType;
        this.name = name;
        this.gender = gender;
        this.factions = new ArrayList<>();
        this.factions.add(faction);
        this.aspectGroups = aspectGroups;
        decidePronouns();
    }

    public Deity(String stringKey, DataSection conf) {
        name = stringKey;
        gender = Gender.valueOf(conf.getString("gender"));
        deityType = DeityType.valueOf(conf.getString("type"));
        this.factions = new ArrayList<>();
        for (Object factionName : conf.getList("factions", new ArrayList<>())) {
            Faction faction = DGData.FACTION_R.factionFromName(factionName.toString());
            if (faction != null) {
                factions.add(faction);
            }
        }
        aspectGroups = conf.getStringList("aspect-groups").stream().map(Groups::valueOf).collect(Collectors.toList());
        decidePronouns();
    }

    // -- GETTERS -- //

    @Override
    public String getPersistentId() {
        return name;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("gender", gender.name());
        map.put("type", deityType.name());
        map.put("factions", factions.stream().map(Faction::getName).collect(Collectors.toList()));
        map.put("aspect-groups", aspectGroups.stream().map(Aspect.Group::getName).collect(Collectors.toList()));
        return map;
    }

    public DeityType getDeityType() {
        return deityType;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public String[] getPronouns() {
        return pronouns;
    }

    public List<Faction> getFactions() {
        return factions;
    }

    public List<Aspect.Group> getAspectGroups() {
        return aspectGroups;
    }

    // -- MUTATORS -- //

    public void addFaction(Faction faction) {
        factions.add(faction);
    }

    public void removeFaction(Faction faction) {
        factions.remove(faction);
    }

    public void addAspectGroup(Aspect.Group group) {
        aspectGroups.add(group);
    }

    public void removeAspectGroup(Aspect.Group group) {
        aspectGroups.remove(group);
    }

    // -- HELPER METHODS -- //

    private void decidePronouns() {
        // Set the pronouns
        switch (gender) {
            case MALE:
                pronouns = new String[]{"he", "him"};
                break;
            case FEMALE:
                pronouns = new String[]{"she", "her"};
                break;
            case EITHER:
                pronouns = new String[]{"they", "them"};
                break;
        }
    }
}
