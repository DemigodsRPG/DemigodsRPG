package com.demigodsrpg.aspect.magnetism;


import com.demigodsrpg.DGData;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.*;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ItemUtil;
import com.demigodsrpg.util.TargetingUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class MagnetismAspectHero implements Aspect {

    // -- ASPECT META -- //

    @Override
    public Group getGroup() {
        return Groups.MAGNETISM_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.IRON_SPADE, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Expert level power over magnetism."};
    }

    @Override
    public Tier getTier() {
        return Tier.HERO;
    }

    @Override
    public String getName() {
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
