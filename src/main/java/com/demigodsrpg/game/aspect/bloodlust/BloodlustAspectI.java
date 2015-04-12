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

package com.demigodsrpg.game.aspect.bloodlust;

import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.util.TargetingUtil;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class BloodlustAspectI implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.BLOODLUST_ASPECT;
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public String getInfo() {
        return "Adept level power over bloodlust.";
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Override
    public String name() {
        return "Bloodthirsty";
    }

    @Ability(name = "Blitz", command = "blitz", info = "Rush to a target entity and deal extra damage.", cost = 170, delay = 3000)
    public AbilityResult blitzAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        LivingEntity target = TargetingUtil.autoTarget(player, 250);

        if (target == null) return AbilityResult.NO_TARGET_FOUND;

        if (player.getLocation().toVector().distance(target.getLocation().toVector()) > 2) {
            float pitch = player.getLocation().getPitch();
            float yaw = player.getLocation().getYaw();
            Location tar = target.getLocation();
            tar.setPitch(pitch);
            tar.setYaw(yaw);
            player.teleport(tar);
            target.damage(2, player);

            player.sendMessage(getGroup().getColor() + "*shooom*");

            return AbilityResult.SUCCESS;
        }
        return AbilityResult.NO_TARGET_FOUND;
    }
}