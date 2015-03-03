package com.demigodsrpg.game.gui;

import org.spongepowered.api.item.inventory.Inventory;

interface IInventoryGUI {
    //Get the inventory
    public Inventory getInventory(Integer... inventory);

    //Get the function of the slot
    public SlotFunction getFunction(int slot);
}
