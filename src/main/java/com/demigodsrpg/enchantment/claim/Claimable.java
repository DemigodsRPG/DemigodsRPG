package com.demigodsrpg.enchantment.claim;

import com.demigodsrpg.enchantment.CustomEnchantment;
import com.demigodsrpg.enchantment.CustomEnchantmentType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Claimable extends CustomEnchantment {
    public Claimable() {
        super(150);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return other instanceof CustomEnchantment && ((CustomEnchantment) other).getType().equals(getType());
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "Claimable";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public CustomEnchantmentType getType() {
        return CustomEnchantmentType.CLAIM_STATUS;
    }
}
