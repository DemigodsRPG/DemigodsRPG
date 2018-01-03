package com.demigodsrpg.ability;

import org.bukkit.*;

import java.util.List;

public interface AbilityCaster {
    List<String> getAspects();

    boolean getOnline();

    String getMojangId();

    OfflinePlayer getOfflinePlayer();

    Location getLocation();

    AbilityMetaData getBound(Material material);

    Material getBound(AbilityMetaData ability);

    Material getBound(String abilityCommand);

    void bind(AbilityMetaData ability, Material material);

    void bind(String abilityCommand, Material material);

    void unbind(AbilityMetaData ability);

    void unbind(String abilityCommand);

    void unbind(Material material);

    double getFavor();

    void setFavor(double favor);
}
