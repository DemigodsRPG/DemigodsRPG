package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddDevotionCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 3) {
            try {
                Player p = DGGame.PLAYER_R.fromName(args[0]).getOfflinePlayer().getPlayer();
                double amount = Double.parseDouble(args[2]);
                Deity deity = Deity.valueOf(args[1].toUpperCase());
                if (!DGGame.PLAYER_R.fromPlayer(p).getAllDeities().contains(deity)) {
                    sender.sendMessage(ChatColor.RED + "The player you are accessing does not have that deity.");
                    return CommandResult.QUIET_ERROR;
                }

                DGGame.PLAYER_R.fromPlayer(p).setDevotion(deity, DGGame.PLAYER_R.fromPlayer(p).getDevotion(deity) + amount);

                sender.sendMessage(ChatColor.YELLOW + "You added " + amount + " devotion to " + p.getName() + " in the deity " + deity.getNomen() + ".");
            } catch (Exception ignored) {
                sender.sendMessage(ChatColor.RED + "Invalid syntax! /AddDevotion [Name, Deity, Amount]");
                return CommandResult.QUIET_ERROR;
            }
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
}
