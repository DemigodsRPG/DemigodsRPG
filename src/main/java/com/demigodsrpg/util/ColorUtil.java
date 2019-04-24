package com.demigodsrpg.util;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;

import java.awt.*;
import java.util.Map;

public class ColorUtil {
    /**
     * Linking ChatColor objects to the corresponding Color.
     */
    public static final ImmutableBiMap<ChatColor, Color> CHAT_COLOR;

    static {
        Map<ChatColor, Color> chatColorColor = Maps.newHashMap();
        chatColorColor.put(ChatColor.BLACK, new Color(0, 0, 0));
        chatColorColor.put(ChatColor.DARK_BLUE, new Color(0, 0, 170));
        chatColorColor.put(ChatColor.DARK_GREEN, new Color(0, 170, 0));
        chatColorColor.put(ChatColor.DARK_AQUA, new Color(0, 170, 170));
        chatColorColor.put(ChatColor.DARK_RED, new Color(170, 0, 0));
        chatColorColor.put(ChatColor.DARK_PURPLE, new Color(170, 0, 170));
        chatColorColor.put(ChatColor.GOLD, new Color(255, 170, 0));
        chatColorColor.put(ChatColor.GRAY, new Color(170, 170, 170));
        chatColorColor.put(ChatColor.DARK_GRAY, new Color(85, 85, 85));
        chatColorColor.put(ChatColor.BLUE, new Color(85, 85, 255));
        chatColorColor.put(ChatColor.GREEN, new Color(85, 255, 85));
        chatColorColor.put(ChatColor.AQUA, new Color(85, 255, 255));
        chatColorColor.put(ChatColor.RED, new Color(255, 85, 85));
        chatColorColor.put(ChatColor.LIGHT_PURPLE, new Color(255, 85, 255));
        chatColorColor.put(ChatColor.YELLOW, new Color(255, 255, 85));
        chatColorColor.put(ChatColor.WHITE, new Color(255, 255, 255));
        CHAT_COLOR = ImmutableBiMap.copyOf(chatColorColor);
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
     * Get a ChatColor that is best related to a given Color.
     *
     * @param color Color to be matched.
     * @return Best ChatColor found.
     */
    public static ChatColor getChatColor(final Color color) {
        Color nearestColor = Color.WHITE;
        double bestDistance = Double.MAX_VALUE;

        for (Color chatColor : CHAT_COLOR.values()) {
            double distance = getColorDistance(chatColor, color);
            if (distance < bestDistance) {
                nearestColor = chatColor;
                bestDistance = distance;
            }
        }

        return CHAT_COLOR.inverse().get(nearestColor);
    }

    /**
     * Get a Color that is best related to a given ChatColor.
     *
     * @param color ChatColor to be matched.
     * @return Best Color found.
     */
    public static Color getColor(final ChatColor color) {
        return CHAT_COLOR.get(color);
    }

    /**
     * Returns a color (red, yellow, green) based on the <code>value</code> and <code>max</code> passed in.
     *
     * @param value the actual value.
     * @param max   the maximum value possible.
     */
    public static ChatColor getColor(double value, double max) {
        ChatColor color = ChatColor.RESET;
        if (value < Math.ceil(0.33 * max)) { color = ChatColor.RED; } else if (value < Math.ceil(0.66 * max) &&
                value > Math.ceil(0.33 * max)) { color = ChatColor.YELLOW; }
        if (value > Math.ceil(0.66 * max)) color = ChatColor.GREEN;
        return color;
    }
}
