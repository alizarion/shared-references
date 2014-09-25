package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.filemanagement.exception.ManagedImageFileDataVisitorException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Class that contain method to get image file meta data.
 * @author selim@openlinux.fr.
 */
public class ManagedImageFileDataVisitor implements ManagedFileVisitor{

    private File file;





    public ManagedImageFileDataVisitor(File file) {
        this.file = file;
    }

    @Override
    public Void visit(ImageManagedFile imageManagedFile) throws ManagedImageFileDataVisitorException {
        try {
            imageManagedFile.setFileName(this.file.getName());
            BufferedImage image = ImageIO.read(new FileInputStream(this.file));
            imageManagedFile.getImageMetaData().setWidth(image.getWidth());
            imageManagedFile.getImageMetaData().setHeight(image.getHeight());
            imageManagedFile.setWeight(this.file.length()/1024);
            imageManagedFile.getImageMetaData().setColorSpace(image.getColorModel().
                    getColorSpace().getType());
            ImageInputStream inputStream = ImageIO.
                    createImageInputStream(this.file);
            Iterator<ImageReader> readerIterator =  ImageIO.
                    getImageReaders(inputStream);
            if (!readerIterator.hasNext()){
                throw new ManagedImageFileDataVisitorException(
                        "ManagedImageFileDataVisitorException" +
                                " cannot read image meta data of :  " +
                                imageManagedFile.toString());
            }
            ImageReader imageReader =  readerIterator.next();
            imageManagedFile.getImageMetaData().setFormat(imageReader.getFormatName());


        } catch (IOException e) {
            throw new ManagedImageFileDataVisitorException(
                    "ManagedImageFileDataVisitorException" +
                            " cannot read image meta data of :  " +
                            imageManagedFile.toString());

        }
        return null;
    }

    @Override
    public Void visit(SimpleManagedFile simpleManagedFile) {
        throw new UnsupportedOperationException("SimpleManagedFile cannot " +
                "support image meta data extraction.");
    }


}
