package com.demigodsrpg.demigods.classic.command;

import com.censoredsoftware.library.util.CommonSymbol;
import com.censoredsoftware.library.util.MapUtil2;
import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.command.type.BaseCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
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

        if (DGClassic.TRIBUTE_R.getTributeValuesMap().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "There are currently no tributes on record.");
            return CommandResult.QUIET_ERROR;
        }

        // Send header
        sender.sendMessage(StringUtil2.chatTitle("Current High Value Tributes"));
        sender.sendMessage(" ");

        for (Map.Entry<Material, Integer> entry : MapUtil2.sortByValue(DGClassic.TRIBUTE_R.getTributeValuesMap(), true).entrySet()) {
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
            sender.sendMessage(ChatColor.GRAY + "The " + (player.getItemInHand().getAmount() == 1 ? "item in your hand is" : "items in your hand are") + " worth " + ChatColor.GREEN + DGClassic.TRIBUTE_R.getValue(player.getItemInHand()) + ChatColor.GRAY + " in total.");
        }

        return CommandResult.SUCCESS;
    }
}
