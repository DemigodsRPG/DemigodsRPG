package com.demigodsrpg.demigods.classic.command.admin;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.command.type.BaseCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForsakeCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "To forsake all do /forsake all");
            return CommandResult.SUCCESS;
        }else if(args.length == 1){
            if (!(sender instanceof Player)){
                sender.sendMessage(ChatColor.DARK_RED + "You are not allowed to do this.");
                return CommandResult.PLAYER_ONLY;
            }
            Player player = (Player) sender;
            PlayerModel playerModel;
            try {
                playerModel = DGClassic.PLAYER_R.fromPlayer(player);
            }catch (Exception ignored){
                sender.sendMessage(ChatColor.DARK_RED + "Wrong player or deity! Please try a little harder.");
                return CommandResult.QUIET_ERROR;
            }
            if (args[0].equalsIgnoreCase("all")){
                for (String deityName : playerModel.getAllDeities()){
                    Deity deity;
                    try {
                        deity = Deity.valueOf(deityName.toUpperCase());
                    }catch (Exception ignored){
                        sender.sendMessage(ChatColor.RED + "Wrong player or deity! Please try a little harder.");
                        return CommandResult.QUIET_ERROR;
                    }
                    if (playerModel.getMajorDeity().equals(deity)){
                        playerModel.setMajorDeity(Deity.HUMAN);
                        sender.sendMessage(ChatColor.YELLOW + "You have forsaken " + deity.getDeityName());
                    }else {
                        playerModel.removeContractedDeity(deity);
                        sender.sendMessage(ChatColor.YELLOW + "You have forsaken " + deity.getDeityName());
                    }
                }
                return CommandResult.SUCCESS;
            }else {
                sender.sendMessage(ChatColor.YELLOW + "To forsake all do /forsake all");
                return CommandResult.SUCCESS;
            }
        }else if (args.length == 2){
            if (!sender.hasPermission("demigods.admin.forsake")){
                return CommandResult.NO_PERMISSIONS;
            }
            if (args[1].equalsIgnoreCase("all")){
                PlayerModel playerModel;
                try {
                    playerModel = DGClassic.PLAYER_R.fromName(args[0]);
                }catch (Exception ingnored){
                    sender.sendMessage(ChatColor.DARK_RED + "Wrong player! Please try a little harder.");
                    return CommandResult.QUIET_ERROR;
                }
                playerModel.setAlliance(IDeity.Alliance.NEUTRAL);
                for (String deityName : playerModel.getAllDeities()){
                    Deity deity;
                    try {
                        deity = Deity.valueOf(deityName);
                    }catch (Exception ignored){
                        continue;
                    }
                    if (playerModel.getMajorDeity().equals(deity)){
                        playerModel.setMajorDeity(Deity.HUMAN);
                    }else {
                        playerModel.removeContractedDeity(deity);
                    }
                }
                sender.sendMessage(ChatColor.YELLOW + "You have removed all Deities from " + args[0]);
                return CommandResult.SUCCESS;
            }else{
                sender.sendMessage(ChatColor.YELLOW + "Invalid Usage");
                sender.sendMessage(ChatColor.YELLOW + "/forsake <player> all");
                return CommandResult.INVALID_SYNTAX;
            }
        }
        return CommandResult.QUIET_ERROR;
    }
}
