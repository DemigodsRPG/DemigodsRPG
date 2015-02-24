package com.demigodsrpg.game.deity;

import com.demigodsrpg.game.model.AbstractPersistentModel;
import com.demigodsrpg.game.util.JsonSection;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class Faction extends AbstractPersistentModel<String> {

    // -- ALWAYS EXISTING FACTIONS -- //

    public static final Faction NEUTRAL = new Faction("Neutral", ChatColor.GRAY, "N");
    public static final Faction EXCOMMUNICATED = new Faction("Excommunicated", ChatColor.DARK_GRAY, "X");

    // -- FACTION META DATA -- //

    private String name;
    private ChatColor color;
    private String chatSymbol;

    // -- CONSTRUCTORS -- //

    public Faction(String name, ChatColor color, String chatSymbol) {
        this.name = name;
        this.color = color;
        this.chatSymbol = chatSymbol;
    }

    public Faction(String stringKey, JsonSection conf) {
        name = stringKey;
        color = ChatColor.valueOf(conf.getString("color"));
        chatSymbol = conf.getString("chat-symbol");
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

    @Override
    public String getPersistentId() {
        return name;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("color", color.name());
        map.put("chat-symbol", chatSymbol);
        return map;
    }
}
