package com.demigodsrpg.demigods.classic.deity.titan.major;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.ability.Ability;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;

public class Cronus implements IDeity {
    @Override
    public String getDeityName() {
        return "Cronus";
    }

    @Override
    public String getNomen() {
        return "Spawn of " + getDeityName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_GRAY;
    }

    @Override
    public Sound getSound() {
        return Sound.BLAZE_BREATH;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.WOOD_HOE);
    }

    @Override
    public IDeity.Importance getImportance() {
        return Importance.MAJOR;
    }

    @Override
    public IDeity.Alliance getDefaultAlliance() {
        return Alliance.TITAN;
    }

    @Override
    public IDeity.Pantheon getPantheon() {
        return Pantheon.TITAN;
    }

    @Ability(name = "Cleave", info = "Attacking with a sickle (hoe) does extra damage.", type = Ability.Type.PASSIVE)
    public void cleaveAbility(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (DGClassic.PLAYER_R.fromPlayer(player).getMajorDeity().equals(Deity.CRONUS)) {
                switch (player.getItemInHand().getType()) {
                    case WOOD_HOE:
                        event.setDamage(7.0);
                        break;
                    case STONE_HOE:
                        event.setDamage(8.0);
                        break;
                    case IRON_HOE:
                        event.setDamage(10.0);
                        break;
                    case GOLD_HOE:
                        event.setDamage(13.0);
                        break;
                    case DIAMOND_HOE:
                        event.setDamage(15.0);
                        break;
                }
            }
        }
    }

    @Ability(name = "Cheat Death", info = "Can only die while being attacked.", type = Ability.Type.PASSIVE)
    public void cheatDeathAbility() {
        // Do nothing, handled directly in the other class
    }

    @Ability(name = "Warp Time", command = "warptime", info = "Warp time to your will.", cost = 700, cooldown = 150000, type = Ability.Type.ULTIMATE)
    public void timeControlAbility(PlayerInteractEvent event) {
        final World world = event.getPlayer().getWorld();
        final long worldTime = world.getFullTime();
        long newTime = worldTime + 12000;
        for (long i = 1; i * 1000 <= newTime; i++) {
            final long delta = i * 1000;
            Bukkit.getScheduler().scheduleSyncDelayedTask(DGClassic.getInst(), new Runnable() {
                @Override
                public void run() {
                    world.setTime(worldTime + delta);
                }
            }, i * 10);
        }
        event.getPlayer().sendMessage(ChatColor.RED + "With a mighty effort, you bend time to your will.");
    }
}