/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
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

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CleanseCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        // Check for no args
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "To cleanse all, do /cleanse all");
            return CommandResult.SUCCESS;
        }
        // Check for only 1 arg
        else if (args.length == 1) {
            // This subcommand doesn't allow the console
            if (sender instanceof ConsoleCommandSender) {
                return CommandResult.PLAYER_ONLY;
            }

            // Get the player object
            Player player = (Player) sender;
            PlayerModel playerModel = DGGame.PLAYER_R.fromPlayer(player);

            // Check that the player model is not null
            if (playerModel == null) {
                sender.sendMessage(ChatColor.DARK_RED + "Something is wrong with your player data. Try to re-log.");
                return CommandResult.QUIET_ERROR;
            }

            // Do '/forsake all'
            if ("all".equalsIgnoreCase(args[0])) {
                // Loop over the player's deities
                for (String deityName : playerModel.getAspects()) {
                    // Get the deity object
                    Aspect aspect = Aspects.valueOf(deityName.toUpperCase());
                    if (aspect == null) {
                        sender.sendMessage(ChatColor.RED + deityName + " does not exist, something is wrong with the player data...");
                        return CommandResult.QUIET_ERROR;
                    }

                    playerModel.removeAspect(aspect);
                    sender.sendMessage(ChatColor.YELLOW + "You have cleansed " + aspect.getGroup().getName());
                }

                // Set the alliance to neutral
                playerModel.setFaction(Faction.NEUTRAL);
                return CommandResult.SUCCESS;
            } else {
                // TODO Check for individual deity forsake requests.
                sender.sendMessage(ChatColor.YELLOW + "To cleanse all do /cleanse all");
                return CommandResult.SUCCESS;
            }
        }
        // Handle admin command to forsake other players
        else if (args.length == 2) {
            // Check if the sender has permission to do the forsaking.
            if (!sender.hasPermission("demigods.admin.cleanse")) {
                return CommandResult.NO_PERMISSIONS;
            }

            // Handle '/forsake <player> all' subcommand
            if ("all".equalsIgnoreCase(args[1])) {
                // Get the target's player model
                PlayerModel playerModel = DGGame.PLAYER_R.fromName(args[0]);
                if (playerModel == null) {
                    sender.sendMessage(ChatColor.DARK_RED + "No such player! Please try a little harder.");
                    return CommandResult.QUIET_ERROR;
                }

                // Loop over the target's deities
                for (String deityName : playerModel.getAspects()) {
                    // Get the deity from the name
                    Aspect aspect = Aspects.valueOf(deityName);
                    if (aspect == null) {
                        continue;
                    }

                    playerModel.removeAspect(aspect);
                }

                // Set the target's alliance to neutral.
                playerModel.setFaction(Faction.NEUTRAL);
                sender.sendMessage(ChatColor.YELLOW + "You have removed all aspects from " + playerModel.getLastKnownName());

                return CommandResult.SUCCESS;
            } else {
                // TODO Check for individual deity forsake requests.
                return CommandResult.INVALID_SYNTAX;
            }
        }

        // Something went wrong...
        return CommandResult.QUIET_ERROR;
    }
}
