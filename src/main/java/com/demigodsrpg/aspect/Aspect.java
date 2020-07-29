package com.demigodsrpg.aspect;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public interface Aspect {
    default Group getGroup() {
        return null;
    }

    default ItemStack getItem() {
        return null;
    }

    int getId();

    String[] getInfo();

    Tier getTier();

    String getName();

    default String name() {
        return getName();
    }

    enum Tier {
        I, II, III, HERO, CUSTOM
    }

    interface Group {
        String getName();

        ChatColor getColor();

        Sound getSound();

        BlockData getClaimMaterial();
    }
}
