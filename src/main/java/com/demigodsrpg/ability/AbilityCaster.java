package com.demigodsrpg.ability;

import org.bukkit.*;

import java.util.List;
import java.util.Optional;

public interface AbilityCaster {
    List<String> getAspects();

    boolean getOnline();

    String getMojangId();

    OfflinePlayer getOfflinePlayer();

    Optional<Location> getLocation();

    Optional<AbilityMetaData> getBound(Material material);

    Optional<Material> getBound(AbilityMetaData ability);

    Optional<Material> getBound(String abilityCommand);

    void bind(AbilityMetaData ability, Material material);

    void bind(String abilityCommand, Material material);

    void unbind(AbilityMetaData ability);

    void unbind(String abilityCommand);

    void unbind(Material material);

    double getFavor();

    void setFavor(double favor);
}
