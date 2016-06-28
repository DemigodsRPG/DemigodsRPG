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

package com.demigodsrpg.chitchat;

import com.demigodsrpg.DGData;
import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FactionIdTag extends PlayerTag {
    @Override
    public String getName() {
        return "faction_id";
    }

    @Override
    public String getFor(Player player) {
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);
        String symbol = model.getFamily().getChatSymbol();
        return ChatColor.DARK_GRAY + "[" + model.getFamily().getColor() + symbol + ChatColor.DARK_GRAY + "]" +
                ChatColor.RESET;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
