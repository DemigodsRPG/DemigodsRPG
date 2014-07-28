package com.demigodsrpg.demigods.classic.command.admin;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.command.type.AdminPlayerCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckPlayerCommand extends AdminPlayerCommand{
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        Player p = null;

        if(args.length == 1)
        {
            try{
                p = DGClassic.PLAYER_R.fromName(args[0]).getOfflinePlayer().getPlayer();
            } catch (Exception ignored)
            {
                sender.sendMessage(ChatColor.RED + "Player is not real, but we appreciate the attempt!");
                return CommandResult.SUCCESS;
            }
            sendInfo(p);
            return CommandResult.SUCCESS;
        }
        sender.sendMessage(ChatColor.RED + "Wrong syntax! /CheckPlayer [Name]");
        return CommandResult.SUCCESS;
    }

    private void sendInfo(Player p)
    {
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(p);
        p.sendMessage(StringUtil2.chatTitle("Player Stats"));
        p.sendMessage(p.getName() + " is a " + model.getMajorDeity().getColor() + model.getMajorDeity().getNomen());
        if (!model.getContractedDeities().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Deity deity : model.getContractedDeities()) {
                builder.append(deity.getColor()).append(deity.getDeityName()).append(ChatColor.RESET).append(", ");
            }
            String minorDeities = builder.toString();
            minorDeities = minorDeities.substring(0, minorDeities.length() - 4);
            p.sendMessage(p.getName() + " is also allied with: " + minorDeities);
        }
        p.sendMessage("Favor: " + model.getFavor() + " / " + model.getMaxFavor());
        p.sendMessage("Total Devotion: " + model.getTotalDevotion());
        p.sendMessage("For a list of binds, use /binds");
    }
}
