package com.demigodsrpg.demigods.classic.command;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.command.type.BaseCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class AllianceCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }

        Player player = (Player) sender;
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);

        if (IDeity.Alliance.NEUTRAL.equals(model.getAlliance()) || IDeity.Alliance.EXCOMMUNICATED.equals(model.getAlliance())) {
            player.sendMessage(ChatColor.RED + "You aren't in an alliance.");
            return CommandResult.QUIET_ERROR;
        }

        if (DGClassic.SERV_R.contains("alliance_chat", player.getUniqueId().toString())) {
            DGClassic.SERV_R.remove("alliance_chat", player.getUniqueId().toString());
            player.sendMessage(ChatColor.YELLOW + "You just disabled alliance chat.");
        } else {
            DGClassic.SERV_R.put("alliance_chat", player.getUniqueId().toString(), true);
            player.sendMessage(ChatColor.YELLOW + "You just enabled alliance chat.");
        }
        return CommandResult.SUCCESS;
    }
}
