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

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckPlayerCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        OfflinePlayer p = null;

        if (args.length == 1) {
            try {
                p = DGGame.PLAYER_R.fromName(args[0]).getOfflinePlayer();
            } catch (Exception ignored) {
                sender.sendMessage(ChatColor.RED + "Player is not real, but we appreciate the attempt!");
                return CommandResult.QUIET_ERROR;
            }
            if (p.isOnline()) {
                sendInfo(p.getPlayer(), sender);
            }
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }

    private void sendInfo(Player p, CommandSender s) {
        PlayerModel model = DGGame.PLAYER_R.fromId(p.getUniqueId().toString());
        s.sendMessage(StringUtil2.chatTitle("Player Stats"));
        s.sendMessage(p.getName() + " is an offspring of " + model.getGod() + " and " + model.getHero() + "."); // TODO Colors
        s.sendMessage(p.getName() + " is a member of the " + StringUtil2.beautify(model.getFaction().getName()) + " faction.");
        if (p.isOnline())
            s.sendMessage(p.getName() + " has " + ColorUtil.getColor(p.getPlayer().getHealth(), p.getPlayer().getMaxHealth()) + ChatColor.ITALIC + p.getPlayer().getHealth() + " / " + p.getPlayer().getMaxHealth() + ChatColor.YELLOW + " health.");
        if (!model.getAspects().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String deityName : model.getAspects()) {
                Aspect aspect = Aspects.valueOf(deityName);
                builder.append(aspect.getGroup().getColor()).append(aspect.getGroup()).append(" ").append(aspect.getTier().name()).append(ChatColor.RESET).append(", ");
            }
            String minorDeities = builder.toString();
            minorDeities = minorDeities.substring(0, minorDeities.length() - 4);
            s.sendMessage(p.getName() + " is also allied with: " + minorDeities);
        }
        s.sendMessage("Favor: " + model.getFavor());
        s.sendMessage("Total Devotion: " + model.getTotalExperience());
        s.sendMessage("Number of ascensions: " + model.getLevel());
    }
}
