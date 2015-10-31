package com.alizarion.reference.filemanagement.test;

import com.alizarion.reference.filemanagement.entities.*;
import com.alizarion.reference.filemanagement.exception.ManagedImageFileDataVisitorException;
import com.alizarion.reference.filemanagement.tools.ImageFileHelper;
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
import java.io.IOException;
import java.net.MalformedURLException;
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
    public void setUp() throws MalformedURLException {
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

    //   @Test
    public void testFileURIGetter() throws Exception {

        ImageManagedFile imageManagedFile= new ImageManagedFile();

        EntityTransaction  trx =  em.getTransaction();
        trx.begin();

        // get file meta datas
        ManagedImageFileDataVisitor fileDataVisitor =
                new ManagedImageFileDataVisitor(this.fileToWrite.toURI().toURL());

        imageManagedFile.accept(fileDataVisitor);
        imageManagedFile = em.merge(imageManagedFile);
        //write the managed file
        ManagedFileWriterVisitor fileWriterVisitor =
                new ManagedFileWriterVisitor(
                        this.fileToWrite.toURI().toURL().openStream(),
                        this.rootFolder);
        imageManagedFile.accept(fileWriterVisitor);
        fileWriterVisitor.closeStream();

        //how to read saved file
        ManagedFileReaderVisitor managedFileReaderVisitor =
                new ManagedFileReaderVisitor(this.rootFolder);


        imageManagedFile.accept(managedFileReaderVisitor);
        assertFalse(!managedFileReaderVisitor.
                getManagedFileAsFile().exists());

        ManagedImageScaledCacheVisitor scaledCacheVisitor =
                new ManagedImageScaledCacheVisitor(
                        this.rootFolder,199,100);

        imageManagedFile.accept(scaledCacheVisitor);

        System.out.println(imageManagedFile);
        File cacheFile = scaledCacheVisitor.getCacheFile();
        ImageFileHelper.getAndManageImage(this.em,
                new URL("https://www.google.fr/images/srpr/logo11w.png"),
                this.rootFolder);

        trx.commit();

    }

    @Test
    public void faviconManageTest() throws IOException, ManagedImageFileDataVisitorException {


        ImageManagedFile  managedFile = ImageFileHelper.
                getAndManageImage(this.em,
                        new URL("https://s.ytimg.com/yts" +
                                "/img/favicon-vfldLzJxy.ico"),
                        "/tmp/");
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
