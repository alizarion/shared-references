package com.alizarion.reference.security.oauth.web.rs;

import com.alizarion.reference.security.oauth.oauth2.exception.InvalidAccessTokenException;
import com.alizarion.reference.security.oauth.oauth2.toolkit.OpenIdJWTNimbusFactory;
import com.alizarion.reference.security.oauth.services.oauth.OAuthServerService;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.net.URISyntaxException;

/**
 * @author selim@openlinux.fr.
 */
@Path("/openIdConnect")
public class UserInfoEndPoint implements Serializable {

    private static final long serialVersionUID = 3456515085190575037L;

    /**
     * OAuth2 services.
     * @see com.alizarion.reference.security.oauth.services.oauth.OAuthServerService
     */
    @EJB
    private OAuthServerService oauthService;

    @GET
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response getUserInfo(@Context HttpServletRequest request)
            throws OAuthSystemException {
        try {
            String accessToken =  request.getHeader(HttpHeaders.AUTHORIZATION);
            accessToken = accessToken.replace("Bearer ","");

            return Response.status(HttpServletResponse.SC_OK).entity(
                    OpenIdJWTNimbusFactory.Server.getUserInfo(
                            this.oauthService.findAliveAccessToken(accessToken)))
                    .build();


        } catch (InvalidAccessTokenException e) {
            return   RevokeTokenEndPoint.invalidTokenErrorResponse(e);
        } catch (URISyntaxException e) {
            return TokenEndpoint.unhandledErrorTokenResponse();
        }
    }
}
