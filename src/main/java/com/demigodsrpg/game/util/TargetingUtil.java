package com.demigodsrpg.game.util;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.model.PlayerModel;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.gamemode.GameModes;
import org.spongepowered.api.entity.projectile.EnderPearl;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TargetingUtil {
    private static final int TARGET_OFFSET = 5;

    @Deprecated
    public static Living autoTarget(Player player) {
        return autoTarget(player, 140);
    }

    // FIXME Line of sight is missing from the Sponge API still

    /**
     * Returns the LivingEntity that <code>player</code> is target.
     *
     * @param player the player
     * @return the targeted LivingEntity
     */
    public static Living autoTarget(Player player, int range) {
        // Define variables
        final int correction = 3;
        Vector3d velocity = null;
        try {
            velocity = targetVelocity(player); // FIXME This is a velocity not a target position
        } catch (Exception ignored) {
        }
        if (velocity == null) return null;
        List<Vector3d> blocks = getNearbyBlocks(player.getLocation(), range);
        List<Entity> targets = new ArrayList<>();
        final PlayerModel looking = DGGame.PLAYER_R.fromPlayer(player);

        // Iterate through the blocks and find the target
        for (Vector3d pos : blocks) {
            targets.addAll(getNearbyEntities(player.getLocation(), range).stream().filter(new Predicate<Entity>() {
                @Override
                public boolean test(Entity entity) {
                    if (entity instanceof Living && entity.getLocation().getPosition().distance(pos) <= correction) {
                        if (entity instanceof Player) {
                            PlayerModel target = DGGame.PLAYER_R.fromPlayer((Player) entity);
                            if (looking.getFaction().equals(target.getFaction()) || ((Player) entity).getGameMode().equals(GameModes.CREATIVE))
                                return false;
                        }
                        return true;
                    }
                    return false;
                }
            }).collect(Collectors.toList()));
        }

        // Attempt to return the closest entity to the cursor
        for (Entity entity : targets)
            if (entity.getLocation().getPosition().distance(velocity) <= correction) return (Living) entity;

        // If it failed to do that then just return the first entity
        try {
            return (Living) targets.get(0);
        } catch (Exception ignored) {
        }

        return null;
    }

    @Deprecated
    public static Location directTarget(Player player) {
        return new Location(player.getWorld(), targetVelocity(player));
    }

    public static Vector3d targetVelocity(Player player) {
        EnderPearl ep = player.launchProjectile(EnderPearl.class);
        try {
            return ep.getVelocity();
        } finally {
            ep.remove();
        }
    }

    public static Collection<Entity> getNearbyEntities(Location center, double range) {
        Vector3d centerPos = center.getPosition();
        return center.getExtent().getEntities(entity -> entity.getLocation().getPosition().distance(centerPos) <= range);
    }

    public static List<Vector3d> getNearbyBlocks(Location center, int range) {
        List<Vector3d> blockPos = new ArrayList<>();
        for (int x = -range; x < range; x++) {
            for (int y = -range; y < range; y++) {
                for (int z = -range; z < range; z++) {
                    blockPos.add(new Vector3d(x, y, z));
                }
            }
        }
        return blockPos;
    }

    /**
     * Returns true if the <code>player</code> ability hits <code>target</code>.
     *
     * @param player the player using the ability
     * @param target the targeted LivingEntity
     * @return true/false depending on if the ability hits or misses
     */
    public static boolean target(Player player, Location target, boolean notify) {
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        Location toHit = adjustedAimLocation(model, target);
        if (isHit(target, toHit)) return true;
        if (notify) player.sendMessage(TextColors.RED + "Missed..."); // TODO Better message.
        return false;
    }

    /**
     * Returns the location that <code>character</code> is actually aiming
     * at when target <code>target</code>.
     *
     * @param player the character triggering the ability callAbilityEvent
     * @param target the location the character is target at
     * @return the aimed at location
     */
    public static Location adjustedAimLocation(PlayerModel player, Location target) {
        // FIXME: This needs major work.

        int accuracy = 15;

        int offset = (int) (TARGET_OFFSET + player.getPlayer().getVelocity().distance(target.getPosition()));
        int adjustedOffset = offset / accuracy;
        if (adjustedOffset < 1) adjustedOffset = 1;
        Random random = new Random();
        World world = (World) target.getExtent();

        int randomInt = random.nextInt(adjustedOffset);
        int sampleSpace = random.nextInt(3);

        double X = target.getPosition().getX();
        double Z = target.getPosition().getZ();
        double Y = target.getPosition().getY();

        if (sampleSpace == 0) {
            X += randomInt;
            Z += randomInt;
        } else if (sampleSpace == 1) {
            X -= randomInt;
            Z -= randomInt;
        } else if (sampleSpace == 2) {
            X -= randomInt;
            Z += randomInt;
        } else if (sampleSpace == 3) {
            X += randomInt;
            Z -= randomInt;
        }

        return new Location(world, new Vector3d(X, Y, Z));
    }

    /**
     * Returns true if <code>target</code> is hit at <code>hit</code>.
     *
     * @param target the LivingEntity being targeted
     * @param hit    the location actually hit
     * @return true/false if <code>target</code> is hit
     */
    public static boolean isHit(Location target, Location hit) {
        return hit.getPosition().distance(target.getPosition()) <= 2;
    }
}
