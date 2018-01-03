package com.demigodsrpg.gui;

import com.demigodsrpg.DGData;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.model.ShrineModel;
import com.demigodsrpg.util.InventoryGUI;
import com.demigodsrpg.util.SlotFunction;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
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
                            meta.setDisplayName(ChatColor.GOLD + "< PREV");
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
