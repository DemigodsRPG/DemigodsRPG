package com.demigodsrpg.command.admin;

import com.demigodsrpg.DGData;
import com.demigodsrpg.command.type.BaseCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class CleanseCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        // Check for no args
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "To cleanse all, do /cleanse all");
            return CommandResult.SUCCESS;
        }

        // Get the player object
        Player player = (Player) sender;
        PlayerModel senderModel = DGData.PLAYER_R.fromPlayer(player);

        // Check for only 1 arg
        if (args.length == 1) {
            // This subcommand doesn't allow the console
            if (sender instanceof ConsoleCommandSender) {
                return CommandResult.PLAYER_ONLY;
            }

            // Do '/forsake all'
            if ("all".equalsIgnoreCase(args[0])) {
                senderModel.cleanse();
            } else {
                // TODO Check for individual aspect forsake requests.
                sender.sendMessage(ChatColor.YELLOW + "To cleanse all do /cleanse all");
                return CommandResult.SUCCESS;
            }
        }
        // Handle admin command to forsake other players
        else if (args.length == 2) {
            // Check if the sender has permission to do the forsaking.
            if (!senderModel.getAdminMode()) {
                return CommandResult.NO_PERMISSIONS;
            }

            // Handle '/forsake <player> all' subcommand
            if ("all".equalsIgnoreCase(args[1])) {
                // Get the target's player model
                PlayerModel playerModel = DGData.PLAYER_R.fromName(args[0]);
                if (playerModel == null) {
                    sender.sendMessage(ChatColor.DARK_RED + "No such player! Please try a little harder.");
                    return CommandResult.QUIET_ERROR;
                }

                playerModel.cleanse();
                sender.sendMessage(
                        ChatColor.YELLOW + "You have removed all aspects from " + playerModel.getLastKnownName());
                return CommandResult.SUCCESS;
            } else {
                // TODO Check for individual aspect forsake requests.
                return CommandResult.INVALID_SYNTAX;
            }
        }

        // Something went wrong...
        return CommandResult.QUIET_ERROR;
    }
}
