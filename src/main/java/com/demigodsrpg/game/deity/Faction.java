package com.demigodsrpg.game.deity;

import com.demigodsrpg.game.model.AbstractPersistentModel;
import com.demigodsrpg.game.util.JsonSection;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Faction extends AbstractPersistentModel<String> {

    // -- ALWAYS EXISTING FACTIONS -- //

    public static final Faction NEUTRAL = new Faction("Neutral", /*TextColors.GRAY,*/ "N", "Welcome to neutral ground.");
    public static final Faction EXCOMMUNICATED = new Faction("Excommunicated", /*TextColors.DARK_GRAY,*/ "X", "Something has gone horribly wrong, alert an admin.");

    // -- FACTION META DATA -- //

    private String name;
    //private TextColor color;
    private String chatSymbol;
    private String welcomeMessage;

    // -- CONSTRUCTORS -- //

    public Faction(String name, /*TextColor color,*/ String chatSymbol, String welcomeMessage) {
        this.name = name;
        //this.color = color;
        this.chatSymbol = chatSymbol;
        this.welcomeMessage = welcomeMessage;
    }

    public Faction(String stringKey, JsonSection conf) {
        name = stringKey;

        // Color TODO Sponge makes this hard to do...
        Color exactColor = new Color(conf.getInt("color-rgb"));
        Optional<TextColor> foundColor = TextColors.getValues().stream().filter(textColor -> textColor.getColor().equals(exactColor)).findAny();
        //if (foundColor.isPresent()) {
        //    color = foundColor.get();
        //} else {
        //   color = TextColors.GRAY;
        //}

        chatSymbol = conf.getString("chat-symbol");
        welcomeMessage = conf.getString("welcome-message");
    }

    // -- GETTERS -- //

    public String getName() {
        return name;
    }

    //public TextColor getColor() {
    //    return color;
    //}

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
        //map.put("color-rgb", color.getColor().getRGB());
        map.put("chat-symbol", chatSymbol);
        map.put("welcome-message", welcomeMessage);
        return map;
    }
}
