package com.demigodsrpg.game.command;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class FactionCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }

        Player player = (Player) sender;
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        if (Faction.NEUTRAL.equals(model.getFaction()) || Faction.EXCOMMUNICATED.equals(model.getFaction())) {
            player.sendMessage(ChatColor.RED + "You aren't in an alliance.");
            return CommandResult.QUIET_ERROR;
        }

        if (DGGame.SERVER_R.contains("alliance_chat", player.getUniqueId().toString())) {
            DGGame.SERVER_R.remove("alliance_chat", player.getUniqueId().toString());
            player.sendMessage(ChatColor.YELLOW + "You just disabled alliance chat.");
        } else {
            DGGame.SERVER_R.put("alliance_chat", player.getUniqueId().toString(), true);
            player.sendMessage(ChatColor.YELLOW + "You just enabled alliance chat.");
        }
        return CommandResult.SUCCESS;
    }
}
