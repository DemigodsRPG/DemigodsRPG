package com.demigodsrpg.demigods.classic.command;

import com.demigodsrpg.demigods.classic.command.type.BaseCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.gui.ShrineGUI;
import org.bukkit.ChatColor;
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

        try {
            Player player = (Player) sender;
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
    }
}
