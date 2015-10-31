package com.alizarion.reference.security.oauth.web.rs;


import com.alizarion.reference.security.oauth.oauth2.entities.OAuthAccessToken;
import com.alizarion.reference.security.oauth.oauth2.exception.*;
import com.alizarion.reference.security.oauth.oauth2.toolkit.OltuFactory;
import com.alizarion.reference.security.oauth.oauth2.toolkit.OpenIdJWTNimbusFactory;
import com.alizarion.reference.security.oauth.services.oauth.CipherSecurityInitializer;
import com.alizarion.reference.security.oauth.services.oauth.OAuthServerService;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

/**
 * OAuth2 token EndPoint,
 * @author selim@openlinux.fr.
 */
@Path("/token")
public class TokenEndpoint {

    public static final String INVALID_CLIENT_DESCRIPTION = "Client authentication failed (e.g., unknown client, no client authentication included, or unsupported authentication method).";

    @EJB
    private OAuthServerService oauthService;

    @EJB
    private CipherSecurityInitializer securityInitializer;

    /**
     * Method call to provide access token
     * @param request http request that contain oauth request params.
     * @return  http response.
     * @throws OAuthSystemException
     */
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response authorize(@Context HttpServletRequest request) throws OAuthSystemException {
        try {
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

            OAuthAccessToken accessToken = null;
            try {
                //first we check if the client has pass the correct secret
                if((oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE)
                        .equals(GrantType.PASSWORD.toString()))){

                    accessToken = this.oauthService
                            .getAccessTokenByPassword(
                                    oauthRequest.getUsername(),
                                    oauthRequest.getPassword(),
                                    oauthRequest.getScopes(),
                                    oauthRequest.getClientId());


                }    else {

                    // check client_id and secret
                    this.oauthService.authenticateClientRequest(
                            oauthRequest.getClientId(),
                            oauthRequest.getClientSecret());
                    // do checking for different grant types
                    if (oauthRequest
                            .getParam(OAuth.OAUTH_GRANT_TYPE)
                            .equals(GrantType.AUTHORIZATION_CODE.toString())) {

                        try {
                            accessToken =
                                    this.oauthService
                                            .getNewAccessTokenByOAuthCode(
                                                    oauthRequest
                                                            .getCode(),
                                                    oauthRequest.getClientId());

                        } catch (InvalidAuthCodeException e) {
                            return buildBadAuthCodeResponse();
                        }






                    } else if (oauthRequest
                            .getParam(OAuth.OAUTH_GRANT_TYPE)
                            .equals(GrantType.REFRESH_TOKEN.toString())) {
                        accessToken = this.oauthService
                                .getNewAccessTokenByOAuthRefreshToken(
                                        oauthRequest.getRefreshToken(),
                                        oauthRequest.getClientId());


                    }
                }
                if (accessToken == null){
                    return unhandledErrorTokenResponse();
                }  else {
                    String idToken = null;
                    if(accessToken.getAuthorization().getScopeKeys().contains("openid")){
                        idToken = OpenIdJWTNimbusFactory.Server.getSignedIDToken(
                                accessToken,
                                securityInitializer.getAESKey(),
                                oauthService.getOrderedAliveCerts().get(0),
                                request.getServerName());
                        //TODO issuer!!!
                    }
                    OAuthResponse response =
                            OltuFactory
                                    .ServerHelper
                                    .getOAuthAccessTokenResponse(accessToken,idToken);



                    return Response.status(response
                            .getResponseStatus())
                            .entity(response.getBody())
                            .build();
                }
            } catch (BadCredentialException e) {
                return buildInvalidClientSecretResponse();
            } catch (ClientIdNotFoundException e) {
                return buildInvalidClientIdResponse();
            } catch (TokenExpiredException | InvalidRefreshTokenException e) {
                return buildInvalidRefreshTokenResponse();
            } catch (InvalidScopeException e) {
                return buildInvalidScopeResponse(e);
            } catch (URISyntaxException | OAuthOpenIDSignatureException e) {
                e.printStackTrace();
                return unhandledErrorTokenResponse();
            }
        } catch (OAuthProblemException e) {
            OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            return Response.status(res.getResponseStatus()).entity(res.getBody()).build();
        }
    }

    private Response buildInvalidClientIdResponse() throws OAuthSystemException {
        OAuthResponse response =
                OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    private Response buildInvalidClientSecretResponse() throws OAuthSystemException {
        OAuthResponse response =
                OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT).setErrorDescription(INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    private Response buildBadAuthCodeResponse() throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("invalid authorization code")
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    private Response buildInvalidRefreshTokenResponse() throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                .setErrorDescription("invalid refresh token or has expired")
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    public static Response unhandledErrorTokenResponse() throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.SERVER_ERROR)
                .setErrorDescription("Oups ...something has throw an unhandled error")
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    private Response buildInvalidScopeResponse(InvalidScopeException e) throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_SCOPE)
                .setErrorDescription(e.getMessage())
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

}