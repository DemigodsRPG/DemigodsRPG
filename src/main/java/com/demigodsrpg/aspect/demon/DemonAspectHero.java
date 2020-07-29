package com.demigodsrpg.aspect.demon;


import com.demigodsrpg.DGData;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.*;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class DemonAspectHero implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.DEMON_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.SKELETON_SKULL, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return -3;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Blood of a demon."};
    }

    @Override
    public Tier getTier() {
        return Tier.HERO;
    }

    @Override
    public String getName() {
        return "Demonlord";
    }

    @Ability(name = "Demon Friendlies", info = "Demon monsters will not attack you.", type = Ability.Type.PASSIVE,
            placeholder = true)
    public void friendlyAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }

    @Ability(name = "Curse", command = "curse", info = "Turns day to night as nothingness corrupts your enemies.",
            cost = 4000, cooldown = 600000, type = Ability.Type.ULTIMATE)
    public AbilityResult curseAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        int amt = tartarus(player, model);
        if (amt > 0) {
            player.sendMessage(
                    ChatColor.DARK_RED + "Nothingness" + ChatColor.GRAY + " has corrupted " + amt + " enemies.");
            player.getWorld().setTime(18000);
            return AbilityResult.SUCCESS;
        } else {
            player.sendMessage(ChatColor.YELLOW + "There were no valid targets or the ultimate could not be used.");
        }
        return AbilityResult.OTHER_FAILURE;
    }

    private int tartarus(Player p, PlayerModel m) {
        int range = (int) Math.round(18.83043 * Math.pow(m.getExperience(Aspects.DEMON_ASPECT_HERO), 0.088637));
        List<LivingEntity> entityList = new ArrayList<>();
        for (Entity anEntity : p.getNearbyEntities(range, range, range)) {
            if (anEntity instanceof Player &&
                    m.getFamily().equals(DGData.PLAYER_R.fromPlayer((Player) anEntity).getFamily())) {
                continue;
            }
            if (anEntity instanceof LivingEntity) {
                entityList.add((LivingEntity) anEntity);
            }
        }
        int duration = (int) Math.round(30 * Math.pow(m.getExperience(Aspects.DEMON_ASPECT_HERO), 0.09)) * 20;
        for (LivingEntity le : entityList) {
            target(le, duration);
        }
        return entityList.size();
    }

    private void target(LivingEntity le, int duration) {
        le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 5));
        le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, 5));
        le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration, 5));
    }
}