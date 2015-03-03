package com.demigodsrpg.game.aspect.crafting;

import com.demigodsrpg.game.aspect.Aspect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class CraftingAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Crafting Aspect";
    }

    @Override
    public TextColor getColor() {
        return TextColors.DARK_GRAY;
    }

    @Override
    public SoundType getSound() {
        return SoundTypes.ANVIL_USE;
    }

    @Override
    public ItemType getClaimMaterial() {
        return ItemTypes.ANVIL;
    }
}
