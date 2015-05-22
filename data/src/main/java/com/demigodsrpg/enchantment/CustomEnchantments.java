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

package com.demigodsrpg.enchantment;

import com.demigodsrpg.enchantment.claim.Claimable;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomEnchantments {
    // -- CONSTANTS -- //

    private static final Map<String, Integer> ROMAN_NUMERALS = new LinkedHashMap<>();

    static {
        ROMAN_NUMERALS.put("M", 1000);
        ROMAN_NUMERALS.put("CM", 900);
        ROMAN_NUMERALS.put("D", 500);
        ROMAN_NUMERALS.put("CD", 400);
        ROMAN_NUMERALS.put("C", 100);
        ROMAN_NUMERALS.put("XC", 90);
        ROMAN_NUMERALS.put("L", 50);
        ROMAN_NUMERALS.put("XL", 40);
        ROMAN_NUMERALS.put("X", 10);
        ROMAN_NUMERALS.put("IX", 9);
        ROMAN_NUMERALS.put("V", 5);
        ROMAN_NUMERALS.put("IV", 4);
        ROMAN_NUMERALS.put("I", 1);
    }

    // -- PUBLIC RELEASE -- //

    public static final Claimable CLAIMABLE = new Claimable();

    // -- ENCHANTMENT LIST -- //

    public static final CustomEnchantment[] enchantList = new CustomEnchantment[]{
            CLAIMABLE
    };

    // -- PRIVATE CONSTRUCTOR -- //

    private CustomEnchantments() {
    }

    // -- HELPER METHODS -- //

    public static CustomEnchantment[] values() {
        return enchantList;
    }

    public static CustomEnchantment valueOf(final String name) {
        for (CustomEnchantment enchant : enchantList) {
            if (enchant.getName().equals(name)) {
                return enchant;
            }
        }
        return null;
    }

    public static ItemStack enchant(ItemStack item, CustomEnchantment enchant, int level, boolean tooltip) {
        item.addUnsafeEnchantment(enchant, level);
        if (tooltip) {
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
                String enchantText = ChatColor.RESET + "" + ChatColor.GRAY + entry.getKey().getName();
                if (entry.getKey().getMaxLevel() != 1) {
                    enchantText += " " + getRomanNumeral(entry.getValue());
                }
                lore.add(enchantText);
            }
            if (meta.getLore().size() > 0) {
                lore.add("  ");
                lore.addAll(meta.getLore());
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static String getRomanNumeral(int num) {
        String res = "";
        for (Map.Entry<String, Integer> entry : ROMAN_NUMERALS.entrySet()) {
            int n = num / entry.getValue();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                sb.append(entry.getKey());
            }
            res += sb.toString();
            num = num % entry.getValue();
        }
        return res;
    }
}
