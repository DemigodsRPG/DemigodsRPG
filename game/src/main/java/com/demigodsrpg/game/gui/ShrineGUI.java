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

package com.demigodsrpg.game.gui;

import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.model.PlayerModel;
import com.demigodsrpg.data.model.ShrineModel;
import com.demigodsrpg.util.InventoryGUI;
import com.demigodsrpg.util.SlotFunction;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
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
import java.util.stream.Collectors;

public class ShrineGUI implements InventoryGUI {
    public static final String INVENTORY_NAME = "Shrine Select";

    private final List<Inventory> INVENTORY_LIST;
    private final ImmutableMap<Integer, String> FUNCTION_MAP;

    public ShrineGUI(final Player player) {
        // Player model
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        // FUNCTION MAP
        ImmutableMap.Builder<Integer, String> builder = ImmutableMap.builder();

        for (int i = 0; i < 18; i++) {
            builder.put(i, SlotFunction.WARP);
        }

        builder.put(25, SlotFunction.PREVIOUS_PAGE);
        builder.put(26, SlotFunction.NEXT_PAGE);

        FUNCTION_MAP = builder.build();

        // INVENTORY LIST
        INVENTORY_LIST = new ArrayList<>();
        List<ItemStack> items = new ArrayList<>();
        int count = 0, icount = 0;
        Iterator<ShrineModel> shrines = model.getShrineWarps().stream().map(DGData.SHRINE_R::fromId).collect(Collectors.toSet()).iterator();
        while (shrines.hasNext()) {
            ShrineModel shrine = shrines.next();
            final String name = shrine.getPersistentId();
            final String type = shrine.getShrineType().name();
            final String owner = DGData.PLAYER_R.fromId(shrine.getOwnerMojangId()).getLastKnownName();

            items.add(count, new ItemStack(owner.equals(model.getLastKnownName()) ? Material.ENCHANTED_BOOK : Material.WRITTEN_BOOK, 1) {
                {
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName(name);
                    List<String> lore = Lists.newArrayList(ChatColor.AQUA + type, ChatColor.YELLOW + "Owner: " + ChatColor.LIGHT_PURPLE + owner);
                    meta.setLore(lore);
                    setItemMeta(meta);
                }
            });

            count++;

            if (count % 19 == 0 || !shrines.hasNext()) {
                Inventory inventory = Bukkit.createInventory(player, 27, INVENTORY_NAME + " " + icount);
                for (int i = 0; i < items.size(); i++) {
                    inventory.setItem(i, items.get(i));
                }
                if (icount > 0) {
                    inventory.setItem(25, new ItemStack(Material.PAPER, 1) {
                        {
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName(ChatColor.GOLD + "< BACK");
                            setItemMeta(meta);
                        }
                    });
                }
                if (shrines.hasNext()) {
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
