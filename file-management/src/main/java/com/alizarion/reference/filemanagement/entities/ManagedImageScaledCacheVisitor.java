package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.filemanagement.tools.FileHelper;
import com.alizarion.reference.filemanagement.tools.ImageFileHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
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
    public void visit(ImageManagedFile imageManagedFile) {
        if(imageManagedFile.getWidth() <= this.newWidth ||
                imageManagedFile.getWidth() <= this.newWidth){
            ManagedFileReaderVisitor readerVisitor =
                    new ManagedFileReaderVisitor(this.rootFolder);
            imageManagedFile.accept(readerVisitor);
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
                imageManagedFile.accept(fileReaderVisitor);

                try {
                    BufferedImage imageSource =
                            ImageIO.read(fileReaderVisitor.
                                    getManagedFileAsFile());
                    BufferedImage scaledImage = ImageFileHelper.
                            scaleImage(imageSource,
                                    this.newWidth, this.newHeight);
                    if (!expectedFile.getParentFile().exists()) {
                        if (!expectedFile.getParentFile().mkdirs()) {
                            throw new Exception("marche pas....");
                            //TODO catcher les excpetions correctement...
                        }
                    }
                    ImageIO.write(scaledImage,
                            imageManagedFile.getFormat(),
                            expectedFile);
                    this.cacheFile = expectedFile;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void visit(SimpleManagedFile simpleManagedFile) {

    }

    public File getCacheFile() {
        return cacheFile;
    }


}
