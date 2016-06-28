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

package com.demigodsrpg.command.admin;

import com.demigodsrpg.DGData;
import com.demigodsrpg.command.type.AdminPlayerCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetFamilyCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }
        if (DGData.PLAYER_R.fromPlayer((Player) sender).getAdminMode()) {
            if (args.length == 2) {
                PlayerModel player = DGData.PLAYER_R.fromName(args[0]);
                Family family = DGData.FAMILY_R.familyFromName(args[1].toUpperCase());
                if (player == null || family == null) {
                    sender.sendMessage(ChatColor.RED + "Wrong player or faction! Please try a little harder.");
                    return CommandResult.QUIET_ERROR;
                }
                player.setFamily(family);
                sender.sendMessage(ChatColor.YELLOW + player.getLastKnownName() + " has been set to the " + family.getName() + " faction.");
                return CommandResult.SUCCESS;
            }
            return CommandResult.INVALID_SYNTAX;
        }
        return CommandResult.NO_PERMISSIONS;
    }
}
