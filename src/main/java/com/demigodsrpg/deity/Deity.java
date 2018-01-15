package com.demigodsrpg.deity;

import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.family.Family;

import java.util.Arrays;
import java.util.List;

public class Deity {

    // -- DEITY META -- //

    private DeityType deityType;
    private String name;
    private Gender gender;
    private String[] pronouns;
    private Family family;
    private List<Aspect.Group> aspects;

    // -- CONSTRUCTORS -- //

    public Deity(DeityType deityType, String name, Gender gender, Family family, Aspect.Group... aspects) {
        this.deityType = deityType;
        this.name = name;
        this.gender = gender;
        this.family = family;
        this.aspects = Arrays.asList(aspects);
        decidePronouns();
    }

    // -- GETTERS -- //

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

    public Family getFamily() {
        return family;
    }

    public List<Aspect.Group> getAspectGroups() {
        return aspects;
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
            case NON_BINARY:
                pronouns = new String[]{"they", "them"};
                break;
        }
    }
}
