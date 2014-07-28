package com.demigodsrpg.demigods.classic.command.admin;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.command.type.AdminPlayerCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SetAllianceCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 2) {
            PlayerModel player = DGClassic.PLAYER_R.fromName(args[0]);
            IDeity.Alliance alliance = IDeity.Alliance.valueOf(args[1].toUpperCase());
            if (player == null || alliance == null) {
                sender.sendMessage(ChatColor.RED + "Wrong player or alliance! Please try a little harder.");
                return CommandResult.QUIET_ERROR;
            }
            player.setAlliance(alliance);
            sender.sendMessage(ChatColor.YELLOW + player.getLastKnownName() + " has been set to " + alliance.name() + ".");
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
}
