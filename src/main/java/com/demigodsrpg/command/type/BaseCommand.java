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

package com.demigodsrpg.command.type;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
