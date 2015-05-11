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

package com.demigodsrpg.aspect.demon;

import com.censoredsoftware.library.bukkitutil.ItemUtil;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DemonAspectHero implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.DEMON_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.SKULL_ITEM, name(), Collections.singletonList(getInfo()), null);
    }

    @Override
    public int getId() {
        return -3;
    }

    @Override
    public String getInfo() {
        return "Blood of a demon.";
    }

    @Override
    public Tier getTier() {
        return Tier.HERO;
    }

    @Override
    public String name() {
        return "Demonlord";
    }

    @Ability(name = "Demon Friendlies", info = "Demon monsters will not attack you.", type = Ability.Type.PASSIVE, placeholder = true)
    public void friendlyAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }

    @Ability(name = "Curse", command = "curse", info = "Turns day to night as nothingness corrupts your enemies.", cost = 4000, cooldown = 600000, type = Ability.Type.ULTIMATE)
    public AbilityResult curseAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        int amt = tartarus(player, model);
        if (amt > 0) {
            player.sendMessage(ChatColor.DARK_RED + "Nothingness" + ChatColor.GRAY + " has corrupted " + amt + " enemies.");
            player.getWorld().setTime(18000);
            return AbilityResult.SUCCESS;
        } else {
            player.sendMessage(ChatColor.YELLOW + "There were no valid targets or the ultimate could not be used.");
        }
        return AbilityResult.OTHER_FAILURE;
    }

    private int tartarus(Player p, PlayerModel m) {
        int range = (int) Math.round(18.83043 * Math.pow(m.getExperience(Aspects.DEMON_ASPECT_HERO), 0.088637));
        List<LivingEntity> entitylist = new ArrayList<>();
        for (Entity anEntity : p.getNearbyEntities(range, range, range)) {
            if (anEntity instanceof Player && m.getFaction().equals(DGData.PLAYER_R.fromPlayer((Player) anEntity).getFaction())) {
                continue;
            }
            if (anEntity instanceof LivingEntity) {
                entitylist.add((LivingEntity) anEntity);
            }
        }
        int duration = (int) Math.round(30 * Math.pow(m.getExperience(Aspects.DEMON_ASPECT_HERO), 0.09)) * 20;
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