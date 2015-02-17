package com.demigodsrpg.game.aspect.titan;

import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.util.TargetingUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class Prometheus implements Aspect {
    @Override
    public String getDeityName() {
        return "Prometheus";
    }

    @Override
    public String getNomen() {
        return "agent of " + getDeityName();
    }

    @Override
    public String getInfo() {
        return "Titan of humanity and forethought.";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }

    @Override
    public Sound getSound() {
        return Sound.FIRE;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.FIREBALL);
    }

    @Override
    public Tier getImportance() {
        return Tier.MINOR;
    }

    @Override
    public Alliance getDefaultAlliance() {
        return Alliance.OLYMPIAN;
    }

    @Override
    public Pantheon getPantheon() {
        return Pantheon.TITAN;
    }

    @Ability(name = "Fireball", command = "fireball", info = "Bring fire to your enemies.", cost = 120, delay = 2000)
    public AbilityResult fireballAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        LivingEntity targetEntity = TargetingUtil.autoTarget(player, 250);
        Location target;

        if (targetEntity != null) {
            target = targetEntity.getLocation();
        } else {
            target = TargetingUtil.directTarget(player);
        }

        shootFireball(player.getEyeLocation(), target, player);

        player.sendMessage(getColor() + "*fhhoom*");

        return AbilityResult.SUCCESS;
    }

    public static void shootFireball(Location from, Location to, Player shooter) {
        Fireball fireball = (org.bukkit.entity.Fireball) shooter.getWorld().spawnEntity(from, EntityType.FIREBALL);
        to.setX(to.getX() + .5);
        to.setY(to.getY() + .5);
        to.setZ(to.getZ() + .5);
        Vector path = to.toVector().subtract(from.toVector());
        Vector victor = from.toVector().add(from.getDirection().multiply(2));
        fireball.teleport(new Location(shooter.getWorld(), victor.getX(), victor.getY(), victor.getZ()));
        fireball.setDirection(path);
        fireball.setShooter(shooter);
    }

    @Ability(name = "No Fire Damage", info = "Fire will not damage you.", type = Ability.Type.PASSIVE, placeholder = true)
    public void noFireDamageAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
