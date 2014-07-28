package com.demigodsrpg.demigods.classic.command;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.command.type.BaseCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import com.google.common.cache.CacheStats;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CheckCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }
        Player player = (Player) sender;
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);
        player.sendMessage(StringUtil2.chatTitle("Player Stats"));
        String nomen = model.getMajorDeity().getNomen();
        player.sendMessage("You are " + (StringUtil2.beginsWithVowel(nomen) ? "an " : "a ") + model.getMajorDeity().getColor() + nomen + ".");
        player.sendMessage("You have " + ChatColor.RED + model.getMaxHealth() + ChatColor.RESET + " health.");
        if (!model.getContractedDeities().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Deity deity : model.getContractedDeities()) {
                builder.append(deity.getColor()).append(deity.getDeityName()).append(ChatColor.RESET).append(", ");
            }
            String minorDeities = builder.toString();
            minorDeities = minorDeities.substring(0, minorDeities.length() - 4) + ".";
            player.sendMessage("You have also allied with: " + minorDeities);
        }
        player.sendMessage("Favor: " + model.getFavor() + " / " + model.getMaxFavor());
        player.sendMessage("Total Devotion: " + model.getTotalDevotion());
        player.sendMessage("Number of ascensions: " + model.getAscensions());
        player.sendMessage("Use " + ChatColor.ITALIC + "/binds" + ChatColor.RESET + " for a list of all ability binds.");

        if (player.hasPermission("demigods.admin.debug")) {
            player.sendMessage("   ");
            player.sendMessage(StringUtil2.chatTitle("Server Debug Stats"));
            CacheStats stats = DGClassic.PLAYER_R.getCache().stats();
            player.sendMessage("Requests to cache: " + stats.requestCount());
            player.sendMessage("Size of cache: " + DGClassic.PLAYER_R.getCache().size());
        }

        return CommandResult.SUCCESS;
    }
}
