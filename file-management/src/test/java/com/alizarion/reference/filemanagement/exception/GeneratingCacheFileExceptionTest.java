package com.alizarion.reference.filemanagement.exception;

import com.alizarion.reference.filemanagement.entities.ImageManagedFile;
import com.alizarion.reference.filemanagement.entities.ManagedImageScaledCacheVisitor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GeneratingCacheFileExceptionTest {

    @Rule
    public ExpectedException expectedException =  ExpectedException.none();


    @Test
    public void generationCacheExceptionTest() throws Exception {
        expectedException.expect(GeneratingCacheFileException.class);
        ImageManagedFile managedFile =  new ImageManagedFile();
        managedFile.setHeight(700);
        managedFile.setWidth(600);
        managedFile.setId(67L);
        managedFile.setFileName("toto");
        ManagedImageScaledCacheVisitor scaledCacheVisitor =
                new ManagedImageScaledCacheVisitor("titi",300,300);
        managedFile.accept(scaledCacheVisitor);
    }

}