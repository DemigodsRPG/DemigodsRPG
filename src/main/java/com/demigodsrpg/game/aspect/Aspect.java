package com.demigodsrpg.game.aspect;

import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.format.TextColor;

public interface Aspect {
    Group getGroup();

    int getId();

    String getInfo();

    Tier getTier();

    public enum Tier {
        I, II, III, HERO
    }

    public interface Group {
        public String getName();

        public TextColor getColor();

        public SoundType getSound();

        public ItemType getClaimMaterial();
    }
}
