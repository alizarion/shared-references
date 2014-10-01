package com.alizarion.reference.entities;

import com.alizarion.reference.security.entities.oauth.server.OAuthApplication;

import javax.persistence.Entity;
import java.net.URL;

/**
 * @author selim@openlinux.fr.
 */

@Entity
public class TestApplication extends OAuthApplication {

    private static final long serialVersionUID = -6026512936181374010L;


    public TestApplication() {
        super();
    }

    public TestApplication(final String name,
                           final URL homePage,
                           final URL callback) {
        super(name, homePage, callback);
    }
}
