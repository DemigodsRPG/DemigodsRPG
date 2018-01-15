package com.demigodsrpg.util;

import com.demigodsrpg.util.misc.ImageUtil;
import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class BukkitImageUtil {
    /**
     * Convert an image a list of String objects.
     *
     * @param image  The image to be converted.
     * @param symbol The symbol being used by the string.
     * @return The converted list.
     */
    public static java.util.List<String> convertImage(BufferedImage image, String symbol) {
        // Working list.
        java.util.List<String> convertedImage = Lists.newArrayList();

        // Get the image's height and width.
        int width = image.getWidth();
        int height = image.getHeight();

        // Iterate through the image, pixel by pixel.
        for (int i = 0; i < height; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < width; j++) {
                // Get the color for each pixel.
                ChatColor color = ColorUtil.getChatColor(new Color(image.getRGB(j, i)));

                // Append to the line.
                line.append(color.toString()).append(symbol);
            }

            // Add to working list.
            convertedImage.add(line.toString());
        }

        // Return finalized list.
        return convertedImage;
    }

    /**
     * Send an image to a player, in the form of an in-game Map.
     *
     * @param player The player to receive the map.
     * @param image  The image to be converted.
     * @return The MapView the player receives.
     */
    public static MapView sendMapImage(Player player, BufferedImage image) {
        MapView map = Bukkit.createMap(player.getWorld());
        map = ImageRenderer.applyToMap(map, image);
        player.sendMap(map);
        return map;
    }

    /**
     * Retrieve the image of a player's head and make it suitable for chat.
     *
     * @param player The player who owns the head.
     * @return A list of strings to be sent in order to a player.
     * @throws NullPointerException
     */
    public static java.util.List<String> getPlayerHead(OfflinePlayer player) throws NullPointerException {
        // Get the player's name.
        String playerName = player.getName();

        try {
            // Find.
            BufferedImage image = ImageUtil.getPlayerHead(playerName);

            // Resize.
            image = ImageUtil.scaleImage(image, 16, 16);

            // Convert.

            // Return the converted head.
            return convertImage(image, "█");
        } catch (Exception errored) {
            errored.printStackTrace();
            Bukkit.getServer().getLogger()
                    .warning("[CensoredLib] " + "Something went wrong during an image conversion process.");
        }

        // Something went wrong.
        return null;
    }

    static class ImageRenderer extends MapRenderer {
        private BufferedImage image;

        public ImageRenderer(BufferedImage image) {
            this.image = MapPalette.resizeImage(image);
        }

        static MapView applyToMap(MapView map, BufferedImage image) {
            for (MapRenderer renderer : map.getRenderers()) { map.removeRenderer(renderer); }

            map.addRenderer(new ImageRenderer(image));

            return map;
        }

        @Override
        public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
            mapCanvas.drawImage(0, 0, image);
        }
    }
}