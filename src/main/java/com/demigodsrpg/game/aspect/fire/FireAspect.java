package com.demigodsrpg.game.aspect.fire;

import com.demigodsrpg.game.aspect.Aspect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class FireAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Fire Aspect";
    }

    @Override
    public TextColor getColor() {
        return TextColors.GOLD;
    }

    @Override
    public SoundType getSound() {
        return SoundTypes.FIRE;
    }

    @Override
    public ItemType getClaimMaterial() {
        return ItemTypes.FIRE_CHARGE;
    }
}
