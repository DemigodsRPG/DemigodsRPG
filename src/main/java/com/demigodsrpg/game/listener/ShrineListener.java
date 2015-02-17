package com.demigodsrpg.game.listener;

// FIXME Shrines and tributing need to be completely redone to reflect that each player only has 1 god and 1 hero.

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.model.ShrineModel;
import com.demigodsrpg.game.shrine.Shrine;
import com.demigodsrpg.game.util.ZoneUtil;
import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShrineListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void createShrine(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (ZoneUtil.isNoDGCWorld(e.getPlayer().getWorld())) return;
        if (e.getClickedBlock().getType() != Material.SIGN && e.getClickedBlock().getType() != Material.SIGN_POST)
            return;
        Sign s = (Sign) e.getClickedBlock().getState();
        if (!s.getLines()[0].trim().equalsIgnoreCase("shrine")) return;
        if (!s.getLines()[1].trim().equalsIgnoreCase("dedicate")) return;
        Aspects aspect = null;
        Player p = e.getPlayer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(p);
        for (String dN : model.getAspects()) {
            Aspects d = Aspects.valueOf(dN);
            if (s.getLines()[2].trim().equalsIgnoreCase(d.getName())) {
                aspect = d;
                break;
            }
        }
        if (aspect == null) {
            p.sendMessage(ChatColor.YELLOW + "There is no deity by that name.");
            return;
        }
        String shrinename;
        if (s.getLines()[3].trim().length() == 0) {
            p.sendMessage(ChatColor.YELLOW + "The shrine requires a name.");
            return;
        } else {
            if (s.getLines()[3].trim().charAt(0) == '#') {
                p.sendMessage(ChatColor.YELLOW + "The shrine's name cannot begin with an invalid character.");
                return;
            }
            if (s.getLines()[3].trim().contains(" ")) {
                p.sendMessage(ChatColor.YELLOW + "The shrine's name cannot contain a space.");
                return;
            }
            for (Aspects d : Aspects.values()) {
                if (s.getLines()[3].trim().equalsIgnoreCase(d.getName())) {
                    p.sendMessage(ChatColor.YELLOW + "The shrine's name cannot be the same as a deity.");
                    return;
                }
            }
            for (ShrineModel shrine : DGGame.SHRINE_R.getRegistered()) {
                if (shrine.getPersistentId().equals(s.getLines()[3].trim())) {
                    p.sendMessage(ChatColor.YELLOW + "A shrine with that name already exists.");
                    return;
                }
            }
            shrinename = "#" + s.getLines()[3].trim();
        }
        for (ShrineModel shrine : DGGame.SHRINE_R.getRegistered()) {
            if (shrine.getLocation().getWorld().equals(e.getClickedBlock().getWorld())) {
                if (e.getClickedBlock().getLocation().distance(shrine.getLocation()) < (shrine.getShrineType().getGroundRadius() + 1)) {
                    p.sendMessage(ChatColor.YELLOW + "Too close to an existing shrine.");
                    return;
                }
            }
        }
        // conditions cleared
        Shrine type = Shrine.OVERWORLD;
        if (e.getClickedBlock().getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            type = Shrine.NETHER;
        }
        ShrineModel shrine = new ShrineModel(shrinename, p, aspect, type, e.getClickedBlock().getLocation());
        DGGame.SHRINE_R.register(shrine);
        shrine.getShrineType().generate(shrine.getPoint());
        e.getClickedBlock().getWorld().strikeLightningEffect(e.getClickedBlock().getLocation());
        p.sendMessage("You have dedicated this shrine to " + aspect.getColor() + aspect.getName() + ChatColor.WHITE + ".");
        p.sendMessage(ChatColor.YELLOW + "Warp here at any time with /shrine.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.isCancelled() || ZoneUtil.inNoDGCZone(location)) return;
        if (DGGame.SHRINE_R.getShrine(location) != null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "That block is protected by a divine force.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.isCancelled() || ZoneUtil.inNoDGCZone(location)) return;
        if (DGGame.SHRINE_R.getShrine(location) != null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "That block is protected by a divine force.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(BlockIgniteEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.isCancelled() || ZoneUtil.inNoDGCZone(location)) return;
        if (DGGame.SHRINE_R.getShrine(location) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamage(BlockDamageEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.isCancelled() || ZoneUtil.inNoDGCZone(location)) return;

        String playerId = event.getPlayer().getUniqueId().toString();

        if (DGGame.SHRINE_R.getShrine(location) != null) {
            // Cancel break animation
            DGGame.SERV_R.put(playerId, "NO-BREAK", true);
            event.getPlayer().addPotionEffect(PotionEffectType.SLOW_DIGGING.createEffect(9999999, 5), true);
            event.setCancelled(true);
        } else if (DGGame.SERV_R.contains(playerId, "NO-BREAK")) {
            // Allow break animation
            DGGame.SERV_R.remove(playerId, "NO-BREAK");
            event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        String playerId = event.getPlayer().getUniqueId().toString();
        Block block = event.getClickedBlock();
        if (block == null && DGGame.SERV_R.contains(playerId, "NO-BREAK")) {
            // Allow break animation
            DGGame.SERV_R.remove(playerId, "NO-BREAK");
            event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled() || ZoneUtil.inNoDGCZone(event.getBlock().getLocation())) return;
        for (Block block : event.getBlocks()) {
            if (DGGame.SHRINE_R.getShrine(block.getLocation()) != null) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.isCancelled() || ZoneUtil.inNoDGCZone(location)) return;
        if (DGGame.SHRINE_R.getShrine(location) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (event.getEntity() == null || ZoneUtil.inNoDGCZone(event.getEntity().getLocation())) return;
        final List<ShrineModel> saves = Lists.newArrayList(DGGame.SHRINE_R.getShrines(event.getLocation(), 10));

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DGGame.getInst(), new Runnable() {
            @Override
            public void run() {
                // Remove all drops from explosion zone
                for (final ShrineModel save : saves)
                    for (Item drop : event.getLocation().getWorld().getEntitiesByClass(Item.class))
                        if (drop.getLocation().distance(save.getLocation()) <= save.getShrineType().getGroundRadius())
                            drop.remove();
            }
        }, 1);

        if (DGGame.SERV_R.contains("explode-structure", "blaam")) return;
        DGGame.SERV_R.put("explode-structure", "blaam", true, 2, TimeUnit.SECONDS);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DGGame.getInst(), new Runnable() {
            @Override
            public void run() {
                for (final ShrineModel save : saves)
                    save.getShrineType().generate(save.getPoint());
            }
        }, 30);
    }
}
