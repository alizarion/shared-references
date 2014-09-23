package com.alizarion.reference.exception;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

public class ApplicationExceptionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testPropagation () throws IOException, ApplicationException {

        System.out.println("before exception in testPropagation");
        throwCheckedException();
    }

    public void throwCheckedException() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        throw new ApplicationException("test of checked exception propagation");

    }

}