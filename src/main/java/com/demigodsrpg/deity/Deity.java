package com.demigodsrpg.deity;

import com.demigodsrpg.DGData;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.util.datasection.AbstractPersistentModel;
import com.demigodsrpg.util.datasection.DataSection;

import java.util.*;
import java.util.stream.Collectors;

public class Deity extends AbstractPersistentModel<String> {
    // -- DEITY META -- //

    private DeityType deityType;
    private String name;
    private Gender gender;
    private String[] pronouns;
    private List<Family> families;
    private List<Aspect.Group> aspectGroups;

    // -- CONSTRUCTORS -- //

    public Deity(DeityType deityType, String name, Gender gender, Family family, Aspect.Group... aspectGroups) {
        this(deityType, name, gender, family, Arrays.asList(aspectGroups));
    }

    public Deity(DeityType deityType, String name, Gender gender, Family family, List<Aspect.Group> aspectGroups) {
        this.deityType = deityType;
        this.name = name;
        this.gender = gender;
        this.families = new ArrayList<>();
        this.families.add(family);
        this.aspectGroups = aspectGroups;
        decidePronouns();
    }

    public Deity(String stringKey, DataSection conf) {
        name = stringKey;
        gender = Gender.valueOf(conf.getString("gender"));
        deityType = DeityType.valueOf(conf.getString("type"));
        this.families = new ArrayList<>();
        for (Object familyName : conf.getList("families", new ArrayList<>())) {
            Family family = DGData.FAMILY_R.familyFromName(familyName.toString());
            if (family != null) {
                families.add(family);
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
        map.put("families", families.stream().map(Family::getName).collect(Collectors.toList()));
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

    public List<Family> getFamilies() {
        return families;
    }

    public List<Aspect.Group> getAspectGroups() {
        return aspectGroups;
    }

    // -- MUTATORS -- //

    public void addFamily(Family family) {
        families.add(family);
    }

    public void removeFamily(Family family) {
        families.remove(family);
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
