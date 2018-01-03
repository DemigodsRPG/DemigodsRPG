package com.demigodsrpg.enchantment;

import org.bukkit.enchantments.*;
import org.bukkit.inventory.ItemStack;

public abstract class CustomEnchantment extends EnchantmentWrapper {
    public CustomEnchantment(int id) {
        super(id);
    }

    @Override
    public abstract boolean canEnchantItem(ItemStack item);

    @Override
    public abstract boolean conflictsWith(Enchantment other);

    @Override
    public abstract EnchantmentTarget getItemTarget();

    @Override
    public abstract int getMaxLevel();

    @Override
    public abstract String getName();

    @Override
    public abstract int getStartLevel();

    public abstract CustomEnchantmentType getType();
}
