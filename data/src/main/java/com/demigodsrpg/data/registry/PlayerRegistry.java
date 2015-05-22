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

package com.demigodsrpg.data.registry;

import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.data.deity.Deity;
import com.demigodsrpg.data.model.PlayerModel;
import com.demigodsrpg.families.data.Family;
import com.demigodsrpg.util.datasection.DataSection;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PlayerRegistry extends AbstractDemigodsDataRegistry<PlayerModel> {
    private static final String FILE_NAME = "players";

    @Deprecated
    public PlayerModel fromName(final String name) {
        Optional<PlayerModel> player = getRegistered().stream().filter(model -> model.getLastKnownName().equalsIgnoreCase(name)).findFirst();
        if (player.isPresent()) {
            return player.get();
        }
        return null;
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
        return getRegistered().stream().map(PlayerModel::getOfflinePlayer).collect(Collectors.toSet());
    }

    public Set<PlayerModel> getOnlineInAlliance(Family family) {
        Set<PlayerModel> players = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerModel model = fromPlayer(player);
            if (model.getFamily().equals(family)) {
                players.add(model);
            }
        }
        return players;
    }

    public Set<PlayerModel> fromDeity(Deity deity) {
        return getRegistered().parallelStream().filter(model -> model.hasDeity(deity)).collect(Collectors.toSet());
    }

    public Collection<PlayerModel> fromAspect(final Aspect aspect) {
        return getRegistered().stream().filter(model -> model.getAspects().contains(aspect.name())).collect(Collectors.toList());
    }

    public List<String> getNameStartsWith(final String name) {
        return getRegistered().stream().filter(model -> TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - model.getLastLoginTime()) < 3
                && model.getLastKnownName().toLowerCase().startsWith(name.toLowerCase())).map(PlayerModel::getLastKnownName).collect(Collectors.toList());
    }

    public boolean hasAspect(Player player, Aspect aspect) {
        return fromPlayer(player).hasAspect(aspect);
    }

    @Override
    public String getName() {
        return FILE_NAME;
    }

    @Override
    public PlayerModel valueFromData(String stringKey, DataSection data) {
        return new PlayerModel(stringKey, data);
    }
}
