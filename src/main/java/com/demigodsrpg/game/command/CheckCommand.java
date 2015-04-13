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

import com.censoredsoftware.library.bukkitutil.ColorUtil;
import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CheckCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }
        Player player = (Player) sender;
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        player.sendMessage(StringUtil2.chatTitle("Player Stats"));
        if (model.getGod().isPresent() && model.getHero().isPresent()) {
            player.sendMessage(ChatColor.YELLOW + "You are the offspring of " + model.getGod().get().getName() + " and " + model.getHero().get().getName() + "."); // TODO Colors
        }
        player.sendMessage(ChatColor.YELLOW + "You are allied with the " + StringUtil2.beautify(model.getFaction().getName()) + " faction.");
        player.sendMessage(ChatColor.YELLOW + "You have " + ColorUtil.getColor(player.getHealth(), player.getMaxHealth()) + ChatColor.ITALIC + player.getHealth() + " / " + player.getMaxHealth() + ChatColor.YELLOW + " health.");
        if (!model.getAspects().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String deityName : model.getAspects()) {
                Aspect aspect = Aspects.valueOf(deityName);
                builder.append(aspect.getGroup().getColor()).append(aspect.name()).append(ChatColor.RESET).append(", ");
            }
            String aspects = builder.toString();
            aspects = aspects.substring(0, aspects.length() - 4) + ".";
            player.sendMessage("Your aspects: " + aspects);
        }
        player.sendMessage("Favor: " + model.getFavor());
        player.sendMessage("Total Devotion: " + model.getTotalExperience());
        player.sendMessage("Number of ascensions: " + model.getLevel());
        player.sendMessage("Use " + ChatColor.ITALIC + "/binds" + ChatColor.RESET + " for a list of all ability binds.");

        return CommandResult.SUCCESS;
    }
}
