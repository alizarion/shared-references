package com.alizarion.reference.security.oauth.jsf;

import com.alizarion.reference.security.doa.CredentialJpaDao;
import com.alizarion.reference.security.entities.oauth.OAuthCredential;
import com.alizarion.reference.security.entities.oauth.OAuthDuration;
import com.alizarion.reference.security.entities.oauth.server.OAuthServerAuthorization;
import com.alizarion.reference.security.entities.oauth.toolkit.OltuFactory;
import com.alizarion.reference.security.exception.InvalidUsernameException;
import com.alizarion.reference.security.exception.oauth.ClientIdNotFoundException;
import com.alizarion.reference.security.exception.oauth.InvalidScopeException;
import com.alizarion.reference.security.exception.oauth.OAuthException;
import com.alizarion.reference.security.services.oauth.OAuthServerService;
import com.alizarion.reference.security.services.resources.OAuthServerMBean;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;

/**
 * @author selim@openlinux.fr.
 */
@Named
public class AuthorizeCtrl implements Serializable {

    //TODO use jsf exception handler

    private static final long serialVersionUID = -298260025695046567L;

    @EJB
    private OAuthServerService oauthService;

    @PersistenceContext
    private EntityManager em;

    private OAuthServerAuthorization authorization;

    @EJB
    private OAuthServerMBean serverMBean;

    private FacesContext context;

    @PostConstruct
    public void init(){
        this.context = FacesContext
                .getCurrentInstance();

    }

    public void validate(){
        CredentialJpaDao credentialMangerDao
                = new CredentialJpaDao(this.em);


        try {
            OAuthCredential credential = credentialMangerDao
                    .getCredentialByUserName(context
                            .getExternalContext()
                            .getUserPrincipal()
                            .getName());
            OAuthAuthzRequest  authzRequest =
                    new OAuthAuthzRequest((HttpServletRequest)
                            context.getExternalContext()
                                    .getRequest());
            this.authorization =
                    this.oauthService
                            .getAuthorization
                                    (authzRequest.getClientId(),
                                            authzRequest.getRedirectURI(),
                                            authzRequest.getScopes(),
                                            authzRequest.getResponseType(),
                                            authzRequest.getParam(OAuthDuration.DURATION_PARAM)
                                            , credential);
        } catch (InvalidUsernameException |
                ClientIdNotFoundException |
                OAuthProblemException |
                OAuthSystemException |
                InvalidScopeException e) {
            int errorCode;
            if (e instanceof ClientIdNotFoundException
                    || e instanceof InvalidUsernameException){
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

    public  Long getAuthorizationDuration(){
        return this.serverMBean
                .getAccessTokenDurationSecond();
    }

    public OAuthServerAuthorization getAuthorization() {
        validate();
        return authorization;
    }

    public  void addAuthorization()
            throws OAuthException {
        try {
            OAuthServerAuthorization authorized = this.oauthService
                    .acceptAuthorization(
                            getAuthorization());

            FacesContext.getCurrentInstance().
                    getExternalContext().
                    redirect(OltuFactory
                            .ServerHelper
                            .getOAuthAuthorizationResponse(
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
