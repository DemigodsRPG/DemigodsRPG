package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.JsonSection;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerRegistry extends AbstractRegistry<PlayerModel> {
    private static final String FILE_NAME = "players.dgc";

    @Deprecated
    public PlayerModel fromName(final String name) {
        return Iterables.find(getRegistered(), new Predicate<PlayerModel>() {
            @Override
            public boolean apply(PlayerModel model) {
                return model.getLastKnownName().equalsIgnoreCase(name);
            }
        }, null);
    }

    public PlayerModel fromPlayer(Player player) {
        PlayerModel found = fromId(player.getUniqueId().toString());
        if (found == null) {
            found = new PlayerModel(player);
            register(found);
        }
        return found;
    }

    public Set<OfflinePlayer> getOfflinePlayers() {
        Set<OfflinePlayer> offlinePlayers = new HashSet<>();
        for (PlayerModel model : getRegistered()) {
            offlinePlayers.add(model.getOfflinePlayer());
        }
        return offlinePlayers;
    }

    public Set<PlayerModel> getOnlineInAlliance(Faction alliance) {
        Set<PlayerModel> players = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerModel model = fromPlayer(player);
            if (model.getFaction().equals(alliance)) {
                players.add(model);
            }
        }
        return players;
    }

    public Collection<PlayerModel> fromAspect(final Aspect aspect) {
        return Collections2.filter(getRegistered(), new Predicate<PlayerModel>() {
            @Override
            public boolean apply(PlayerModel model) {
                return model.getAspects().contains(aspect.getName());
            }
        });
    }

    public List<String> getNameStartsWith(final String name) {
        List<String> names = new ArrayList<>();
        for (PlayerModel model : getRegistered()) {
            if (TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - model.getLastLoginTime()) < 3
                    && model.getLastKnownName().toLowerCase().startsWith(name.toLowerCase())) {
                names.add(model.getLastKnownName());
            }
        }
        return names;
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }

    @Override
    public PlayerModel valueFromData(String stringKey, JsonSection data) {
        return new PlayerModel(stringKey, data);
    }
}
