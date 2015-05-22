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

package com.demigodsrpg.game.integration.chitchat;

import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.model.PlayerModel;
import com.demigodsrpg.families.data.Family;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FactionChatTag extends PlayerTag {
    @Override
    public String getName() {
        return "faction_chat";
    }

    @Override
    public String getFor(Player player) {
        if (isInChat(player)) {
            return ChatColor.DARK_GRAY + "[.]";
        }
        return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "!" + ChatColor.DARK_GRAY + "]";
    }

    @Override
    public boolean cancelRedis(Player player) {
        return isInChat(player);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    private boolean isInChat(Player player) {
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);
        return DGData.SERVER_R.contains("faction_chat", player.getUniqueId().toString()) && !Family.NEUTRAL.equals(model.getFamily()) && !Family.EXCOMMUNICATED.equals(model.getFamily());
    }
}
