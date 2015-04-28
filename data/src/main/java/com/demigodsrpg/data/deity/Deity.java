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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Deity extends AbstractPersistentModel<String> {
    // -- DEITY META -- //

    private DeityType deityType;
    private String name;
    private Gender gender;
    private String[] pronouns;
    private Faction faction;
    private List<Aspect.Group> aspectGroups;

    // -- CONSTRUCTORS -- //

    public Deity(DeityType deityType, String name, Gender gender, Faction faction, Aspect.Group... aspectGroups) {
        this(deityType, name, gender, faction, Arrays.asList(aspectGroups));
    }

    public Deity(DeityType deityType, String name, Gender gender, Faction faction, List<Aspect.Group> aspectGroups) {
        this.deityType = deityType;
        this.name = name;
        this.gender = gender;
        this.faction = faction;
        this.aspectGroups = aspectGroups;
        decidePronouns();
    }

    public Deity(String stringKey, DataSection conf) {
        name = stringKey;
        gender = Gender.valueOf(conf.getString("gender"));
        deityType = DeityType.valueOf(conf.getString("type"));
        faction = DGData.FACTION_R.factionFromName("faction");
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

    public Gender getGender() {
        return gender;
    }

    public String[] getPronouns() {
        return pronouns;
    }

    public Faction getFaction() {
        return faction;
    }

    public List<Aspect.Group> getAspectGroups() {
        return aspectGroups;
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
