package com.alizarion.reference.exception;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ApplicationErrorTest {

    @Rule
    public ExpectedException exception =  ExpectedException.none();

    @Test
    public void testPropagation(){
        System.out.println("before exception in testPropagation");
        throwUnCheckedException();
        System.out.println("after exception in testPropagation");

    }

    public void throwUnCheckedException(){
        exception.expect(ApplicationError.class);
        throw new ApplicationError("test of unchecked exception propagation");
    }

}