package com.alizarion.reference.filemanagement.tools;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author selim@openlinux.fr.
 */
public class ImageFileHelper {


    public static BufferedImage scaleImage(BufferedImage image,
                                           Integer newWidth,
                                           Integer newHeight) {
        double thumbRatio = (double) newWidth / (double) newHeight;
        Integer imageWidth = image.getWidth(null);
        Integer imageHeight = image.getHeight(null);

        double aspectRatio = (double) imageWidth / (double) imageHeight;
        if (thumbRatio < aspectRatio) {
            newHeight = (int) (newWidth / aspectRatio);
        } else {
            newWidth = (int) (newHeight * aspectRatio);
        }
        // Draw the scaled image
        BufferedImage newImage = new BufferedImage(newWidth, newHeight,
                image.getType());
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);

        return newImage;
    }


}
