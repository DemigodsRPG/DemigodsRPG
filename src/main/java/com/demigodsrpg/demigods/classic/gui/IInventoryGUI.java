package com.demigodsrpg.demigods.classic.gui;

import org.bukkit.inventory.Inventory;

import java.util.List;

public interface IInventoryGUI
{
    //get the inventory
    public Inventory getInvenrory();

    //Generate inventory
    public String getInventoryname();

    //Get amound of slots
    public int getSlots();

    //Slots
    public enum SlotFunction
    {
        NEXT_PAGE,PREVIOUS_PAGE,RUN_COMMAND,OPEN_NEW_INVENTORY
    }
}
