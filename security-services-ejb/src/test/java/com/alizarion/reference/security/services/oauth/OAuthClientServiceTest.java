package com.alizarion.reference.security.services.oauth;

import com.alizarion.reference.security.doa.OAuthJpaDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

@RunWith(MockitoJUnitRunner.class)
public class OAuthClientServiceTest {
    @Mock(name = "authDao")
    OAuthJpaDao authDao;

    @Mock
    EntityManager entityManager;

    @InjectMocks
    OAuthClientService clientService ;

    @Test
    public void testSetUp1() throws Exception {

     //TODO write test !!!
     /*   clientService.setUp();
        Mockito.when(entityManager.find(Mockito.any(Class.class),Mockito.anyObject())).
                thenReturn(SecurityTestFactory.getOAuthServerApplication());
        OAuthClientAuthorization authorization =
                clientService.getOAuthCodeRequest(
                        SecurityTestFactory.getOAuthServerApplication());
        Mockito.verify(entityManager,Mockito.atLeast(1)).
                find(Mockito.any(Class.class), Mockito.anyObject());
        System.out.println(authorization);
        Assert.assertNotNull(authorization);   */

    }
}