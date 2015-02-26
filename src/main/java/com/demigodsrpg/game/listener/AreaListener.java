package com.demigodsrpg.game.listener;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.area.Area;
import com.demigodsrpg.game.area.ClaimRoom;
import com.demigodsrpg.game.area.FactionTerritory;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.registry.AreaRegistry;
import com.demigodsrpg.game.util.ZoneUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AreaListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(final PlayerMoveEvent event) {
        // Get the world
        World world = event.getTo().getWorld();

        // If it isn't a DG world, don't listen anymore
        if (!ZoneUtil.isNoDGWorld(world)) {
            AreaRegistry area_r = DGGame.AREA_R.get(world.getName());

            // Get the possible areas
            List<Area> areasFrom = area_r.fromLocation(event.getFrom());
            List<Area> areasTo = area_r.fromLocation(event.getTo());

            // Make sure the areas exist
            if (!areasTo.isEmpty()) {

                // Remove all overlapping areas
                areasTo.removeAll(areasFrom);

                // Iterate over all new areas
                for (Area area : areasTo) {
                    // Faction territories
                    if (area instanceof FactionTerritory) {
                        // Handle the faction territory, check if it should cancel the event
                        FactionTerritory factionArea = (FactionTerritory) area;
                        if (!handleFactionAreas(factionArea, event.getPlayer(), event.getTo())) {
                            event.setCancelled(true);
                        }
                    }

                    // Claim rooms
                    else if (area instanceof ClaimRoom) {
                        ClaimRoom claimRoom = (ClaimRoom) area;
                        handleClaimAreas(claimRoom, event.getPlayer());
                    }
                }
            }
        }
    }

    // -- HELPER METHODS -- //

    private boolean handleFactionAreas(FactionTerritory area, Player player, Location forward) {
        // Important info
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        Faction faction = area.getFaction();

        // Check to make sure the player is in the right faction
        if (!faction.equals(model.getFaction())) {
            // Throttle the warning message
            if (!DGGame.SERVER_R.contains(model.getMojangId(), "faction-area")) {
                player.sendMessage(ChatColor.RED + "You are not a member of the " + faction.getColor() + faction.getName() + org.bukkit.ChatColor.RED + " faction.");
                DGGame.SERVER_R.put(model.getMojangId(), "faction-area", false, 4, TimeUnit.SECONDS);
            }

            // Send a fake invisible wall to prevent the player from moving forward
            Location forwardBottom = forward.clone().add(0, -1, 0);
            player.sendBlockChange(forward, Material.BARRIER, (byte) 0);
            player.sendBlockChange(forwardBottom, Material.BARRIER, (byte) 0);

            // Cancel the event
            return false;
        }

        // Throttle the welcome message
        if (!DGGame.SERVER_R.contains(model.getMojangId(), "faction-area")) {
            player.sendMessage(faction.getColor() + faction.getWelcomeMessage());
            DGGame.SERVER_R.put(model.getMojangId(), "faction-area", true, 4, TimeUnit.SECONDS);
        }

        // Don't cancel the event
        return true;
    }

    private void handleClaimAreas(ClaimRoom area, Player player) {
        // Important info
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        Deity deity = area.getDeity();

        String endMessage = ChatColor.YELLOW + "You have chosen ";
        String factionMessage = "";

        // Set the correct type (and potentially faction if the deity is a hero)
        switch (deity.getDeityType()) {
            case GOD:
                model.setGod(deity);
                endMessage += deity.getFaction().getColor() + deity.getName() + ChatColor.YELLOW + " as your parent God.";
                break;
            case HERO:
                model.setHero(deity);
                endMessage += deity.getFaction().getColor() + deity.getName() + ChatColor.YELLOW + " as your parent Hero.";
                factionMessage = ChatColor.YELLOW + deity.getPronouns()[0] + " has placed you in the " + deity.getFaction().getColor() + deity.getFaction().getName() + ChatColor.YELLOW + " faction.";
                model.setFaction(deity.getFaction());
                break;
        }

        // Send the appropriate messages
        player.sendMessage(endMessage);
        if (!"".equals(factionMessage)) {
            player.sendMessage(factionMessage);
        }

        // If there is a next location, teleport the player to it
        if (area.getNextLocation() != null) {
            player.teleport(area.getNextLocation());
        }

        // Save the model
        DGGame.PLAYER_R.register(model);
    }
}
