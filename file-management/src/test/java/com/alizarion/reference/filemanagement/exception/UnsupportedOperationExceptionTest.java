package com.alizarion.reference.filemanagement.exception;

import com.alizarion.reference.exception.ApplicationException;
import com.alizarion.reference.filemanagement.entities.ManagedImageScaledCacheVisitor;
import com.alizarion.reference.filemanagement.entities.SimpleManagedFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author selim@openlinux.fr.
 */
public class UnsupportedOperationExceptionTest {

    @Rule
    public ExpectedException expectedException =  ExpectedException.none();

    @Test
    public void unsupportedOperationExceptionTest()
            throws ApplicationException {
        expectedException.expect(UnsupportedOperationException.class);
        SimpleManagedFile simpleManagedFile = new SimpleManagedFile();
        ManagedImageScaledCacheVisitor scaledCacheVisitor =
                new ManagedImageScaledCacheVisitor("toto",100,300);
        simpleManagedFile.accept(scaledCacheVisitor);
    }
}
