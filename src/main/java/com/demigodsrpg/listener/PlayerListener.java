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

package com.demigodsrpg.listener;

import com.demigodsrpg.DGData;
import com.demigodsrpg.Setting;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.util.Set;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(event.getPlayer());

        player.setMaxHealth(model.getMaxHealth());
        player.setHealthScale(20.0);
        player.setHealthScaled(true);
        player.setHealth(model.getMaxHealth());

        player.sendMessage("Welcome!");
        if (Setting.DEBUG_DATA) {
            player.sendMessage(ChatColor.RED + "This server is currently in " + ChatColor.BOLD + "Demigods Demo Mode" + ChatColor.RED + ".");
        }
        if (model.getAspects().isEmpty()) {
            // TODO IDK what to do here yet
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(event.getPlayer());

        player.setMaxHealth(model.getMaxHealth());
        player.setHealthScale(20.0);
        player.setHealthScaled(true);
        player.setHealth(model.getMaxHealth());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        PlayerModel model = DGData.PLAYER_R.fromPlayer(event.getPlayer());
        if (DGData.SERVER_R.contains("faction_chat", event.getPlayer().getUniqueId().toString()) && !Family.NEUTRAL.equals(model.getFamily()) && !Family.EXCOMMUNICATED.equals(model.getFamily())) {
            event.getRecipients().clear();
            Set<PlayerModel> playerModelSet = DGData.PLAYER_R.getOnlineInAlliance(model.getFamily());
            for (PlayerModel playerModel : playerModelSet) {
                event.getRecipients().add(playerModel.getOfflinePlayer().getPlayer());
            }
        }
    }
}
