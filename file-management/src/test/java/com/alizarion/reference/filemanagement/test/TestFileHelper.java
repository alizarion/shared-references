package com.alizarion.reference.filemanagement.test;

import com.alizarion.reference.filemanagement.entities.ImageManagedFile;
import com.alizarion.reference.filemanagement.tools.ManagedFileReaderVisitor;
import com.alizarion.reference.filemanagement.tools.ManagedFileWriterVisitor;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
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
        URL url = this.getClass().getResource("/JPG_RGB_IMAGE.jpg");
        this.fileToWrite = new File(url.getFile());
        this.rootFolder = this.fileToWrite.getParentFile().getAbsolutePath();
        this.emf = Persistence.createEntityManagerFactory("ManagedFilePU");
        this.em =  emf.createEntityManager();
    }

    @After
    public void tearDown(){
        this.em.close();
        this.emf.close();
    }

    @Test
    public void testFileURIGetter() throws URISyntaxException, IOException {
        ImageManagedFile imageManagedFile= new ImageManagedFile();
        imageManagedFile.setExtension(FilenameUtils.
                getExtension(this.fileToWrite.getAbsolutePath()));
        imageManagedFile.setFileName(this.fileToWrite.getName());
        imageManagedFile.setColorSpace(1);
        imageManagedFile.setHeight(900);
        imageManagedFile.setWidth(500);
        EntityTransaction  trx =  em.getTransaction();
        trx.begin();
        imageManagedFile = em.merge(imageManagedFile);
        ManagedFileWriterVisitor fileWriterVisitor =
                new ManagedFileWriterVisitor(new FileInputStream(this.fileToWrite),
                        this.rootFolder);
        imageManagedFile.accept(fileWriterVisitor);
        ManagedFileReaderVisitor managedFileReaderVisitor =
                new ManagedFileReaderVisitor(this.rootFolder);
        managedFileReaderVisitor.visit(imageManagedFile);
        assertFalse(!managedFileReaderVisitor.getManagedFileAsFile().exists());
        trx.commit();


    }
}
