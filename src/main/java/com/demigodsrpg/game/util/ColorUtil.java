/*
 * Copyright 2014 Alex Bennett & Alexander Chauncey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.game.util;

import com.google.common.collect.ImmutableBiMap;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorUtil {
    /**
     * Linking block materials to their corresponding Color.
     */
    public static ImmutableBiMap<BlockType, Color> BLOCK_COLOR;

    static {
        Map<BlockType, Color> blockColor = new HashMap<>();

        /* FIXME Not all block types are in Sponge yet

        // WOOL
        blockColor.put(new MaterialData(Material.WOOL, (byte) 0), new Color(238, 238, 238));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 1), new Color(213, 116, 50));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 2), new Color(168, 65, 177));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 3), new Color(113, 141, 201));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 4), new Color(193, 181, 45));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 5), new Color(66, 171, 55));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 6), new Color(203, 125, 146));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 7), new Color(72, 72, 72));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 8), new Color(127, 135, 135));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 9), new Color(39, 94, 117));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 10), new Color(120, 55, 173));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 11), new Color(39, 45, 116));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 12), new Color(74, 46, 29));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 13), new Color(36, 48, 19));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 14), new Color(153, 35, 38));
        blockColor.put(new MaterialData(Material.WOOL, (byte) 15), new Color(25, 25, 25));

        // HARD CLAY
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 0), new Color(221, 176, 152));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 1), new Color(185, 82, 36));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 2), new Color(163, 83, 98));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 3), new Color(121, 106, 129));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 4), new Color(206, 132, 35));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 5), new Color(102, 115, 58));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 6), new Color(179, 75, 73));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 7), new Color(64, 44, 39));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 8), new Color(141, 102, 88));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 9), new Color(80, 86, 82));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 10), new Color(124, 67, 79));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 11), new Color(77, 63, 86));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 12), new Color(83, 55, 39));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 13), new Color(73, 82, 46));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 14), new Color(166, 61, 46));
        blockColor.put(new MaterialData(Material.HARD_CLAY, (byte) 15), new Color(44, 32, 19));

        // SAND
        blockColor.put(new MaterialData(Material.SAND, (byte) 0), new Color(247, 233, 163));
        blockColor.put(new MaterialData(Material.SANDSTONE, (byte) 0), new Color(213, 201, 140));

        // STONE
        blockColor.put(new MaterialData(Material.STONE, (byte) 0), new Color(144, 144, 144));
        blockColor.put(new MaterialData(Material.SMOOTH_BRICK, (byte) 0), new Color(117, 117, 117));

        // REDSTONE
        blockColor.put(new MaterialData(Material.REDSTONE_BLOCK, (byte) 0), new Color(255, 0, 0));

        // ICE
        blockColor.put(new MaterialData(Material.PACKED_ICE, (byte) 0), new Color(160, 160, 255));
        blockColor.put(new MaterialData(Material.ICE, (byte) 0), new Color(138, 138, 220));

        // DIRT
        blockColor.put(new MaterialData(Material.DIRT, (byte) 0), new Color(73, 58, 35));

        // NETHER
        blockColor.put(new MaterialData(Material.NETHER_BRICK, (byte) 0), new Color(112, 2, 0));

        // OBSIDIAN
        blockColor.put(new MaterialData(Material.OBSIDIAN, (byte) 0), new Color(21, 20, 31));

        // COAL
        blockColor.put(new MaterialData(Material.COAL_BLOCK, (byte) 0), new Color(12, 12, 12));

        // EMERALD
        blockColor.put(new MaterialData(Material.EMERALD_BLOCK, (byte) 0), new Color(0, 217, 58));

        */

        BLOCK_COLOR = ImmutableBiMap.copyOf(blockColor);
    }

    /**
     * Color distance formula.
     *
     * @param c1 Color one.
     * @param c2 Color two.
     * @return The 'distance' between the two colors.
     */
    public static double getColorDistance(Color c1, Color c2) {
        double rmean = (c1.getRed() + c2.getRed()) / 2;
        int r = c1.getRed() - c2.getRed();
        int g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2 + rmean / 256;
        double weightG = 4.0;
        double weightB = 2 + (255 - rmean) / 256;
        return Math.sqrt(weightR * r * r + weightG * g * g + weightB * b * b);
    }

    /**
     * Get a TextColor that is best related to a given Color.
     *
     * @param color Color to be matched.
     * @return Best ChatColor found.
     */
    public static TextColor getChatColor(final Color color) {
        TextColor nearestColor = TextColors.WHITE;
        double bestDistance = Double.MAX_VALUE;

        for (TextColor chatColor : TextColors.getValues()) {
            double distance = getColorDistance(chatColor.getColor(), color);
            if (distance < bestDistance) {
                nearestColor = chatColor;
                bestDistance = distance;
            }
        }

        return nearestColor;
    }

    /**
     * Get a Color that is best related to a given ChatColor.
     *
     * @param color ChatColor to be matched.
     * @return Best Color found.
     */
    public static Color getColor(final TextColor color) {
        return color.getColor();
    }

    /**
     * Get MaterialData that is best related to a given ChatColor.
     *
     * @param color ChatColor to be matched.
     * @return Best MaterialData found.
     */
    public static BlockType getMaterial(final TextColor color) {
        return getMaterial(Color.WHITE, getColor(color));
    }

    /**
     * Get MaterialData that is best related to a given Color.
     *
     * @param color Color to be matched.
     * @return Best MaterialData found.
     */
    public static BlockType getMaterial(Color average, final Color color) {
        Color nearestColor = average;
        double bestDistance = Double.MAX_VALUE;

        for (Color theColor : BLOCK_COLOR.values()) {
            double distance = getColorDistance(theColor, color);
            if (distance < bestDistance) {
                nearestColor = theColor;
                bestDistance = distance;
            }
        }

        return BLOCK_COLOR.inverse().get(nearestColor);
    }

    /**
     * Returns a color (red, yellow, green) based on the <code>value</code> and <code>max</code> passed in.
     *
     * @param value the actual value.
     * @param max   the maximum value possible.
     */
    public static TextColor getColor(double value, double max) {
        TextColor color = TextColors.RESET;
        if (value < Math.ceil(0.33 * max)) color = TextColors.RED;
        else if (value < Math.ceil(0.66 * max) && value > Math.ceil(0.33 * max)) color = TextColors.YELLOW;
        if (value > Math.ceil(0.66 * max)) color = TextColors.GREEN;
        return color;
    }
}
