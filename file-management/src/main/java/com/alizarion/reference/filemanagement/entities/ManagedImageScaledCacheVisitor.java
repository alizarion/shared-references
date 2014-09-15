package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.filemanagement.exception.GeneratingCacheFileException;
import com.alizarion.reference.filemanagement.tools.FileHelper;
import com.alizarion.reference.filemanagement.tools.ImageFileHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Visitor that contain methods to generate image scaled cache.
 * @author selim@openlinux.fr.
 */
public class ManagedImageScaledCacheVisitor implements ManagedFileVisitor{

    /**
     * root folder to discriminate cache files.
     */
    public final static String CACHE_FOLDER_DISCRIMINATOR= "CACHE";

    /**
     * root folder of all managed files.
     */
    private  String rootFolder;

    /**
     * new image width.
     */
    private Integer newWidth;

    /**
     * new image height.
     */
    private Integer newHeight;

    /**
     * scaled cache file.
     */
    private File cacheFile;

    public ManagedImageScaledCacheVisitor(String rootFolder,
                                          Integer newWidth,
                                          Integer newHeight) {
        this.rootFolder = rootFolder;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    @Override
    public File visit(ImageManagedFile imageManagedFile)
            throws GeneratingCacheFileException {
        if(imageManagedFile.getWidth() <= this.newWidth ||
                imageManagedFile.getWidth() <= this.newWidth){
            ManagedFileReaderVisitor readerVisitor =
                    new ManagedFileReaderVisitor(this.rootFolder);
            try {
                imageManagedFile.accept(readerVisitor);
            } catch (Exception e) {
                throw new GeneratingCacheFileException(
                        "error generatig cache file ",e);
            }
            this.cacheFile = readerVisitor.getManagedFileAsFile();
        }  else {

            File expectedFile = new File(FileHelper.
                    getFileFullPath(imageManagedFile,
                            this.rootFolder,
                            CACHE_FOLDER_DISCRIMINATOR,
                            "_".concat(this.newWidth.toString()).
                                    concat("x")));

            if (expectedFile.exists()) {
                this.cacheFile = expectedFile;

            } else {

                ManagedFileReaderVisitor fileReaderVisitor =
                        new ManagedFileReaderVisitor(this.rootFolder);
                try {
                    imageManagedFile.accept(fileReaderVisitor);
                } catch (Exception e) {
                    throw  new GeneratingCacheFileException(
                            "error generatig cache file ",e);
                }

                try {
                    BufferedImage imageSource =
                            ImageIO.read(fileReaderVisitor.
                                    getManagedFileAsFile());
                    BufferedImage scaledImage = ImageFileHelper.
                            scaleImage(imageSource,
                                    this.newWidth, this.newHeight);
                    if (!expectedFile.getParentFile().exists()) {
                        if (!expectedFile.getParentFile().mkdirs()) {
                            throw new GeneratingCacheFileException(
                                    "Cannot create directories" +
                                            " for generating cache" +
                                            expectedFile.getParentFile());
                        }
                    }
                    ImageIO.write(scaledImage,
                            imageManagedFile.getFormat(),
                            expectedFile);
                    this.cacheFile = expectedFile;

                } catch (Exception e) {
                    throw  new GeneratingCacheFileException(
                            "Cache generation failed for : " + imageManagedFile );
                }
            }
        }
        return this.cacheFile ;
    }

    @Override
    public Void visit(SimpleManagedFile simpleManagedFile) {
        throw new UnsupportedOperationException("SimpleManagedFile cannot support cache");
    }

    public File getCacheFile() {
        return cacheFile;
    }


}
