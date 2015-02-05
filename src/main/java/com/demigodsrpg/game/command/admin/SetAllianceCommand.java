package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.IAspect;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SetAllianceCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 2) {
            PlayerModel player = DGGame.PLAYER_R.fromName(args[0]);
            IAspect.Alliance alliance = IAspect.Alliance.valueOf(args[1].toUpperCase());
            if (player == null || alliance == null) {
                sender.sendMessage(ChatColor.RED + "Wrong player or alliance! Please try a little harder.");
                return CommandResult.QUIET_ERROR;
            }
            player.setFaction(alliance);
            sender.sendMessage(ChatColor.YELLOW + player.getLastKnownName() + " has been set to " + alliance.name() + ".");
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
}
