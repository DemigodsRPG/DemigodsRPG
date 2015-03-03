package com.demigodsrpg.game.listener;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.area.Area;
import com.demigodsrpg.game.area.ClaimRoom;
import com.demigodsrpg.game.area.FactionTerritory;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.deity.DeityType;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.registry.AreaRegistry;
import com.demigodsrpg.game.util.ZoneUtil;
import com.flowpowered.math.vector.Vector3d;
import org.apache.commons.lang.StringUtils;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.EntityTeleportEvent;
import org.spongepowered.api.event.entity.living.player.PlayerMoveEvent;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AreaListener {

    @Subscribe(order = Order.LATE)
    public void onPlayerMove(final PlayerMoveEvent event) {
        // Ignore if cancelled
        if (!event.isCancelled()) {

            // Get the world
            World world = (World) event.getNewLocation().getExtent();

            // If it isn't a DG world, don't listen anymore
            if (!ZoneUtil.isNoDGWorld(world)) {
                AreaRegistry area_r = DGGame.AREA_R.get(world.getName());

                // Get the possible areas
                List<Area> areasFrom = area_r.fromLocation(event.getOldLocation());
                List<Area> areasTo = area_r.fromLocation(event.getNewLocation());

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
                            if (!handleFactionAreas(factionArea, event.getPlayer(), event.getNewLocation(), !(event instanceof EntityTeleportEvent))) {
                                // Cancel the event
                                event.setCancelled(true);

                                // Bounce back
                                Vector3d victor = event.getPlayer().getVelocity();
                                victor.mul(-8); // TODO This is wonky
                                victor = new Vector3d(victor.getX(), 0.5, victor.getZ());
                                event.getPlayer().setVelocity(victor);
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
    }

    // -- HELPER METHODS -- //

    private boolean handleFactionAreas(FactionTerritory area, Player player, Location forward, boolean block) {
        // Important info
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        Faction faction = area.getFaction();

        // Check to make sure the player is in the right faction
        if (!faction.equals(model.getFaction()) && !model.getAdminMode()) {
            // Throttle the warning message
            if (!DGGame.MISC_R.contains(model.getMojangId(), "faction-area")) {
                player.sendMessage(TextColors.RED + "You are not a member of the " + faction.getColor() + faction.getName() + TextColors.RED + " faction.");
                DGGame.MISC_R.put(model.getMojangId(), "faction-area", false, 4, TimeUnit.SECONDS);
            }

            // Send a fake invisible wall to prevent the player from moving forward
            if (block) {
                /* FIXME Not in Sponge yet
                BlockType wall = Setting.DEBUG_INVISIBLE_WALLS.get() ? BlockTypes.BRICK_BLOCK : BlockTypes.BARRIER;
                Location forwardTop = forward.add(0, 1, 0);
                player.sendBlockChange(forward, wall, (byte) 0);
                player.sendBlockChange(forwardTop, wall, (byte) 0);
                */
            }

            // Cancel the event
            return false;
        }

        // Throttle the welcome message
        if (!DGGame.MISC_R.contains(model.getMojangId(), "faction-area")) {
            player.sendMessage(faction.getColor() + faction.getWelcomeMessage());
            DGGame.MISC_R.put(model.getMojangId(), "faction-area", true, 4, TimeUnit.SECONDS);
        }

        // Don't cancel the event
        return true;
    }

    private void handleClaimAreas(ClaimRoom area, Player player) {
        // Important info
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        Deity deity = area.getDeity();

        String endMessage = TextColors.YELLOW + "You have chosen ";

        // Set the correct type (and potentially faction if the deity is a hero)
        switch (deity.getDeityType()) {
            case GOD:
                model.setGod(deity);
                endMessage += deity.getFaction().getColor() + deity.getName() + TextColors.YELLOW + " as your parent God.";
                break;
            case HERO:
                model.setHero(deity);
                endMessage += deity.getFaction().getColor() + deity.getName() + TextColors.YELLOW + " as your parent Hero.";
                break;
        }

        // Send the appropriate messages
        player.sendMessage(endMessage);

        // Add starting aspects
        for (Aspect.Group group : deity.getAspectGroups()) {
            List<Aspect> inGroup = Groups.aspectsInGroup(group);
            for (Aspect aspect : inGroup) {
                // Hero aspect
                if (DeityType.HERO.equals(deity.getDeityType()) && Aspect.Tier.HERO.equals(aspect.getTier())) {
                    model.giveHeroAspect(deity, aspect);
                    player.sendMessage(TextColors.YELLOW + StringUtils.capitalize(deity.getPronouns()[0]) + " has placed you in the " + deity.getFaction().getColor() + deity.getFaction().getName() + TextColors.YELLOW + " faction.");
                    break;
                }

                // God tier I aspect
                else if (DeityType.GOD.equals(deity.getDeityType()) && Aspect.Tier.I.equals(aspect.getTier())) {
                    model.giveAspect(aspect);
                }
            }
        }

        // If there is a next location, teleport the player to it
        if (area.getNextLocation() != null) {
            player.setLocation(area.getNextLocation());
            player.setRotation(area.getNextRotation());
        }

        // Save the model
        DGGame.PLAYER_R.register(model);
    }
}
