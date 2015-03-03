package com.demigodsrpg.game.aspect.lightning;

import com.demigodsrpg.game.aspect.Aspect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class LightingAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Lightning Aspect";
    }

    @Override
    public TextColor getColor() {
        return TextColors.YELLOW;
    }

    @Override
    public SoundType getSound() {
        return SoundTypes.AMBIENCE_THUNDER;
    }

    @Override
    public ItemType getClaimMaterial() {
        return ItemTypes.FEATHER;
    }
}
