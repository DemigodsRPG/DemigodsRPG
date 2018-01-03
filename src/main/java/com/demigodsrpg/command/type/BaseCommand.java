package com.demigodsrpg.command.type;

import org.bukkit.ChatColor;
import org.bukkit.command.*;

public abstract class BaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        CommandResult result = onCommand(sender, command, args);
        switch (result) {
            case SUCCESS:
            case QUIET_ERROR:
                break;
            case INVALID_SYNTAX:
                sender.sendMessage(ChatColor.RED + "Invalid syntax, please try again.");
                return false;
            case NO_PERMISSIONS:
                sender.sendMessage(ChatColor.RED + "You don't have the permissions to use this command.");
                break;
            case CONSOLE_ONLY:
                sender.sendMessage(ChatColor.RED + "This command is for the console only.");
                break;
            case PLAYER_ONLY:
                sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
                break;
            case ERROR:
                sender.sendMessage(ChatColor.RED + "An error occurred, please check the console.");
                break;
            case UNKNOWN:
            default:
                sender.sendMessage(ChatColor.RED + "The command can't run for some unknown reason.");
                break;
        }
        return true;
    }

    protected abstract CommandResult onCommand(CommandSender sender, Command command, String[] args);
}
