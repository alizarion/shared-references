package com.alizarion.reference.filemanagement.tools;

import com.alizarion.reference.filemanagement.entities.ImageManagedFile;
import com.alizarion.reference.filemanagement.entities.ManagedFileWriterVisitor;
import com.alizarion.reference.filemanagement.entities.ManagedImageFileDataVisitor;
import com.alizarion.reference.filemanagement.exception.ManagedImageFileDataVisitorException;

import javax.persistence.EntityManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

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

    public static ImageManagedFile getAndManageImage(EntityManager em,
                                                     URL url,
                                                     final String configRootFolder) throws ManagedImageFileDataVisitorException {
        ManagedImageFileDataVisitor fileDataVisitor =null;
        ManagedFileWriterVisitor fileWriterVisitor = null;
        try {
            ImageManagedFile imageManagedFile = new ImageManagedFile();

            InputStream urlStream = url.openStream();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlStream));
            fileDataVisitor =
                    new ManagedImageFileDataVisitor(url);
            imageManagedFile.accept(fileDataVisitor);

            imageManagedFile = em.merge(imageManagedFile);

            fileWriterVisitor =
                    new ManagedFileWriterVisitor(
                            url.openStream(),
                            configRootFolder);

            imageManagedFile.accept(fileWriterVisitor);
            fileWriterVisitor.closeStream();
            imageManagedFile = em.merge(imageManagedFile);
            em.flush();
            return imageManagedFile;
        } catch (Exception e){

            if (fileWriterVisitor != null){
                fileWriterVisitor.closeStream();
            }
            throw  new ManagedImageFileDataVisitorException(e);
        }
    }


}
