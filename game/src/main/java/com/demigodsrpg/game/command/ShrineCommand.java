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

package com.demigodsrpg.game.command;

import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.model.PlayerModel;
import com.demigodsrpg.data.model.ShrineModel;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.gui.ShrineGUI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ShrineCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }
        Player player = (Player) sender;

        if (args.length < 1) {
            try {
                Inventory inventory = new ShrineGUI(player).getInventory();
                if (inventory == null) {
                    player.sendMessage(ChatColor.YELLOW + "You don't have any shrines yet!");
                    return CommandResult.QUIET_ERROR;
                }
                player.openInventory(inventory);
            } catch (Exception oops) {
                oops.printStackTrace();
                return CommandResult.ERROR;
            }

            return CommandResult.SUCCESS;
        } else if ("invite".equalsIgnoreCase(args[0]) || "uninvite".equalsIgnoreCase(args[0])) {
            if (args.length < 3) {
                return CommandResult.INVALID_SYNTAX;
            }

            String inviteeName = args[1];
            String shrineName = args[2];
            if (!shrineName.startsWith("#")) {
                shrineName = "#" + shrineName;
            }

            PlayerModel invitee = DGData.PLAYER_R.fromName(inviteeName);
            ShrineModel shrine = DGData.SHRINE_R.fromId(shrineName);

            PlayerModel inviter = DGData.PLAYER_R.fromPlayer(player);

            boolean invite = "invite".equalsIgnoreCase(args[0]);

            if (invitee == null) {
                player.sendMessage(ChatColor.RED + "That player has not joined this server yet.");
                return CommandResult.QUIET_ERROR;
            }
            if (!invite && !invitee.getShrineWarps().contains(shrineName)) {
                player.sendMessage(ChatColor.RED + "That player has never been invited to that shrine.");
                return CommandResult.QUIET_ERROR;
            } else if (invite && !invitee.getFamily().equals(inviter.getFamily())) {
                player.sendMessage(ChatColor.RED + "That player is not in the same faction as you.");
                return CommandResult.QUIET_ERROR;
            }
            if (shrine == null || !inviter.getMojangId().equals(shrine.getOwnerMojangId())) {
                player.sendMessage(ChatColor.RED + "That shrine is not yours.");
                return CommandResult.QUIET_ERROR;
            }

            if (invite) {
                invitee.addShrineWarp(shrine);
                player.sendMessage(ChatColor.YELLOW + "Invite sent to " + invitee.getLastKnownName() + ".");
            } else {
                invitee.removeShrineWarp(shrine);
                player.sendMessage(ChatColor.YELLOW + "Uninvited " + invitee.getLastKnownName() + ".");
            }
            OfflinePlayer inviteePlayer = invitee.getOfflinePlayer();
            if (inviteePlayer.isOnline()) {
                inviteePlayer.getPlayer().sendMessage(invite ? ChatColor.YELLOW + player.getName() + " has invited you to a shrine." :
                        ChatColor.YELLOW + player.getName() + " has uninvited you from a shrine.");
            }

            return CommandResult.SUCCESS;
        }

        return CommandResult.INVALID_SYNTAX;
    }
}
