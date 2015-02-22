package com.demigodsrpg.game.deity;

import org.bukkit.ChatColor;

public class Faction {
    // FIXME Replace these with the appropriate stuff
    public static final Faction NEUTRAL = new Faction("Neutral", ChatColor.GRAY, "N");
    public static final Faction EXCOMMUNICATED = new Faction("Excommunicated", ChatColor.DARK_GRAY, "X");

    private String name;
    private ChatColor color;
    private String chatSymbol;

    public Faction(String name, ChatColor color, String chatSymbol) {
        this.name = name;
        this.color = color;
        this.chatSymbol = chatSymbol;
    }

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
