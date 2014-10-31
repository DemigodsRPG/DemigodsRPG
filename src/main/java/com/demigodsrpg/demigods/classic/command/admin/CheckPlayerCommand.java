package com.demigodsrpg.demigods.classic.command.admin;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.command.type.AdminPlayerCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import com.demigodsrpg.demigods.classic.util.ColorUtil;
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
                p = DGClassic.PLAYER_R.fromName(args[0]).getOfflinePlayer();
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
        PlayerModel model = DGClassic.PLAYER_R.fromId(p.getUniqueId().toString());
        s.sendMessage(StringUtil2.chatTitle("Player Stats"));
        s.sendMessage(p.getName() + " is a " + model.getMajorDeity().getColor() + model.getMajorDeity().getNomen());
        s.sendMessage(p.getName() + " is allied with the " + StringUtil2.beautify(model.getAlliance().name()) + " alliance.");
        if (p.isOnline())
            s.sendMessage(p.getName() + " has " + ColorUtil.getColor(p.getPlayer().getHealth(), p.getPlayer().getMaxHealth()) + ChatColor.ITALIC + p.getPlayer().getHealth() + " / " + p.getPlayer().getMaxHealth() + ChatColor.YELLOW + " health.");
        if (!model.getContractedDeities().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String deityName : model.getContractedDeities()) {
                Deity deity = Deity.valueOf(deityName);
                builder.append(deity.getColor()).append(deity.getDeityName()).append(ChatColor.RESET).append(", ");
            }
            String minorDeities = builder.toString();
            minorDeities = minorDeities.substring(0, minorDeities.length() - 4);
            s.sendMessage(p.getName() + " is also allied with: " + minorDeities);
        }
        s.sendMessage("Favor: " + model.getFavor());
        s.sendMessage("Total Devotion: " + model.getTotalDevotion());
        s.sendMessage("Number of ascensions: " + model.getAscensions());
    }
}
