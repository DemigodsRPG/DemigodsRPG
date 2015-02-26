package com.demigodsrpg.game.command;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminModeComand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof Player && sender.hasPermission("demigods.admin")) {
            PlayerModel model = DGGame.PLAYER_R.fromPlayer((Player) sender);
            if (model.getAdminMode()) {
                sender.sendMessage(ChatColor.YELLOW + "Demigods admin mode disabled.");
            } else {
                sender.sendMessage(ChatColor.YELLOW + "Demigods admin mode enabled.");
            }
            model.setAdminMode(!model.getAdminMode());

            return CommandResult.SUCCESS;
        }

        return CommandResult.NO_PERMISSIONS;
    }
}
