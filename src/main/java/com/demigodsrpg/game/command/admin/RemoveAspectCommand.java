package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RemoveAspectCommand extends AdminPlayerCommand {

    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 2) {
            PlayerModel p;
            Aspects aspect;
            try {
                aspect = Aspects.valueOf(args[1].toUpperCase());
                p = DGGame.PLAYER_R.fromName(args[0]);
            } catch (Exception ignored) {
                sender.sendMessage(ChatColor.RED + "Wrong player or deity! Please try a little harder.");
                return CommandResult.QUIET_ERROR;
            }
            if (p.getMajorDeity().equals(aspect)) {
                p.setMajorDeity(Aspects.HUMAN);
                sender.sendMessage(ChatColor.YELLOW + "You removed " + aspect.getDeityName() + " from " + p.getLastKnownName() + ".");
                p.setExperience(aspect, 0.0);
                return CommandResult.SUCCESS;
            } else {
                p.removeAspect(aspect);
                sender.sendMessage(ChatColor.YELLOW + "You removed " + aspect.getDeityName() + " from " + p.getLastKnownName() + ".");
                p.setExperience(aspect, 0.0);
                return CommandResult.SUCCESS;
            }
        }
        return CommandResult.INVALID_SYNTAX;
    }
}
