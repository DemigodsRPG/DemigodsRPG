package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class GiveAspectCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 2) {
            PlayerModel p = null;
            Aspect aspect = null;
            try {
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

            p.giveAspect(aspect);
            sender.sendMessage(ChatColor.YELLOW + "You added " + aspect.getGroup().getName() + " " + aspect.getTier().name() + " to " + p.getLastKnownName() + ".");
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
}
