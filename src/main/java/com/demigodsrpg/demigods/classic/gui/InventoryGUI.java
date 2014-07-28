package com.demigodsrpg.demigods.classic.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public enum InventoryGUI implements IInventoryGUI
{
    ShrineGUI(new ShrineGUI());

    private IInventoryGUI gui;

    private InventoryGUI(IInventoryGUI gui)
    {
        this.gui = gui;
    }

    @Override
    public Inventory getInvenrory() {
        return gui.getInvenrory();
    }

    @Override
    public String getInventoryname() {
        return gui.getInventoryname();
    }

    @Override
    public int getSlots() {
        return gui.getSlots();
    }
}
