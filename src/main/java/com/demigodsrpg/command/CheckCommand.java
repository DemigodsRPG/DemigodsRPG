package com.demigodsrpg.command;

import com.demigodsrpg.DGData;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.command.type.BaseCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ColorUtil;
import com.demigodsrpg.util.misc.StringUtil2;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class CheckCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }
        Player player = (Player) sender;
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);
        player.sendMessage(StringUtil2.chatTitle("Player Stats"));
        if (model.getGod().isPresent() && model.getHero().isPresent()) {
            player.sendMessage(ChatColor.YELLOW + "You are the offspring of " + model.getGod().get().getName() + " and " + model.getHero().get().getName() + "."); // TODO Colors
        }
        player.sendMessage(ChatColor.YELLOW + "You are allied with the " + StringUtil2.beautify(model.getFamily().getName()) + " family.");
        player.sendMessage(ChatColor.YELLOW + "You have " + ColorUtil.getColor(player.getHealth(), player.getMaxHealth()) + ChatColor.ITALIC + player.getHealth() + " / " + player.getMaxHealth() + ChatColor.YELLOW + " health.");
        if (!model.getAspects().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String deityName : model.getAspects()) {
                Aspect aspect = Aspects.valueOf(deityName);
                builder.append(aspect.getGroup().getColor()).append(aspect.name()).append(ChatColor.RESET).append(", ");
            }
            String aspects = builder.toString();
            aspects = aspects.substring(0, aspects.length() - 4) + ".";
            player.sendMessage("Your aspects: " + aspects);
        }
        player.sendMessage("Favor: " + model.getFavor());
        player.sendMessage("Total Devotion: " + model.getTotalExperience());
        player.sendMessage("Number of ascensions: " + model.getLevel());
        player.sendMessage("Use " + ChatColor.ITALIC + "/binds" + ChatColor.RESET + " for a list of all ability binds.");

        return CommandResult.SUCCESS;
    }
}
