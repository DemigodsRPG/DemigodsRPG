package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.area.AreaPriority;
import com.demigodsrpg.game.area.AreaSelection;
import com.demigodsrpg.game.area.FactionTerritory;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateFactionAreaCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        // Need at least 1 arg
        if (args.length > 0) {
            if (sender instanceof Player) {
                PlayerModel model = DGGame.PLAYER_R.fromPlayer((Player) sender);
                if (model.getAdminMode()) {
                    // Is there a selection made?
                    if (AreaSelection.AREA_SELECTION_CACHE.containsKey(model.getMojangId())) {
                        // Get the selection
                        AreaSelection selection = AreaSelection.AREA_SELECTION_CACHE.get(model.getMojangId());
                        AreaSelection.AREA_SELECTION_CACHE.remove(model.getMojangId());

                        // Get territory info
                        List<Location> corners = selection.getPoints();
                        Faction faction = DGGame.FACTION_R.factionFromName(args[0]);

                        // Does the faction exist?
                        if (faction == null) {
                            sender.sendMessage(ChatColor.RED + args[0] + " is not a valid faction name.");
                            return CommandResult.QUIET_ERROR;
                        }

                        // Check that the selection isn't empty
                        if (!corners.isEmpty()) {
                            // Unregister/clear the selection
                            selection.unregister();
                            sender.sendMessage(ChatColor.YELLOW + "Area selection cleared.");

                            // Create and register the faction territory
                            FactionTerritory territory = new FactionTerritory(faction, AreaPriority.NORMAL, corners);
                            DGGame.AREA_R.get(selection.getPoints().get(0).getWorld().getName()).register(territory);

                            // Notify the admin
                            sender.sendMessage(ChatColor.YELLOW + "Faction territory for " + faction.getName() + " has been created.");

                            return CommandResult.SUCCESS;
                        } else {
                            sender.sendMessage(ChatColor.RED + "The selection was empty, try again.");
                            return CommandResult.QUIET_ERROR;
                        }
                    } else {
                        // Tell them to make a selection
                        sender.sendMessage(ChatColor.RED + "Make a selection before using this command.");
                        return CommandResult.QUIET_ERROR;
                    }
                } else {
                    return CommandResult.NO_PERMISSIONS;
                }
            } else {
                return CommandResult.PLAYER_ONLY;
            }
        }

        return CommandResult.INVALID_SYNTAX;
    }
}
