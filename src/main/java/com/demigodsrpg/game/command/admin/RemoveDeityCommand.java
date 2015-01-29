package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.model.PlayerModel;
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
                p = DGGame.PLAYER_R.fromName(args[0]);
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
