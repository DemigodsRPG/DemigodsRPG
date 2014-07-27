package com.demigodsrpg.demigods.classic.command;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.command.type.BaseCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class AllianceCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }

        Player player = (Player) sender;
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);

        if(model.getAlliance() == IDeity.Alliance.NEUTRAL){
            player.sendMessage("You aren't in an Alliance");
            return CommandResult.QUIET_ERROR;
        }
        Set<PlayerModel> onlinePlayers = DGClassic.PLAYER_R.getOnlineInAlliance(model.getAlliance());

        StringBuilder builder = new StringBuilder();
        for(String string : args){
            if(builder.length() > 0){
                builder.append(" ");
            }
            builder.append(string);
        }


        for (PlayerModel onlinePlayer : onlinePlayers){
            Player onlinePlayer1 = Bukkit.getPlayer(onlinePlayer.getMojangId());
            onlinePlayer1.sendMessage("[" + StringUtil2.beautify(onlinePlayer.getAlliance().name()) + "] " + builder.toString());

        }
        return CommandResult.SUCCESS;
    }
}
