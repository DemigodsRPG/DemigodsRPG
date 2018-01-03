package com.demigodsrpg.util;

import org.bukkit.inventory.Inventory;

public interface InventoryGUI {
    //Get the inventory
    Inventory getInventory(Integer... inventory);

    //Get the function of the slot
    String getFunction(int slot);
}
