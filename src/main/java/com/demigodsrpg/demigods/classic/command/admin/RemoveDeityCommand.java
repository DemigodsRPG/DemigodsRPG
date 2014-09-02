package com.demigodsrpg.demigods.classic.command.admin;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.command.type.AdminPlayerCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RemoveDeityCommand extends AdminPlayerCommand {

    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 2) {
            PlayerModel p;
            Deity deity;
            try {
                deity = Deity.valueOf(args[1].toUpperCase());
                p = DGClassic.PLAYER_R.fromName(args[0]);
            } catch (Exception ignored) {
                sender.sendMessage(ChatColor.RED + "Wrong player or deity! Please try a little harder.");
                return CommandResult.QUIET_ERROR;
            }
            if (p.getMajorDeity().equals(deity)) {
                p.setMajorDeity(Deity.HUMAN);
                sender.sendMessage(ChatColor.YELLOW + "You removed " + deity.getDeityName() + " from " + p.getLastKnownName() + ".");
                p.setDevotion(deity, 0.0);
                return CommandResult.SUCCESS;
            } else {
                p.removeContractedDeity(deity);
                sender.sendMessage(ChatColor.YELLOW + "You removed " + deity.getDeityName() + " from " + p.getLastKnownName() + ".");
                p.setDevotion(deity, 0.0);
                return CommandResult.SUCCESS;
            }
        }
        return CommandResult.INVALID_SYNTAX;
    }
}
