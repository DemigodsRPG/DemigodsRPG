package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class GiveAspectCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 3) {
            PlayerModel p = null;
            Aspects aspect = null;
            boolean major = true;
            try {
                major = args[2].equalsIgnoreCase("major");
                aspect = Aspects.valueOf(args[1].toUpperCase());
                p = DGGame.PLAYER_R.fromName(args[0]);
            } catch (Exception ignored) {
            }
            if (p == null) {
                sender.sendMessage(ChatColor.RED + "No such player.");
                return CommandResult.QUIET_ERROR;
            }
            if (aspect == null) {
                sender.sendMessage(ChatColor.RED + "No such deity.");
                return CommandResult.QUIET_ERROR;
            }
            if (major) {
                p.giveMajorDeity(aspect, false); // TODO This might be the first time sometimes
                sender.sendMessage(ChatColor.YELLOW + "You added " + aspect.getDeityName() + " to " + p.getLastKnownName() + " as major deity.");
                return CommandResult.SUCCESS;
            } else {
                p.giveDeity(aspect);
                sender.sendMessage(ChatColor.YELLOW + "You added " + aspect.getDeityName() + " to " + p.getLastKnownName() + " as minor deity.");
                return CommandResult.SUCCESS;
            }

        }
        return CommandResult.INVALID_SYNTAX;
    }
}
