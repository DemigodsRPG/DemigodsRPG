package com.demigodsrpg.aspect.bloodlust;

import com.demigodsrpg.DGData;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class BloodlustAspectIII implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.BLOODLUST_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.REDSTONE, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Mastery over bloodlust."};
    }

    @Override
    public Tier getTier() {
        return Tier.III;
    }

    @Override
    public String getName() {
        return "Bloody Sacrament";
    }

    @Ability(name = "Mighty Fists", info = "Attacking with no item does extra damage.", type = Ability.Type.PASSIVE)
    public AbilityResult fistsAbility(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (DGData.PLAYER_R.fromPlayer(player).getAspects().contains(getGroup() + " " + getTier().name())) {
                if (player.getItemInHand().getType().equals(Material.AIR)) {
                    event.setDamage(15.0);
                }
            }
        }

        return AbilityResult.SUCCESS;
    }
}