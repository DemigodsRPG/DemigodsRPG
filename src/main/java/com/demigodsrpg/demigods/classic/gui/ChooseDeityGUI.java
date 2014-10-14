package com.demigodsrpg.demigods.classic.gui;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ChooseDeityGUI implements IInventoryGUI {
    public static final String INVENTORY_NAME = "Choose Deity";

    private final List<Inventory> INVENTORY_LIST;
    private final ImmutableMap<Integer, SlotFunction> FUNCTION_MAP;

    public ChooseDeityGUI(final Player player) {
        // Get the player model
        final PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);

        // FUNCTION MAP
        ImmutableMap.Builder<Integer, SlotFunction> builder = ImmutableMap.builder();

        for (int i = 0; i < 18; i++) {
            builder.put(i, SlotFunction.RUN_COMMAND);
        }

        builder.put(25, SlotFunction.PREVIOUS_PAGE);
        builder.put(26, SlotFunction.NEXT_PAGE);

        FUNCTION_MAP = builder.build();

        // INVENTORY LIST
        INVENTORY_LIST = new ArrayList<>();

        // Return if can't claim a new deity
        if (!model.getAlliance().equals(IDeity.Alliance.NEUTRAL) && model.costForNextDeity() > model.getAscensions()) {
            return;
        }

        // Fill the inventory list
        List<ItemStack> items = new ArrayList<>();
        int count = 0, icount = 0;
        Iterator<Deity> deities = Collections2.filter(Arrays.asList(Deity.values()), new Predicate<Deity>() {
            @Override
            public boolean apply(Deity deity) {
                return model.canClaim(deity);
            }
        }).iterator();

        while (deities.hasNext()) {
            Deity deity = deities.next();
            final String name = deity.name();
            final ChatColor color = deity.getColor();
            final String alliance = deity.getDefaultAlliance().name();

            items.add(count, new ItemStack(deity.getClaimMaterial().getItemType(), 1, (short) 0, deity.getClaimMaterial().getData()) {
                {
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName(color + name);
                    List<String> lore = Lists.newArrayList(color + alliance);
                    meta.setLore(lore);
                    setItemMeta(meta);
                }
            });

            count++;

            if (count % 19 == 0 || !deities.hasNext()) {
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
                if (deities.hasNext()) {
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
    public SlotFunction getFunction(int slot) {
        if (FUNCTION_MAP.containsKey(slot)) {
            return FUNCTION_MAP.get(slot);
        }
        return SlotFunction.NO_FUNCTION;
    }
}
