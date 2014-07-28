package com.demigodsrpg.demigods.classic.gui;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.model.ShrineModel;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ShrineGUI implements IInventoryGUI
{
    private Inventory inv;
    private String invName = "ShrineWarpSelect";
    private int slots = 27;

    public ShrineGUI()
    {

    }

    private void createInventory()
    {
        List<ItemStack> items = Lists.newArrayList();
        inv = Bukkit.createInventory(null, slots, invName);
        int count = 0;
        for(ShrineModel model : DGClassic.SHRINE_R.getRegistered())
        {
            final String name = model.getPersistantId();
            final String type = model.getShrineType().name();
            final String owner = DGClassic.PLAYER_R.fromId(model.getOwnerMojangId()).getLastKnownName();
            if(count == 19)count++;
            items.add(count, new ItemStack(Material.GOLD_BLOCK, 1)
            {
                {
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName(ChatColor.GOLD + name);
                    List<String> lore = Lists.newArrayList(ChatColor.AQUA + type,ChatColor.YELLOW + "Owner: " + ChatColor.LIGHT_PURPLE + owner);
                    meta.setLore(lore);
                    setItemMeta(meta);
                }
            });
            count++;
        }
    }

    @Override
    public Inventory getInvenrory() {
        return inv;
    }

    @Override
    public String getInventoryname() {
        return invName;
    }

    @Override
    public int getSlots() {
        return slots;
    }
}
