package com.demigodsrpg.family;

import com.demigodsrpg.util.datasection.AbstractPersistentModel;
import com.demigodsrpg.util.datasection.DataSection;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class Family extends AbstractPersistentModel<String> {

    // -- ALWAYS EXISTING FACTIONS -- //

    public static final Family NEUTRAL = new Family("Neutral", ChatColor.GRAY, "N", "Welcome to neutral ground.");
    public static final Family EXCOMMUNICATED = new Family("Excommunicated", ChatColor.DARK_GRAY, "X",
            "Something has gone horribly wrong, alert an admin.");

    // -- FACTION META DATA -- //

    private String name;
    private String color;
    private String chatSymbol;
    private String welcomeMessage;

    // -- CONSTRUCTORS -- //

    public Family(String name, ChatColor color, String chatSymbol, String welcomeMessage) {
        this(name, color.toString(), chatSymbol, welcomeMessage);
    }

    public Family(String name, String color, String chatSymbol, String welcomeMessage) {
        this.name = name;
        this.color = color;
        this.chatSymbol = chatSymbol;
        this.welcomeMessage = welcomeMessage;
    }

    public Family(String stringKey, DataSection conf) {
        name = stringKey;
        color = conf.getString("color");
        chatSymbol = conf.getString("chat-symbol");
        welcomeMessage = conf.getString("welcome-message");
    }

    // -- GETTERS -- //

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getChatSymbol() {
        return chatSymbol;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    @Override
    public String getPersistentId() {
        return name;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("color", color);
        map.put("chat-symbol", chatSymbol);
        map.put("welcome-message", welcomeMessage);
        return map;
    }
}
