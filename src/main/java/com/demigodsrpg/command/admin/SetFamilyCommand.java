package com.demigodsrpg.command.admin;

import com.demigodsrpg.DGData;
import com.demigodsrpg.command.type.AdminPlayerCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetFamilyCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }
        if (DGData.PLAYER_R.fromPlayer((Player) sender).getAdminMode()) {
            if (args.length == 2) {
                PlayerModel player = DGData.PLAYER_R.fromName(args[0]);
                Family family = DGData.getFamily(args[1].toUpperCase());
                if (player == null || family == null) {
                    sender.sendMessage(ChatColor.RED + "Wrong player or faction! Please try a little harder.");
                    return CommandResult.QUIET_ERROR;
                }
                player.setFamily(family);
                sender.sendMessage(
                        ChatColor.YELLOW + player.getLastKnownName() + " has been set to the " + family.getName() +
                                " faction.");
                return CommandResult.SUCCESS;
            }
            return CommandResult.INVALID_SYNTAX;
        }
        return CommandResult.NO_PERMISSIONS;
    }
}
