package com.demigodsrpg.game.aspect.water;

import com.demigodsrpg.game.aspect.Aspect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class WaterAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Water Aspect";
    }

    @Override
    public TextColor getColor() {
        return TextColors.AQUA;
    }

    @Override
    public SoundType getSound() {
        return SoundTypes.WATER;
    }

    @Override
    public ItemType getClaimMaterial() {
        return ItemTypes.FISH;
    }
}
