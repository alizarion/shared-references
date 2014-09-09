package com.alizarion.reference.filemanagement.entities;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author selim@openlinux.fr.
 */
public class ManagedImageFileDataVisitor implements ManagedFileVisitor{

    private File file;





    public ManagedImageFileDataVisitor(File file) {
        this.file = file;
    }

    @Override
    public void visit(ImageManagedFile imageManagedFile) {
        try {
            imageManagedFile.setFileName(this.file.getName());
            BufferedImage image = ImageIO.read(new FileInputStream(this.file));
            imageManagedFile.setWidth(image.getWidth());
            imageManagedFile.setHeight(image.getHeight());
            imageManagedFile.setWeight(this.file.length()/1024);
            imageManagedFile.setColorSpace(image.getColorModel().
                    getColorSpace().getType());
            ImageInputStream inputStream = ImageIO.
                    createImageInputStream(this.file);
            Iterator<ImageReader> readerIterator =  ImageIO.
                    getImageReaders(inputStream);
            if (!readerIterator.hasNext()){
                return;
            }
            //TODO gestion des erreur en cas d'echec lecture
            ImageReader imageReader =  readerIterator.next();
            imageManagedFile.setFormat(imageReader.getFormatName());


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void visit(SimpleManagedFile simpleManagedFile) {

    }


}
