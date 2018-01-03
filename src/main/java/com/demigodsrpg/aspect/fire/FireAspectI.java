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

package com.demigodsrpg.aspect.fire;


import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.util.ItemUtil;
import com.demigodsrpg.util.TargetingUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class FireAspectI implements Aspect {

    // -- ASPECT META -- //

    @Override
    public Group getGroup() {
        return Groups.FIRE_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.FIREBALL, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Adept level power over fire."};
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Override
    public String getName() {
        return "Pyro";
    }

    // -- ABILITIES -- //

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

        player.sendMessage(getGroup().getColor() + "*fhhoom*");

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
