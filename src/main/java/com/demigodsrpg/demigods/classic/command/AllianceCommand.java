package com.demigodsrpg.demigods.classic.command;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.command.type.BaseCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
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

        if(model.getAlliance() == IDeity.Alliance.NEUTRAL){
            player.sendMessage("You aren't in an Alliance");
            return CommandResult.QUIET_ERROR;
        }
        if(DGClassic.SERV_R.exists("alliance_chat", player.getUniqueId().toString())){
            DGClassic.SERV_R.remove("alliance_chat", player.getUniqueId().toString());
            player.sendMessage("Disabled Alliance Chat");
        }else{
            DGClassic.SERV_R.put("alliance_chat", player.getUniqueId().toString(), true);
            player.sendMessage("Enabled Alliance Chat");
        }
        return CommandResult.SUCCESS;
    }
}
