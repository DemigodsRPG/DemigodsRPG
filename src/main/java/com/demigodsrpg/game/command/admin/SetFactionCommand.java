package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SetFactionCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 2) {
            PlayerModel player = DGGame.PLAYER_R.fromName(args[0]);
            Faction faction = DGGame.FACTION_R.factionFromName(args[1].toUpperCase());
            if (player == null || faction == null) {
                sender.sendMessage(ChatColor.RED + "Wrong player or alliance! Please try a little harder.");
                return CommandResult.QUIET_ERROR;
            }
            player.setFaction(faction);
            sender.sendMessage(ChatColor.YELLOW + player.getLastKnownName() + " has been set to the " + faction.getName() + " faction.");
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
}
