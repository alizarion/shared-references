package com.alizarion.reference.security.oauth.web.rs;

import com.alizarion.reference.security.oauth.oauth2.exception.InvalidTokenException;
import com.alizarion.reference.security.oauth.services.oauth.OAuthServerService;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Path("/revoke")
public class RevokeTokenEndPoint implements Serializable {

    private static final long serialVersionUID = 5424681464037246170L;

    public static final String TOKEN_PARAMETER_NAME = "token";
    /**
     * OAuth2 services.
     * @see com.alizarion.reference.security.oauth.services.oauth.OAuthServerService
     */
    @EJB
    private OAuthServerService oauthService;

    /**
     * Method to revoke and access token
     * @param request contain token to revoke
     * @return status ok if revoked
     */
    @POST
    @Produces("application/json")
    public Response authorize(@Context HttpServletRequest request)
            throws OAuthSystemException {
        try {
            oauthService.revokeToken(request.getParameter(TOKEN_PARAMETER_NAME));
            return Response.status(HttpServletResponse.SC_OK).build();
        } catch (InvalidTokenException e) {
            return invalidTokenErrorResponse(e);
        }
    }

    public static Response invalidTokenErrorResponse(Exception e) throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                .setErrorDescription(e.getMessage())
                .buildJSONMessage();

        return Response.status(
                response.getResponseStatus())
                .entity(response.getBody()).build();
    }

}
