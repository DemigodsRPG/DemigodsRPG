package com.demigodsrpg.game.listener;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.area.Area;
import com.demigodsrpg.game.area.ClaimRoom;
import com.demigodsrpg.game.area.FactionTerritory;
import com.demigodsrpg.game.registry.AreaRegistry;
import com.demigodsrpg.game.util.ZoneUtil;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

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
                for (Area area : areasTo) {
                    // Faction territories
                    if (area instanceof FactionTerritory) {
                        FactionTerritory factionArea = (FactionTerritory) area;

                        // TODO Faction areas
                    }

                    // Claim rooms
                    else if (area instanceof ClaimRoom) {
                        ClaimRoom claimRoom = (ClaimRoom) area;

                        // TODO Claim rooms
                    }
                }
            }
        }
    }
}
