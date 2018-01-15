package com.demigodsrpg.registry;

import com.demigodsrpg.ability.AbilityCasterProvider;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.deity.Deity;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.datasection.DataSection;
import com.demigodsrpg.util.datasection.Registry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public interface PlayerRegistry extends Registry<PlayerModel>, AbilityCasterProvider {
    String NAME = "players";

    @Deprecated
    default PlayerModel fromName(final String name) {
        Optional<PlayerModel> player =
                getRegisteredData().values().stream().filter(model -> model.getLastKnownName().equalsIgnoreCase(name))
                        .findFirst();
        return player.orElse(null);
    }

    default PlayerModel fromPlayer(OfflinePlayer player) {
        Optional<PlayerModel> found = fromKey(player.getUniqueId().toString());
        if (!found.isPresent()) {
            PlayerModel model = new PlayerModel(player);
            return register(model);
        }
        return found.get();
    }

    @Deprecated
    default PlayerModel fromId(UUID id) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(id);
        return fromPlayer(player);
    }

    @Deprecated
    default PlayerModel fromId(String id) {
        return fromId(UUID.fromString(id));
    }

    default Set<OfflinePlayer> getOfflinePlayers() {
        return getRegisteredData().values().stream().map(PlayerModel::getOfflinePlayer).collect(Collectors.toSet());
    }

    default Set<PlayerModel> getOnlineInAlliance(Family family) {
        Set<PlayerModel> players = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerModel model = (PlayerModel) fromPlayer(player);
            if (model.getFamily().equals(family)) {
                players.add(model);
            }
        }
        return players;
    }

    default Set<PlayerModel> fromDeity(Deity deity) {
        return getRegisteredData().values().parallelStream().filter(model -> model.hasDeity(deity))
                .collect(Collectors.toSet());
    }

    default List<PlayerModel> fromAspect(final Aspect aspect) {
        return getRegisteredData().values().stream().filter(model -> model.getAspects().contains(aspect.name()))
                .collect(Collectors.toList());
    }

    default List<String> getNameStartsWith(final String name) {
        return getRegisteredData().values().stream()
                .filter(model -> TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - model.getLastLoginTime()) < 3
                        && model.getLastKnownName().toLowerCase().startsWith(name.toLowerCase()))
                .map(PlayerModel::getLastKnownName).collect(Collectors.toList());
    }

    default boolean hasAspect(Player player, Aspect aspect) {
        return fromPlayer(player).hasAspect(aspect);
    }

    @Override
    default PlayerModel fromDataSection(String stringKey, DataSection data) {
        return new PlayerModel(stringKey, data);
    }
}
