package com.demigodsrpg.demigods.classic.command.admin;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.command.type.AdminPlayerCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class GiveDeityCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 3) {
            PlayerModel p = null;
            Deity deity = null;
            boolean major = true;
            try {
                major = args[2].equalsIgnoreCase("major");
                deity = Deity.valueOf(args[1].toUpperCase());
                p = DGClassic.PLAYER_R.fromName(args[0]);
            } catch (Exception ignored) {
            }
            if (p == null) {
                sender.sendMessage(ChatColor.RED + "No such player.");
                return CommandResult.QUIET_ERROR;
            }
            if (deity == null) {
                sender.sendMessage(ChatColor.RED + "No such deity.");
                return CommandResult.QUIET_ERROR;
            }
            if (major) {
                p.giveMajorDeity(deity, false); // TODO This might be the first time sometimes
                sender.sendMessage(ChatColor.YELLOW + "You added " + deity.getDeityName() + " to " + p.getLastKnownName() + " as major deity.");
                return CommandResult.SUCCESS;
            } else {
                p.giveDeity(deity);
                sender.sendMessage(ChatColor.YELLOW + "You added " + deity.getDeityName() + " to " + p.getLastKnownName() + " as minor deity.");
                return CommandResult.SUCCESS;
            }

        }
        return CommandResult.INVALID_SYNTAX;
    }
}
