package com.alizarion.reference.security.oauth.web.jsf;

import com.alizarion.reference.exception.NotImplementedException;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthDuration;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthResponseType;
import com.alizarion.reference.security.oauth.oauth2.entities.server.OAuthServerAuthorization;
import com.alizarion.reference.security.oauth.oauth2.exception.BadCredentialException;
import com.alizarion.reference.security.oauth.oauth2.exception.ClientIdNotFoundException;
import com.alizarion.reference.security.oauth.oauth2.exception.InvalidScopeException;
import com.alizarion.reference.security.oauth.oauth2.toolkit.OltuFactory;
import com.alizarion.reference.security.oauth.services.oauth.OAuthServerService;
import com.alizarion.reference.security.oauth.services.resources.OAuthServerMBean;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;

/**
 * OAuth2 authorization EndPoint, all enter point of all authorizations wokflows
 * return an authorization code if required (response_type=code)
 * return an access token if (response_type=token)
 * return openid token if (response_type=token openid)
 * @author selim@openlinux.fr.
 */
@Named
public class AuthorizeCtrl implements Serializable {

    //TODO use jsf exception handler

    private static final long serialVersionUID = -298260025695046567L;


    /**
     * OAuth2 services.
     * @see com.alizarion.reference.security.oauth.services.oauth.OAuthServerService
     */
    @EJB
    private OAuthServerService oauthService;


    /**
     * the requested authorization.
     */
    private OAuthServerAuthorization authorization;


    /**
     * Server application mBean params.
     */
    @EJB
    private OAuthServerMBean serverMBean;


    /**
     * Faces context that provide request object
     * and params used to generate
     * a valid authorization.
     */
    private FacesContext context;

    private OAuthResponseType responseType;

    @PostConstruct
    public void init(){
        this.context = FacesContext
                .getCurrentInstance();

    }

    /**
     * Method that parse oauth requested params,
     * and initiate an OAuthAuthorization business object that
     * will be used to provide the response.
     */
    public void validate(){

        try {


            OAuthAuthzRequest  authzRequest =
                    new OAuthAuthzRequest((HttpServletRequest)
                            context.getExternalContext()
                                    .getRequest());
            this.responseType = OAuthResponseType
                    .getInstance(authzRequest.getResponseType());

            this.authorization =
                    this.oauthService
                            .getAuthorization
                                    (authzRequest.getClientId(),
                                            authzRequest.getRedirectURI(),
                                            authzRequest.getScopes(),
                                            this.responseType,
                                            authzRequest.getParam(OAuthDuration.DURATION_PARAM)
                                            , context.getExternalContext()
                                                    .getUserPrincipal()
                                                    .getName());

            if (!this.authorization.isPromptRequired()){
                addAuthorization(this.authorization);
            }
        } catch (BadCredentialException |
                ClientIdNotFoundException |
                OAuthProblemException |
                OAuthSystemException |
                NotImplementedException |
                InvalidScopeException e) {
            int errorCode;
            if (e instanceof ClientIdNotFoundException
                    || e instanceof BadCredentialException) {
                errorCode = HttpServletResponse.SC_FORBIDDEN;
            } else {
                errorCode = HttpServletResponse.SC_BAD_REQUEST;

            }
            context.getExternalContext()
                    .setResponseStatus(errorCode);
            context.responseComplete();
            HttpServletResponse lHttpServletResponse =
                    ((HttpServletResponse) FacesContext
                            .getCurrentInstance()
                            .getExternalContext().getResponse());
            try {
                lHttpServletResponse
                        .sendError(errorCode);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Get access token duration.
     * @return Access token duration.
     */
    public  Long getAuthorizationDuration(){
        return this.serverMBean
                .getAccessTokenDurationSecond();
    }

    /**
     * Return the requested business authorisation.
     * @return requested business authorisation to the user front-end validation
     */
    public OAuthServerAuthorization getAuthorization() {
        validate();
        return authorization;
    }


    public void doAcceptAndGrant(){
        addAuthorization(getAuthorization());
    }

    /**
     * Method to merge the OAuthAuthorization
     * that have been validate by the user
     */
    private   void addAuthorization(OAuthServerAuthorization authorization)
    {
        try {
            OAuthServerAuthorization authorized = this.oauthService
                    .acceptAuthorization(
                            authorization);

            FacesContext.getCurrentInstance().
                    getExternalContext().
                    redirect(OltuFactory
                            .ServerHelper
                            .getOAuthAuthorizationResponse(
                                    this.responseType,
                                    authorized,
                                    (HttpServletRequest)
                                            this.context
                                                    .getExternalContext()
                                                    .getRequest())
                            .getLocationUri());
        } catch (URISyntaxException |
                OAuthSystemException |
                IOException e) {
            e.printStackTrace();
        }
    }
}
