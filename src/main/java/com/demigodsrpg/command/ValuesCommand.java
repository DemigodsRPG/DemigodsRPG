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
import com.demigodsrpg.util.misc.CommonSymbol;
import com.demigodsrpg.util.misc.MapUtil2;
import com.demigodsrpg.util.misc.StringUtil2;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class ValuesCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        // Define variables
        Player player = (Player) sender;
        int count = 0;

        if (DGData.TRIBUTE_R.getTributeValuesMap().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "There are currently no tributes on record.");
            return CommandResult.QUIET_ERROR;
        }

        // Send header
        sender.sendMessage(StringUtil2.chatTitle("Current Top Value Tributes"));
        sender.sendMessage(" ");

        for (Map.Entry<Material, Integer> entry : MapUtil2.sortByValue(DGData.TRIBUTE_R.getTributeValuesMap(), true).entrySet()) {
            // Handle count
            if (count >= 10) break;
            count++;

            // Display value
            sender.sendMessage(ChatColor.GRAY + " " + CommonSymbol.RIGHTWARD_ARROW + " " + ChatColor.YELLOW + StringUtil2.beautify(entry.getKey().name()) + ChatColor.GRAY + " (currently worth " + ChatColor.GREEN + entry.getValue() + ChatColor.GRAY + " per item)");
        }

        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Values are constantly changing based on how players");
        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "tribute, so check back often!");

        if (!Material.AIR.equals(player.getItemInHand().getType())) {
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GRAY + "The " + (player.getItemInHand().getAmount() == 1 ? "item in your hand is" : "items in your hand are") + " worth " + ChatColor.GREEN + DGData.TRIBUTE_R.getValue(player.getItemInHand()) + ChatColor.GRAY + " in total.");
        }

        return CommandResult.SUCCESS;
    }
}
