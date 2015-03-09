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

package com.demigodsrpg.game.listener;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(event.getPlayer());

        event.getPlayer().sendMessage("Welcome!");
        if (model.getAspects().isEmpty()) {
            event.getPlayer().sendMessage("TEMP: " + ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/aspect claim" + ChatColor.GRAY + " to claim an aspect.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(event.getPlayer());
        if (DGGame.SERVER_R.contains("faction_chat", event.getPlayer().getUniqueId().toString()) && !Faction.NEUTRAL.equals(model.getFaction()) && !Faction.EXCOMMUNICATED.equals(model.getFaction())) {
            event.getRecipients().clear();
            Set<PlayerModel> playerModelSet = DGGame.PLAYER_R.getOnlineInAlliance(model.getFaction());
            for (PlayerModel playerModel : playerModelSet) {
                event.getRecipients().add(playerModel.getOfflinePlayer().getPlayer());
            }
        }
    }
}
