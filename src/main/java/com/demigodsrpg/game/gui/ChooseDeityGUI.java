package com.demigodsrpg.game.gui;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.IAspect;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
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
        final PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

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
        if (!model.getFaction().equals(Faction.NEUTRAL) && model.costForNextDeity() > model.getLevel()) {
            return;
        }

        // Fill the inventory list
        List<ItemStack> items = new ArrayList<>();
        int count = 0, icount = 0;
        Iterator<Aspect> deities = Collections2.filter(Arrays.asList(Aspect.values()), new Predicate<Aspect>() {
            @Override
            public boolean apply(Aspect deity) {
                return model.canClaim(deity);
            }
        }).iterator();

        while (deities.hasNext()) {
            Aspect aspect = deities.next();
            final String name = aspect.name();
            final ChatColor color = aspect.getColor();
            // FIXME final String alliance = aspect.getDefaultAlliance().name();

            // Ignore deities that aren't important
            if (IAspect.Strength.NONE.equals(aspect.getStrength())) continue;

            items.add(count, new ItemStack(aspect.getClaimMaterial().getItemType(), 1, (short) 0, aspect.getClaimMaterial().getData()) {
                {
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName(color + name);
                    List<String> lore = Lists.newArrayList(/* FIXME color + alliance */);
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
