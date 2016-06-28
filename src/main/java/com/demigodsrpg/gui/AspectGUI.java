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

package com.demigodsrpg.gui;

// TODO This needs major reorganization to make it look nice

import com.demigodsrpg.DGData;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.enchantment.CustomEnchantments;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.InventoryGUI;
import com.demigodsrpg.util.SlotFunction;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// TODO Format this so it is actually a list and not just jumbled together

public class AspectGUI implements InventoryGUI {
    public static final String INVENTORY_NAME = "Aspect Tree";

    private final List<Inventory> INVENTORY_LIST;
    private final ImmutableMap<Integer, String> FUNCTION_MAP;

    public AspectGUI(final Player player) {
        // Player model
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        // FUNCTION MAP
        ImmutableMap.Builder<Integer, String> builder = ImmutableMap.builder();

        builder.put(25, SlotFunction.PREVIOUS_PAGE);
        builder.put(26, SlotFunction.NEXT_PAGE);

        // INVENTORY LIST
        INVENTORY_LIST = new ArrayList<>();
        List<ItemStack> items = new ArrayList<>();
        int count = 0, icount = 0;
        List<Aspect> aspects = model.getPotentialAspects(true);
        Iterator<Aspect> aspectIterator = aspects.iterator();
        while (aspectIterator.hasNext()) {
            Aspect aspect = aspectIterator.next();
            ItemStack item = aspect.getItem().clone();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            if(model.getAspects().contains(aspect.name())) {
                lore.add(ChatColor.YELLOW + "You already have this aspect!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                builder.put(count, SlotFunction.LOCKED);
            } else if(model.canClaim(aspect)) {
                lore.add(ChatColor.GREEN + "This aspect is claimable!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                item = CustomEnchantments.enchant(item, CustomEnchantments.CLAIMABLE, 1, false);
                builder.put(count, SlotFunction.CLAIM);
            } else {
                item.setType(Material.BARRIER);
                lore.add(ChatColor.DARK_GRAY + "This aspect is currently locked.");
                if (!model.hasPrereqs(aspect)) {
                    lore.add(ChatColor.GRAY + "You don't have the prereqs for this aspect.");
                } else if (model.getLevel() < model.costForNextAspect()) {
                    lore.add(ChatColor.GRAY + "You need " + model.costForNextAspect() + " ascensions to claim a new aspect.");
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
                builder.put(count, SlotFunction.LOCKED);
            }

            items.add(count, item);
            count++;

            if (count % 19 == 0 || !aspectIterator.hasNext()) {
                Inventory inventory = Bukkit.createInventory(player, 27, INVENTORY_NAME + " " + icount);
                for (int i = 0; i < items.size(); i++) {
                    inventory.setItem(i, items.get(i));
                }
                if (icount > 0) {
                    inventory.setItem(25, new ItemStack(Material.PAPER, 1) {
                        {
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName(ChatColor.GOLD + "< PREV");
                            setItemMeta(meta);
                        }
                    });
                }
                if (aspectIterator.hasNext()) {
                    inventory.setItem(26, new ItemStack(Material.PAPER, 1) {
                        {
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName(ChatColor.GOLD + "NEXT >");
                            setItemMeta(meta);
                        }
                    });
                }

                items.clear();
                count = 0;

                INVENTORY_LIST.add(inventory);
                icount++;
            }
        }

        FUNCTION_MAP = builder.build();
    }

    @Override
    public Inventory getInventory(Integer... inventory) {
        if (INVENTORY_LIST.size() < 1) {
            return null;
        }
        if (inventory.length == 0) {
            return INVENTORY_LIST.get(0);
        }
        return INVENTORY_LIST.get(inventory[0]);
    }

    @Override
    public String getFunction(int slot) {
        if (FUNCTION_MAP.containsKey(slot)) {
            return FUNCTION_MAP.get(slot);
        }
        return SlotFunction.NO_FUNCTION;
    }
}
