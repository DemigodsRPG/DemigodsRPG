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

package com.demigodsrpg.aspect.magnetism;

import com.censoredsoftware.library.bukkitutil.ItemUtil;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.model.PlayerModel;
import com.demigodsrpg.util.TargetingUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collections;

public class MagnetismAspectHero implements Aspect {

    // -- ASPECT META -- //

    @Override
    public Group getGroup() {
        return Groups.MAGNETISM_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.IRON_SPADE, name(), Collections.singletonList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public String getInfo() {
        return "Expert level power over magnetism.";
    }

    @Override
    public Tier getTier() {
        return Tier.HERO;
    }

    @Override
    public String name() {
        return "Fundamental Energy";
    }

    // -- ABILITIES -- //

    @Ability(name = "Shove", command = "shove", info = "Use the force of wind to shove your enemies.", cost = 170, delay = 1500)
    public AbilityResult pullAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        double devotion = model.getExperience(Aspects.MAGNETISM_ASPECT_HERO);
        double multiply = 0.1753 * Math.pow(devotion, 0.322917);

        LivingEntity hit = TargetingUtil.autoTarget(player);

        if (hit != null) {
            player.sendMessage(ChatColor.YELLOW + "*whoosh*");

            Vector v = player.getLocation().toVector();
            Vector victor = hit.getLocation().toVector().subtract(v);
            victor.multiply(multiply);
            hit.setVelocity(victor);

            return AbilityResult.SUCCESS;
        }

        return AbilityResult.NO_TARGET_FOUND;
    }


    @Ability(name = "No Fall Damage", info = "Take no fall damage.", type = Ability.Type.PASSIVE, placeholder = true)
    public void noFallDamageAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
