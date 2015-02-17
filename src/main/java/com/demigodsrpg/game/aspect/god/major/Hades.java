package com.demigodsrpg.game.aspect.god.major;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.TargetingUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Hades implements Aspect {
    @Override
    public String getDeityName() {
        return "Hades";
    }

    @Override
    public String getNomen() {
        return "child of " + getDeityName();
    }

    @Override
    public String getInfo() {
        return "God of the dead.";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public Sound getSound() {
        return Sound.GHAST_DEATH;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.BONE);
    }

    @Override
    public Tier getImportance() {
        return Tier.MAJOR;
    }

    @Override
    public Aspect.Alliance getDefaultAlliance() {
        return Alliance.OLYMPIAN;
    }

    @Override
    public Aspect.Pantheon getPantheon() {
        return Pantheon.OLYMPIAN;
    }

    @Ability(name = "Mob Friendlies", info = "Undead monsters will not attack you.", type = Ability.Type.PASSIVE, placeholder = true)
    public void friendlyAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }

    @Ability(name = "Chain", command = "chain", info = "Fire a chain of smoke that damages and blinds.", cost = 250, delay = 1500)
    public AbilityResult chainAbility(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        double devotion = model.getExperience(Aspects.HADES);
        double damage = Math.round(Math.pow(devotion, 0.20688));
        int blindpower = (int) Math.round(1.26985 * Math.pow(devotion, 0.13047));
        int blindduration = (int) Math.round(0.75 * Math.pow(devotion, 0.323999));
        chain(player, damage, blindpower, blindduration);

        return AbilityResult.SUCCESS;
    }

    @Ability(name = "Entomb", command = "entomb", info = "Entomb an entity in obsidian.", cost = 470, cooldown = 20000)
    public AbilityResult entombAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        LivingEntity le = TargetingUtil.autoTarget(player);
        if (le == null) return AbilityResult.NO_TARGET_FOUND;
        int duration = (int) Math.round(2.18678 * Math.pow(model.getExperience(Aspects.HADES), 0.24723)); // seconds
        final ArrayList<Block> tochange = new ArrayList<Block>();
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    Block block = player.getWorld().getBlockAt(le.getLocation().getBlockX() + x, le.getLocation().getBlockY() + y, le.getLocation().getBlockZ() + z);
                    if ((block.getLocation().distance(le.getLocation()) > 2) && (block.getLocation().distance(le.getLocation()) < 3.5))
                        if ((block.getType() == Material.AIR) || (block.getType() == Material.WATER) || (block.getType() == Material.LAVA)) {
                            block.setType(Material.OBSIDIAN);
                            tochange.add(block);
                        }
                }
            }
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DGGame.getInst(), new Runnable() {
            @Override
            public void run() {
                for (Block b : tochange)
                    if (b.getType() == Material.OBSIDIAN) b.setType(Material.AIR);
            }
        }, duration * 20);

        return AbilityResult.SUCCESS;
    }

    @Ability(name = "Curse", command = "curse", info = "Turns day to night as Hades curses your enemies.", cost = 4000, cooldown = 600000, type = Ability.Type.ULTIMATE)
    public AbilityResult curseAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        int amt = tartarus(player, model);
        if (amt > 0) {
            player.sendMessage(ChatColor.DARK_RED + "Hades" + ChatColor.GRAY + " curses " + amt + " enemies.");
            player.getWorld().setTime(18000);
            return AbilityResult.SUCCESS;
        } else {
            player.sendMessage(ChatColor.YELLOW + "There were no valid targets or the ultimate could not be used.");
        }
        return AbilityResult.OTHER_FAILURE;
    }

    private boolean chain(Player p, double damage, int blindpower, int blindduration) {
        LivingEntity target = TargetingUtil.autoTarget(p);
        if (target == null) return false;
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, blindduration, blindpower));
        target.damage(damage);
        target.setLastDamageCause(new EntityDamageByEntityEvent(p, target, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
        for (BlockFace bf : BlockFace.values()) {
            p.getWorld().playEffect(target.getLocation().getBlock().getRelative(bf).getLocation(), Effect.SMOKE, 1);
        }
        return true;
    }

    private int tartarus(Player p, PlayerModel m) {
        int range = (int) Math.round(18.83043 * Math.pow(m.getExperience(Aspects.HADES), 0.088637));
        List<LivingEntity> entitylist = new ArrayList<>();
        for (Entity anEntity : p.getNearbyEntities(range, range, range)) {
            if (anEntity instanceof Player && m.getFaction().equals(DGGame.PLAYER_R.fromPlayer((Player) anEntity).getFaction())) {
                continue;
            }
            if (anEntity instanceof LivingEntity) {
                entitylist.add((LivingEntity) anEntity);
            }
        }
        int duration = (int) Math.round(30 * Math.pow(m.getExperience(Aspects.HADES), 0.09)) * 20;
        for (LivingEntity le : entitylist) {
            target(le, duration);
        }
        return entitylist.size();
    }

    private void target(LivingEntity le, int duration) {
        le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 5));
        le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, 5));
        le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration, 5));
    }
}