package com.demigodsrpg.game.aspect.demon;

import com.demigodsrpg.game.aspect.Aspect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class DemonAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Demon Aspect";
    }

    @Override
    public TextColor getColor() {
        return TextColors.DARK_PURPLE;
    }

    @Override
    public SoundType getSound() {
        return SoundTypes.GHAST_DEATH;
    }

    @Override
    public ItemType getClaimMaterial() {
        return ItemTypes.BONE;
    }
}
