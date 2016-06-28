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

package com.demigodsrpg.util.misc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageUtil {
    /**
     * Retrieve the image of a player's head.
     *
     * @param playerName The player who owns the head.
     * @return The mentioned image.
     * @throws NullPointerException
     */
    public static BufferedImage getPlayerHead(String playerName) throws NullPointerException {
        try {
            // Get the image from Mojang.
            URL url = new URL("http://s3.amazonaws.com/MinecraftSkins/" + playerName + ".png");
            BufferedImage image = ImageIO.read(url);

            // Get data from the skin.
            Image head = image.getSubimage(8, 8, 8, 8);
            Image overlay = image.getSubimage(40, 8, 8, 8);

            // Render just the (front of the) head of the skin.
            BufferedImage finalImage = new BufferedImage(64, 64, Image.SCALE_SMOOTH);
            finalImage.getGraphics().drawImage(head, 0, 0, 64, 64, null);
            finalImage.getGraphics().drawImage(overlay, 0, 0, 64, 64, null);

            return finalImage;
        } catch (Exception errored) {
            errored.printStackTrace();
        }

        return null;
    }

    /**
     * Scale an image.
     *
     * @param image  The image to manipulate.
     * @param width  The desired width.
     * @param height The desired height.
     * @return The new image.
     * @throws IOException
     */
    public static BufferedImage scaleImage(BufferedImage image, int width, int height) throws IOException {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double) width / imageWidth;
        double scaleY = (double) height / imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
    }
}
