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
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.command.type.AdminPlayerCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveDevotionCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 3) {

            PlayerModel m;
            try {
                Player p = DGData.PLAYER_R.fromName(args[0]).getOfflinePlayer().getPlayer();
                double amount = Double.parseDouble(args[2]);
                m = DGData.PLAYER_R.fromPlayer(p);
                Aspect aspect = Aspects.valueOf(args[1].toUpperCase());
                if (!m.getAspects().contains(aspect.name())) {
                    sender.sendMessage(ChatColor.RED + "The player you are accessing does not have that aspect.");
                    return CommandResult.QUIET_ERROR;
                }
                double newAmount = m.getExperience(aspect) - amount;
                if (newAmount < 0) newAmount = 0;

                m.setExperience(aspect, newAmount, true);

                sender.sendMessage(ChatColor.YELLOW + "You removed " + amount + " devotion from " + p.getName() + " in the aspect group " + aspect.getGroup().getName() + ".");
            } catch (Exception ignored) {
                sender.sendMessage(ChatColor.RED + "Invalid syntax! /RemoveDevotion [Name, Deity, Amount]");
                return CommandResult.QUIET_ERROR;
            }
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
}