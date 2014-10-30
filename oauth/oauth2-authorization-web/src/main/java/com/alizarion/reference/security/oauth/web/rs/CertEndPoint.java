package com.alizarion.reference.security.oauth.web.rs;

import com.alizarion.reference.security.oauth.oauth2.exception.OAuthOpenIDSignatureException;
import com.alizarion.reference.security.oauth.oauth2.toolkit.OpenIdJWTNimbusFactory;
import com.alizarion.reference.security.oauth.services.oauth.OAuthServerService;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Path("/certs")
public class CertEndPoint implements Serializable {

    private static final long serialVersionUID = -8287516605924816670L;

    @EJB
    private OAuthServerService oauthService;


    @GET
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response certs(@Context HttpServletRequest request) {
        try {
            return Response.status(Response.Status.OK)
                    .entity(OpenIdJWTNimbusFactory.Server.getJSONWebKeyCert(
                            oauthService.getOrderedAliveCerts()))
                    .build();
        } catch (OAuthOpenIDSignatureException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Oups do not tell anyone !").build();
        }


    }
}
