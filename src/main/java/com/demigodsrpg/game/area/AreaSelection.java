package com.demigodsrpg.game.area;

import com.demigodsrpg.game.DGGame;
import org.spongepowered.api.entity.EntityInteractionTypes;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AreaSelection {

    // -- GLOBAL CACHE -- //

    public static final ConcurrentMap<String, AreaSelection> AREA_SELECTION_CACHE = new ConcurrentHashMap<>();

    // -- META DATA -- //

    private String playerUUID;
    private List<Location> points;

    public AreaSelection(Player player) {
        this.playerUUID = player.getUniqueId().toString();
        points = new ArrayList<>();
    }

    @Subscribe(order = Order.LATE)
    public void onClick(PlayerInteractEvent event) {
        if (!event.isCancelled()) {
            if (event.getPlayer().getUniqueId().toString().equals(playerUUID) && EntityInteractionTypes.USE.equals(event.getInteractionType()) && event.getClickedPosition().isPresent()) {
                // Get the point
                Location point = new Location(event.getPlayer().getLocation().getExtent(), event.getClickedPosition().get().toDouble());

                if (!points.isEmpty() && !points.get(0).getExtent().equals(point.getExtent())) {
                    event.getPlayer().sendMessage(TextColors.RED + "Points must all be from the same world.");
                    return;
                }

                // Cancel the click to be sure
                event.setCancelled(true);

                // Either add or remove the point
                if (!points.contains(point)) {
                    points.add(point);
                    event.getPlayer().sendMessage(TextColors.YELLOW + "Point " + points.size() + " has been marked.");
                } else {
                    int index = points.indexOf(point) + 1;
                    points.remove(point);
                    event.getPlayer().sendMessage(TextColors.RED + "Point " + index + " has been unmarked.");
                }
            }
        }
    }

    public List<Location> getPoints() {
        return points;
    }

    public void register() {
        // Register this listener
        DGGame.GAME.getEventManager().register(this, DGGame.PLUGIN);
    }

    public void unregister() {
        // Unregister this listener
        DGGame.GAME.getEventManager().unregister(this);
    }
}
