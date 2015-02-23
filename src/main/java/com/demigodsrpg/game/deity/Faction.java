package com.demigodsrpg.game.deity;

import org.bukkit.ChatColor;

public class Faction {

    // -- ALWAYS EXISTING FACTIONS -- //

    public static final Faction NEUTRAL = new Faction("Neutral", ChatColor.GRAY, "N");
    public static final Faction EXCOMMUNICATED = new Faction("Excommunicated", ChatColor.DARK_GRAY, "X");

    // -- FACTION META DATA -- //

    private String name;
    private ChatColor color;
    private String chatSymbol;

    // -- CONSTRUCTOR -- //

    public Faction(String name, ChatColor color, String chatSymbol) {
        this.name = name;
        this.color = color;
        this.chatSymbol = chatSymbol;
    }

    // -- GETTERS -- //

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getChatSymbol() {
        return chatSymbol;
    }
}
