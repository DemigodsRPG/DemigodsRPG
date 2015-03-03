package com.demigodsrpg.game.aspect.bloodlust;

import com.demigodsrpg.game.aspect.Aspect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class BloodlustAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Bloodlust Aspect";
    }

    @Override
    public TextColor getColor() {
        return TextColors.RED;
    }

    @Override
    public SoundType getSound() {
        return SoundTypes.VILLAGER_HIT;
    }

    @Override
    public ItemType getClaimMaterial() {
        return ItemTypes.GOLDEN_SWORD;
    }
}
