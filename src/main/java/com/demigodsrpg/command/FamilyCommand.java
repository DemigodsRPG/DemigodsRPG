package com.demigodsrpg.command;

import com.demigodsrpg.DGData;
import com.demigodsrpg.command.type.BaseCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class FamilyCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }

        Player player = (Player) sender;
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        if (Family.NEUTRAL.equals(model.getFamily()) || Family.EXCOMMUNICATED.equals(model.getFamily())) {
            player.sendMessage(ChatColor.RED + "You aren't in a family.");
            return CommandResult.QUIET_ERROR;
        }

        if (DGData.SERVER_R.contains("family_chat", player.getUniqueId().toString())) {
            DGData.SERVER_R.remove("family_chat", player.getUniqueId().toString());
            player.sendMessage(ChatColor.YELLOW + "You just disabled family chat.");
        } else {
            DGData.SERVER_R.put("family_chat", player.getUniqueId().toString(), true);
            player.sendMessage(ChatColor.YELLOW + "You just enabled family chat.");
        }
        return CommandResult.SUCCESS;
    }
}
