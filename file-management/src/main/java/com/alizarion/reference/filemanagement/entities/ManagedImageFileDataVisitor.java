package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.filemanagement.exception.ManagedImageFileDataVisitorException;
import net.sf.image4j.codec.ico.ICODecoder;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Class that contain method to get image url meta data.
 * @author selim@openlinux.fr.
 */
public class ManagedImageFileDataVisitor implements ManagedFileVisitor{

    private URL url ;

    public ManagedImageFileDataVisitor(final URL url) throws IOException {

        this.url = url;

    }



    @Override
    public Void visit(ImageManagedFile imageManagedFile)
            throws ManagedImageFileDataVisitorException {
        String fileName =
                new File(this.url.getFile()).getName();

        InputStream is = null;
        try {
            is = url.openStream();
            imageManagedFile.setFileName(fileName);
            BufferedImage image = null;
            if (FilenameUtils.getExtension(imageManagedFile.getFileName()).equals("ico")){

                List<BufferedImage> bufferedImages = ICODecoder.read(is);
                if (!bufferedImages.isEmpty()) {

                    image = bufferedImages.get(0);

                }else {

                    throw new ManagedImageFileDataVisitorException("cannot read data about "+ fileName);
                }
                imageManagedFile.getImageMetaData().setFormat("ico");
                imageManagedFile.setWeight((long) IOUtils.toByteArray(is).length / 1024);

            } else {
                ImageInputStream imageInputStream = ImageIO.
                        createImageInputStream(url.openStream());
                Iterator<ImageReader>  readerIterator =  ImageIO.
                        getImageReaders(imageInputStream);

                image = ImageIO.read(imageInputStream);
                if (!readerIterator.hasNext()){

                    throw new ManagedImageFileDataVisitorException(
                            "ManagedImageFileDataVisitorException" +
                                    " cannot read image meta data of :  " +
                                    imageManagedFile.toString());
                }
                ImageReader imageReader =  readerIterator.next();
                imageManagedFile.getImageMetaData().setFormat(imageReader.getFormatName());
                imageManagedFile.setWeight(imageInputStream.length() / 1024);


            }
            if(image== null){

                return null;
            }
            imageManagedFile.
                    getImageMetaData()
                    .setWidth(image.getWidth());
            imageManagedFile.
                    getImageMetaData()
                    .setHeight(image.getHeight());
            imageManagedFile.
                    getImageMetaData()
                    .setColorSpace(image.getColorModel().
                    getColorSpace()
                            .getType());

        } catch (IOException e) {
            if(is != null){
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
            throw new ManagedImageFileDataVisitorException(
                    "ManagedImageFileDataVisitorException" +
                            " cannot read image meta data of :  " +
                            imageManagedFile.toString() +"\n"+ e);

        }  finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    @Override
    public Void visit(SimpleManagedFile simpleManagedFile) {
        throw new UnsupportedOperationException("SimpleManagedFile cannot " +
                "support image meta data extraction.");
    }

}
