package com.demigodsrpg.command.admin;

import com.demigodsrpg.DGData;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.command.type.AdminPlayerCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RemoveAspectCommand extends AdminPlayerCommand {

    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 2) {
            PlayerModel p;
            Aspect aspect;
            try {
                aspect = Aspects.valueOf(args[1].toUpperCase());
                p = DGData.PLAYER_R.fromName(args[0]);
            } catch (Exception ignored) {
                sender.sendMessage(ChatColor.RED + "Wrong player or deity! Please try a little harder.");
                return CommandResult.QUIET_ERROR;
            }

            p.removeAspect(aspect);
            sender.sendMessage(
                    ChatColor.YELLOW + "You removed " + aspect.getGroup().getName() + " " + aspect.getTier().name() +
                            " from " + p.getLastKnownName() + ".");
            p.setExperience(aspect, 0.0, true);
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
}
