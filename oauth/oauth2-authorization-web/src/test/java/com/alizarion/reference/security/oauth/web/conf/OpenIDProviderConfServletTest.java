package com.alizarion.reference.security.oauth.web.conf;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

@RunWith( value = MockitoJUnitRunner.class)
public class OpenIDProviderConfServletTest {

    @Spy
    OpenIDProviderConfServlet servlet;

    @Test
    public void testOpenIDConfigRenderer() throws IOException {
        System.out.println(servlet.getConfiguration("toto.com"));
        Assert.assertNotNull(servlet.getConfiguration("toto.fr"));


    }

}