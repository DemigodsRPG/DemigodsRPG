package com.demigodsrpg.game.command.admin;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.ColorUtil;
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
                p = DGGame.PLAYER_R.fromName(args[0]).getOfflinePlayer();
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
        PlayerModel model = DGGame.PLAYER_R.fromId(p.getUniqueId().toString());
        s.sendMessage(StringUtil2.chatTitle("Player Stats"));
        s.sendMessage(p.getName() + " is a " + model.getMajorDeity().getColor() + model.getMajorDeity().getNomen());
        s.sendMessage(p.getName() + " is allied with the " + StringUtil2.beautify(model.getFaction().name()) + " alliance.");
        if (p.isOnline())
            s.sendMessage(p.getName() + " has " + ColorUtil.getColor(p.getPlayer().getHealth(), p.getPlayer().getMaxHealth()) + ChatColor.ITALIC + p.getPlayer().getHealth() + " / " + p.getPlayer().getMaxHealth() + ChatColor.YELLOW + " health.");
        if (!model.getAspects().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String deityName : model.getAspects()) {
                Aspects aspect = Aspects.valueOf(deityName);
                builder.append(aspect.getColor()).append(aspect.getDeityName()).append(ChatColor.RESET).append(", ");
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
