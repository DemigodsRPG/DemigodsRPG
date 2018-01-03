package com.demigodsrpg.command.admin;

import com.demigodsrpg.DGData;
import com.demigodsrpg.command.type.BaseCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.demigodsrpg.family.Family;
import com.google.common.base.Joiner;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class CreateFamilyCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }
        if (DGData.PLAYER_R.fromPlayer((Player) sender).getAdminMode()) {
            if (args.length < 3) {
                return CommandResult.INVALID_SYNTAX;
            }

            List<String> parts = new ArrayList<>(Arrays.asList(args));
            parts.subList(3, parts.size() - 1); // FIXME IDK if this will work how I want

            String name = args[0];
            String color = ChatColor.translateAlternateColorCodes('&', args[1]);
            String chatSymbol = args[2];
            String welcomeMessage = Joiner.on(" ").join(parts);

            Family newFamily = new Family(name, color, chatSymbol, welcomeMessage);
            DGData.FAMILY_R.register(newFamily);

            sender.sendMessage("You've successfully created the " + color + name + " faction!");

            return CommandResult.SUCCESS;
        }
        return CommandResult.NO_PERMISSIONS;
    }
}
