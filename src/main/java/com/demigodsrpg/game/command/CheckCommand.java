package com.demigodsrpg.game.command;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.ColorUtil;
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
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        player.sendMessage(StringUtil2.chatTitle("Player Stats"));
        // FIXME player.sendMessage(ChatColor.YELLOW + "You are " + (StringUtil2.beginsWithVowel(nomen) ? "an " : "a ") + model.getMajorDeity().getColor() + nomen + ".");
        player.sendMessage(ChatColor.YELLOW + "You are allied with the " + StringUtil2.beautify(model.getFaction().getName()) + " alliance.");
        player.sendMessage(ChatColor.YELLOW + "You have " + ColorUtil.getColor(player.getHealth(), player.getMaxHealth()) + ChatColor.ITALIC + player.getHealth() + " / " + player.getMaxHealth() + ChatColor.YELLOW + " health.");
        if (!model.getAspects().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String deityName : model.getAspects()) {
                Aspect aspect = Aspects.valueOf(deityName);
                builder.append(aspect.getColor()).append(aspect.getName()).append(ChatColor.RESET).append(", ");
            }
            String minorDeities = builder.toString();
            minorDeities = minorDeities.substring(0, minorDeities.length() - 4) + ".";
            player.sendMessage("You have also allied with: " + minorDeities);
        }
        player.sendMessage("Favor: " + model.getFavor());
        player.sendMessage("Total Devotion: " + model.getTotalExperience());
        player.sendMessage("Number of ascensions: " + model.getLevel());
        player.sendMessage("Use " + ChatColor.ITALIC + "/binds" + ChatColor.RESET + " for a list of all ability binds.");

        return CommandResult.SUCCESS;
    }
}
