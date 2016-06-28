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

package com.demigodsrpg.command;

import com.demigodsrpg.DGData;
import com.demigodsrpg.command.type.BaseCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class FamilyCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }

        Player player = (Player) sender;
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        if (Family.NEUTRAL.equals(model.getFamily()) || Family.EXCOMMUNICATED.equals(model.getFamily())) {
            player.sendMessage(ChatColor.RED + "You aren't in a family.");
            return CommandResult.QUIET_ERROR;
        }

        if (DGData.SERVER_R.contains("family_chat", player.getUniqueId().toString())) {
            DGData.SERVER_R.remove("family_chat", player.getUniqueId().toString());
            player.sendMessage(ChatColor.YELLOW + "You just disabled family chat.");
        } else {
            DGData.SERVER_R.put("family_chat", player.getUniqueId().toString(), true);
            player.sendMessage(ChatColor.YELLOW + "You just enabled family chat.");
        }
        return CommandResult.SUCCESS;
    }
}
