/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.game.util;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.model.PlayerModel;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class TargetingUtil {
    private static final int TARGET_OFFSET = 5;

    @Deprecated
    public static LivingEntity autoTarget(Player player) {
        return autoTarget(player, 140);
    }

    /**
     * Returns the LivingEntity that <code>player</code> is target.
     *
     * @param player the player
     * @return the targeted LivingEntity
     */
    public static LivingEntity autoTarget(Player player, int range) {
        // Define variables
        final int correction = 3;
        Location target = null;
        try {
            target = player.getTargetBlock((Set<Material>) null, range).getLocation();
        } catch (Exception ignored) {
        }
        if (target == null) return null;
        BlockIterator iterator = new BlockIterator(player, range);
        List<Entity> targets = Lists.newArrayList();
        final PlayerModel looking = DGGame.PLAYER_R.fromPlayer(player);

        // Iterate through the blocks and find the target
        while (iterator.hasNext()) {
            final Block block = iterator.next();

            targets.addAll(Collections2.filter(player.getNearbyEntities(range, range, range), new Predicate<Entity>() {
                @Override
                public boolean apply(Entity entity) {
                    if (entity instanceof LivingEntity && entity.getLocation().distance(block.getLocation()) <= correction) {
                        if (entity instanceof Player) {
                            PlayerModel target = DGGame.PLAYER_R.fromPlayer((Player) entity);
                            if (looking.getFaction().equals(target.getFaction()) || ((Player) entity).getGameMode().equals(GameMode.CREATIVE))
                                return false;
                        }
                        return true;
                    }
                    return false;
                }
            }));
        }

        // Attempt to return the closest entity to the cursor
        for (Entity entity : targets)
            if (entity.getLocation().distance(target) <= correction) return (LivingEntity) entity;

        // If it failed to do that then just return the first entity
        try {
            return (LivingEntity) targets.get(0);
        } catch (Exception ignored) {
        }

        return null;
    }

    public static Location directTarget(Player player) {
        return player.getTargetBlock((Set<Material>) null, 140).getLocation();
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
        if (notify) player.sendMessage(ChatColor.RED + "Missed..."); // TODO Better message.
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

        int offset = (int) (TARGET_OFFSET + player.getOfflinePlayer().getPlayer().getLocation().distance(target));
        int adjustedOffset = offset / accuracy;
        if (adjustedOffset < 1) adjustedOffset = 1;
        Random random = new Random();
        World world = target.getWorld();

        int randomInt = random.nextInt(adjustedOffset);
        int sampleSpace = random.nextInt(3);

        double X = target.getX();
        double Z = target.getZ();
        double Y = target.getY();

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

        return new Location(world, X, Y, Z);
    }

    /**
     * Returns true if <code>target</code> is hit at <code>hit</code>.
     *
     * @param target the LivingEntity being targeted
     * @param hit    the location actually hit
     * @return true/false if <code>target</code> is hit
     */
    public static boolean isHit(Location target, Location hit) {
        return hit.distance(target) <= 2;
    }
}
