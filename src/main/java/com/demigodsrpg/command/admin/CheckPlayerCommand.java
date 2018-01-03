package com.demigodsrpg.command.admin;

import com.demigodsrpg.DGData;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.command.type.AdminPlayerCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ColorUtil;
import com.demigodsrpg.util.misc.StringUtil2;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckPlayerCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        OfflinePlayer p = null;

        if (args.length == 1) {
            try {
                p = DGData.PLAYER_R.fromName(args[0]).getOfflinePlayer();
            } catch (Exception ignored) {
                sender.sendMessage(ChatColor.RED + "Player is not real, but we appreciate the attempt!");
                return CommandResult.QUIET_ERROR;
            }
            if (p.isOnline()) {
                sendInfo(p.getPlayer(), sender);
            }
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }

    private void sendInfo(Player p, CommandSender s) {
        PlayerModel model = DGData.PLAYER_R.fromId(p.getUniqueId().toString());
        s.sendMessage(StringUtil2.chatTitle("Player Stats"));
        if (model.getGod().isPresent() && model.getHero().isPresent()) {
            s.sendMessage(ChatColor.YELLOW + p.getName() + " is the offspring of " + model.getGod().get().getName() + " and " + model.getHero().get().getName() + "."); // TODO Colors
        }
        s.sendMessage(p.getName() + " is a member of the " + StringUtil2.beautify(model.getFamily().getName()) + " family.");
        if (p.isOnline())
            s.sendMessage(p.getName() + " has " + ColorUtil.getColor(p.getPlayer().getHealth(), p.getPlayer().getMaxHealth()) + ChatColor.ITALIC + p.getPlayer().getHealth() + " / " + p.getPlayer().getMaxHealth() + ChatColor.YELLOW + " health.");
        if (!model.getAspects().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String deityName : model.getAspects()) {
                Aspect aspect = Aspects.valueOf(deityName);
                builder.append(aspect.getGroup().getColor()).append(aspect.name()).append(ChatColor.RESET).append(", ");
            }
            String minorDeities = builder.toString();
            minorDeities = minorDeities.substring(0, minorDeities.length() - 4);
            s.sendMessage(p.getName() + " is also allied with: " + minorDeities);
        }
        s.sendMessage("Favor: " + model.getFavor());
        s.sendMessage("Total Devotion: " + model.getTotalExperience());
        s.sendMessage("Number of ascensions: " + model.getLevel());
    }
}
