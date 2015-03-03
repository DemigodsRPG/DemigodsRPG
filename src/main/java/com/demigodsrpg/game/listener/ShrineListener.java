package com.demigodsrpg.game.listener;

// FIXME Shrines and tributing need to be completely redone to reflect that each player only has 1 hero.

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.model.ShrineModel;
import com.demigodsrpg.game.shrine.Shrine;
import com.demigodsrpg.game.util.ZoneUtil;
import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.data.Sign;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.block.BlockBreakEvent;
import org.spongepowered.api.event.block.BlockChangeEvent;
import org.spongepowered.api.event.block.BlockPlaceEvent;
import org.spongepowered.api.event.entity.ExplosionPrimeEvent;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.potion.PotionEffectTypes;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShrineListener {
    @Subscribe(order = Order.LATE)
    public void createShrine(PlayerInteractEvent e) {
        if (!e.getInteractionType().equals(EntityInteractionType.RIGHT_CLICK) || !e.getClickedPosition().isPresent())
            return;
        if (ZoneUtil.isNoDGWorld(e.getPlayer().getWorld())) return;
        Location clickedLocation = new Location(e.getPlayer().getLocation().getExtent(), e.getClickedPosition().get().toDouble());
        if (!BlockTypes.WALL_SIGN.equals(clickedLocation.getBlock().getType()) && !BlockTypes.STANDING_SIGN.equals(clickedLocation.getBlock().getType())) {
            return;
        }
        Sign s = (Sign) clickedLocation.getBlock().getSnapshot(); // TODO Does this work?
        if (!s.getLines()[0].toLegacy().trim().equalsIgnoreCase("shrine")) return;
        if (!s.getLines()[1].toLegacy().trim().equalsIgnoreCase("dedicate")) return;
        Player p = e.getPlayer();
        Deity deity = DGGame.DEITY_R.deityFromName(s.getLines()[2].toLegacy().trim());

        if (deity == null) {
            p.sendMessage(TextColors.YELLOW + "There is no deity by that name.");
            return;
        }

        String shrinename;
        if (s.getLines()[3].toLegacy().trim().length() == 0) {
            p.sendMessage(TextColors.YELLOW + "The shrine requires a name.");
            return;
        } else {
            if (s.getLines()[3].toLegacy().trim().charAt(0) == '#') {
                p.sendMessage(TextColors.YELLOW + "The shrine's name cannot begin with an invalid character.");
                return;
            }
            if (s.getLines()[3].toLegacy().trim().contains(" ")) {
                p.sendMessage(TextColors.YELLOW + "The shrine's name cannot contain a space.");
                return;
            }
            for (Deity d : DGGame.DEITY_R.getRegistered()) {
                if (s.getLines()[3].toLegacy().trim().equalsIgnoreCase(d.getName())) {
                    p.sendMessage(TextColors.YELLOW + "The shrine's name cannot be the same as a deity.");
                    return;
                }
            }
            for (ShrineModel shrine : DGGame.SHRINE_R.getRegistered()) {
                if (shrine.getPersistentId().equals(s.getLines()[3].toLegacy().trim())) {
                    p.sendMessage(TextColors.YELLOW + "A shrine with that name already exists.");
                    return;
                }
            }
            shrinename = "#" + s.getLines()[3].toLegacy().trim();
        }
        for (ShrineModel shrine : DGGame.SHRINE_R.getRegistered()) {
            if (shrine.getLocation().getExtent().equals(clickedLocation.getExtent())) {
                if (clickedLocation.getPosition().distance(shrine.getLocation().getPosition()) < (shrine.getShrineType().getGroundRadius() + 1)) {
                    p.sendMessage(TextColors.YELLOW + "Too close to an existing shrine.");
                    return;
                }
            }
        }
        // conditions cleared
        Shrine type = Shrine.OVERWORLD;
        if (((World) clickedLocation.getExtent()).getDimension().equals(DGGame.GAME.getRegistry().getDimensionType("nether"))) {
            type = Shrine.NETHER;
        }
        ShrineModel shrine = new ShrineModel(shrinename, p, deity, type, clickedLocation);
        DGGame.SHRINE_R.register(shrine);
        shrine.getShrineType().generate(shrine.getLocation());
        // ((World) clickedLocation.getExtent().strikeLightningEffect(e.getClickedBlock().getLocation()); FIXME
        p.sendMessage("You have dedicated this shrine to " + deity.getFaction().getColor() + deity.getName() + TextColors.WHITE + ".");
        p.sendMessage(TextColors.YELLOW + "Warp here at any time with /shrine.");
    }

    @Subscribe(order = Order.LAST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.isCancelled() || ZoneUtil.inNoDGZone(location)) return;
        if (DGGame.SHRINE_R.getShrine(location) != null) {
            event.setCancelled(true);
            if (event.getCause().isPresent() && event.getCause().get().getCause() instanceof Player) {
                ((Player) event.getCause().get().getCause()).sendMessage(TextColors.YELLOW + "That block is protected by a divine force.");
            }
        }
    }

    @Subscribe(order = Order.LAST)
    private void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.isCancelled() || ZoneUtil.inNoDGZone(location)) return;
        if (DGGame.SHRINE_R.getShrine(location) != null) {
            event.setCancelled(true);
            if (event.getCause().isPresent() && event.getCause().get().getCause() instanceof Player) {
                ((Player) event.getCause().get().getCause()).sendMessage(TextColors.YELLOW + "That block is protected by a divine force.");
            }
        }
    }

    /* FIXME This isn't in Sponge 100% yet (can't cancel)
    @Subscribe(order = Order.LAST)
    public void onBlockIgnite(BlockIgniteEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.isCancelled() || ZoneUtil.inNoDGZone(location)) return;
        if (DGGame.SHRINE_R.getShrine(location) != null) {
            event.setCancelled(true);
        }
    }
    */

    @Subscribe(order = Order.LAST)
    public void onBlockDamage(BlockChangeEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.isCancelled() || ZoneUtil.inNoDGZone(location)) return;

        if (event.getCause().isPresent() && event.getCause().get().getCause() instanceof Player) {
            Player player = ((Player) event.getCause().get().getCause());
            String playerId = player.getUniqueId().toString();

            if (DGGame.SHRINE_R.getShrine(location) != null) {
                // Cancel break animation
                DGGame.MISC_R.put(playerId, "NO-BREAK", true);
                player.addPotionEffect(DGGame.GAME.getRegistry().getPotionEffectBuilder().potionType(PotionEffectTypes.MINING_FATIGUE).amplifier(5).duration(9999999).particles(true).build(), true);
                event.setCancelled(true);
            } else if (DGGame.MISC_R.contains(playerId, "NO-BREAK")) {
                // Allow break animation
                DGGame.MISC_R.remove(playerId, "NO-BREAK");
                player.removePotionEffect(PotionEffectTypes.MINING_FATIGUE);
            }
        }
    }

    @Subscribe(order = Order.LAST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.isCancelled()) {
            String playerId = event.getPlayer().getUniqueId().toString();
            Optional<Vector3f> block = event.getClickedPosition();
            if (block.isPresent() && DGGame.MISC_R.contains(playerId, "NO-BREAK")) {
                // Allow break animation
                DGGame.MISC_R.remove(playerId, "NO-BREAK");
                event.getPlayer().removePotionEffect(PotionEffectTypes.MINING_FATIGUE);
            }
        }
    }

    @Subscribe(order = Order.LAST)
    public void onEntityExplode(final ExplosionPrimeEvent event) {
        if (ZoneUtil.inNoDGZone(event.getEntity().getLocation())) return;
        final List<ShrineModel> saves = Lists.newArrayList(DGGame.SHRINE_R.getShrines(event.getEntity().getLocation(), 10));

        DGGame.GAME.getSyncScheduler().runTaskAfter(DGGame.getInst(), () -> {
            // Remove all drops from explosion zone
            for (final ShrineModel save : saves)
                event.getEntity().getWorld().getEntities().stream().filter(drop -> drop instanceof Item && drop.getLocation().getPosition().distance(save.getLocation().getPosition()) <= save.getShrineType().getGroundRadius()).forEach(Entity::remove);
        }, 1);

        if (DGGame.MISC_R.contains("explode-structure", "blaam")) return;
        DGGame.MISC_R.put("explode-structure", "blaam", true, 2, TimeUnit.SECONDS);

        DGGame.GAME.getSyncScheduler().runTaskAfter(DGGame.getInst(), () -> {
            for (final ShrineModel save : saves)
                save.getShrineType().generate(save.getLocation());
        }, 30);
    }
}
