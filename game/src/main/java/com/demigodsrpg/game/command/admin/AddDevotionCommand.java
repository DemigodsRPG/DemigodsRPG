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

package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.model.PlayerModel;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddDevotionCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 3) {
            try {
                Player p = DGData.PLAYER_R.fromName(args[0]).getOfflinePlayer().getPlayer();
                double amount = Double.parseDouble(args[2]);
                Aspect aspect = Aspects.valueOf(args[1].toUpperCase());
                if (!DGData.PLAYER_R.fromPlayer(p).getAspects().contains(aspect.getGroup().getName() + " " + aspect.getTier().name())) {
                    sender.sendMessage(ChatColor.RED + "The player you are accessing does not have that aspect.");
                    return CommandResult.QUIET_ERROR;
                }

                DGData.PLAYER_R.fromPlayer(p).setExperience(aspect, DGData.PLAYER_R.fromPlayer(p).getExperience(aspect) + amount, true);

                sender.sendMessage(ChatColor.YELLOW + "You added " + amount + " devotion to " + p.getName() + " to the " + aspect.getGroup().getName() + " " + aspect.getTier().name() + " aspect.");
            } catch (Exception ignored) {
                sender.sendMessage(ChatColor.RED + "Invalid syntax! /AddDevotion [Name, Aspect, Amount]");
                return CommandResult.QUIET_ERROR;
            }
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
}
