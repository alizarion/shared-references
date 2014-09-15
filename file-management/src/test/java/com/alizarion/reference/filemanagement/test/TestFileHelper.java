package com.alizarion.reference.filemanagement.test;

import com.alizarion.reference.filemanagement.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertFalse;

/**
 *
 * @author selim@openlinux.fr.
 */
public class TestFileHelper {

    EntityManagerFactory emf ;
    EntityManager em;

    private String rootFolder;

    private File fileToWrite;

    @Before
    public void setUp(){
        URL url = this.getClass().getResource("/sample.txt");
        this.fileToWrite = new File(url.getFile());
        this.rootFolder = this.fileToWrite.getParentFile().getAbsolutePath();
        this.emf = Persistence.createEntityManagerFactory("ManagedFilePU");
        this.em =  emf.createEntityManager();
        this.fileToWrite = drawImage();
    }

    @After
    public void tearDown(){
        this.em.close();
        this.emf.close();
    }

    @Test
    public void testFileURIGetter() throws Exception {

        ImageManagedFile imageManagedFile= new ImageManagedFile();

        EntityTransaction  trx =  em.getTransaction();
        trx.begin();
        ManagedImageFileDataVisitor fileDataVisitor =
                new ManagedImageFileDataVisitor(this.fileToWrite);

            imageManagedFile.accept(fileDataVisitor);


        imageManagedFile = em.merge(imageManagedFile);
        ManagedFileWriterVisitor fileWriterVisitor =
                new ManagedFileWriterVisitor(
                        new FileInputStream(this.fileToWrite),
                        this.rootFolder);
        imageManagedFile.accept(fileWriterVisitor);
        ManagedFileReaderVisitor managedFileReaderVisitor =
                new ManagedFileReaderVisitor(this.rootFolder);
        imageManagedFile.accept(managedFileReaderVisitor);
        assertFalse(!managedFileReaderVisitor.getManagedFileAsFile().exists());
        ManagedImageScaledCacheVisitor scaledCacheVisitor =
                new ManagedImageScaledCacheVisitor(this.rootFolder,199,100);
        imageManagedFile.accept(scaledCacheVisitor);
        System.out.println(imageManagedFile);
        File cacheFile = scaledCacheVisitor.getCacheFile();
        trx.commit();

    }

    public File drawImage(){
        BufferedImage bufferedImage = new BufferedImage(200,200 ,BufferedImage.TYPE_INT_RGB);
        Graphics2D  graphics2D =  bufferedImage.createGraphics();
        for(int i = 0 ; i < 200 ; i += 20){
            graphics2D.drawString("Test file management",20,i);

        }
        File file =  new File(this.rootFolder,"mon_image.jpg");
        try {
            ImageIO.write(bufferedImage,"jpg",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
