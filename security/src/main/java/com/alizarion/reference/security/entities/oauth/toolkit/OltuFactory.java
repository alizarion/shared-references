package com.alizarion.reference.security.entities.oauth.toolkit;

import com.alizarion.reference.security.entities.Token;
import com.alizarion.reference.security.entities.oauth.OAuthAccessToken;
import com.alizarion.reference.security.entities.oauth.client.OAuthClientAuthorization;
import com.alizarion.reference.security.entities.oauth.client.OAuthServerApplication;
import com.alizarion.reference.security.entities.oauth.server.OAuthServerAuthorization;
import com.alizarion.reference.security.exception.oauth.OAuthException;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;

/**
 * @author selim@openlinux.fr.
 */
public class OltuFactory {



    public static class ClientHelper{

        public static OAuthClientRequest makeAuthCodeRequest(
                final OAuthClientAuthorization bizAuth)
                throws OAuthSystemException, URISyntaxException {
            OAuthServerApplication  apiParams =
                    (OAuthServerApplication) bizAuth.getAuthApplication();
            return  OAuthClientRequest
                    .authorizationLocation(apiParams.getApiAuthzUrl().toString())
                    .setClientId(apiParams.getApplicationKey().getClientId())
                    .setRedirectURI(apiParams.getRedirectURI().toString())
                    .setResponseType(ResponseType.CODE.toString())
                    .setScope(apiParams.getDefaultScopesCommaSeparated())
                    .setState(bizAuth.getState().getValue())
                    .buildQueryMessage();
        }

        public static OAuthClientAuthorization makeTokenRequestWithAuthCode(
                final OAuthClientAuthorization bizAuth,
                final String authCode)
                throws OAuthSystemException, OAuthProblemException, OAuthException, URISyntaxException {
            OAuthServerApplication  apiParams =
                    (OAuthServerApplication) bizAuth.getAuthApplication();
            OAuthClientRequest request = OAuthClientRequest
                    .tokenLocation(apiParams.getApiTokenUrl().toString())
                    .setClientId(apiParams.getApplicationKey().getClientId())
                    .setClientSecret(apiParams.getApplicationKey().getClientSecret())
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setCode(authCode)
                    .setRedirectURI(apiParams.getRedirectURI().toString())
                    .buildBodyMessage();
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthAccessTokenResponse oauthResponse = oAuthClient.accessToken(request);
            if (!StringUtils.isEmpty(oauthResponse.getAccessToken())){
                bizAuth.addAccessToken(oauthResponse.getExpiresIn(),
                        oauthResponse.getAccessToken());
            }   else {
                throw new OAuthException("no access token in the response " + oauthResponse.toString());
            }
            if (!StringUtils.isEmpty(oauthResponse.getAccessToken())){
                bizAuth.setRefreshToken(new Token(oauthResponse.getRefreshToken()));
            }
            return bizAuth;
        }


        public static OAuthClientAuthorization makeTokenRequestWithRefreshCode(
                final OAuthClientAuthorization bizAuth)
                throws OAuthSystemException, OAuthProblemException, OAuthException {
            OAuthServerApplication  apiParams =
                    (OAuthServerApplication) bizAuth.getAuthApplication();
            OAuthClientRequest request = OAuthClientRequest
                    .tokenLocation(apiParams.getApiTokenUrl().toString())
                    .setClientId(apiParams.getApplicationKey().getClientId())
                    .setClientSecret(apiParams.getApplicationKey().getClientSecret())
                    .setGrantType(GrantType.REFRESH_TOKEN)
                    .setRefreshToken(bizAuth.getRefreshToken().getValue())
                    .buildBodyMessage();
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthAccessTokenResponse oauthResponse = oAuthClient.accessToken(request);
            if (!StringUtils.isEmpty(oauthResponse.getAccessToken())){
                bizAuth.addAccessToken(oauthResponse.getExpiresIn(),
                        oauthResponse.getAccessToken());
            }   else {
                throw new OAuthException("no access" +
                        " token in the " +
                        "response " +
                        oauthResponse.toString());
            }

            return bizAuth;
        }


    }

    public static class ServerHelper{

        public static OAuthResponse getOAuthAccessTokenResponse(
                final OAuthAccessToken accessToken)
                throws OAuthSystemException {
            OAuthASResponse.OAuthTokenResponseBuilder responseBuilder =
                    OAuthASResponse
                            .tokenResponse(HttpServletResponse.SC_OK)
                            .setAccessToken(accessToken
                                    .getBearer().getValue())
                            .setExpiresIn(accessToken
                                    .getBearer()
                                    .expireIn().toString());
            if (accessToken.getAuthorization().getRefreshToken()!= null){
                responseBuilder.
                        setRefreshToken(accessToken.
                                getAuthorization().
                                getRefreshToken().
                                getValue()) ;
            }
            return responseBuilder.buildJSONMessage();
        }

        public static OAuthResponse getOAuthAuthorizationResponse(
                final OAuthServerAuthorization authorization,
                final HttpServletRequest request) throws OAuthSystemException, URISyntaxException {
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(
                            request,
                            HttpServletResponse.SC_FOUND);

            if (authorization.getAuthCode() == null){
                OAuthAccessToken accessToken =
                        authorization
                                .getMostLifeTimeAccessToken();
                builder.setAccessToken(accessToken
                        .getBearer()
                        .getValue());
                builder.setExpiresIn(accessToken
                        .getBearer()
                        .expireIn());
            } else {
                builder.setCode(authorization
                        .getAuthCode()
                        .getValue());
            }
            return builder.
                    location(authorization
                            .getAuthApplication()
                            .getRedirectURI()
                            .toString())
                    .buildQueryMessage();

        }

    }

}
