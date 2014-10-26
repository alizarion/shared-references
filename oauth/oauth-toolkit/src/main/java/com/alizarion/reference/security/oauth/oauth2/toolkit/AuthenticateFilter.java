package com.alizarion.reference.security.oauth.oauth2.toolkit;

import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Jax-RS filter to secure oauth protected resources.
 * targeted authorisation server must be configured in web.xml <br/>
 * and declared as jax-rs provider
 * example targeted auth server: in the web.xml<br/>
 * <context-param>
 *    <param-name>oauth-authorization-server</param-name>
 *    <param-value>http://localhost:8080/oauth/api/oauth2/tokeninfo</param-value>
 * </context-param>
 *  <br/>
 *  example provider config in the web xml: <br/>
 *   <context-param>
 *        <param-name>resteasy.providers</param-name>
 *       <param-value>com.alizarion.reference.security.oauth.oauth2.toolkit.AuthenticateFilter</param-value>
 *   </context-param>
 * @author selim@openlinux.fr.
 */
//@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticateFilter implements ContainerRequestFilter {


    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Bearer ";
    private static final String TOKEN_VERIFICATION_PARAM = "access_token";
    private static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401, new Headers<>());
    private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nobody can access this resource", 403, new Headers<>());
    private static final ServerResponse SERVER_ERROR = new ServerResponse("INTERNAL SERVER ERROR", 500, new Headers<>());

    @Context
    private ServletContext context;

    @Override
    public void filter(ContainerRequestContext requestContext)
    {
        ResourceMethodInvoker methodInvoker =
                (ResourceMethodInvoker)
                        requestContext
                                .getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();
        try {
            URI oauthService = new URI(context.getInitParameter("oauth-authorization-server"));

            //Access allowed for all
            if(!method.isAnnotationPresent(PermitAll.class)) {
                //Access denied for all
                if (method.isAnnotationPresent(DenyAll.class)) {
                    requestContext.abortWith(ACCESS_FORBIDDEN);
                    return;
                }

                //Get request headers
                final MultivaluedMap<String, String> headers = requestContext.getHeaders();

                //Fetch authorization header
                final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

                //If no authorization information present; block access
                if (authorization == null || authorization.isEmpty()) {
                    requestContext.abortWith(ACCESS_DENIED);
                    return;
                }


                //Get encoded bearer token
                final String bearerToken = authorization.get(0)
                        .replaceFirst(AUTHENTICATION_SCHEME, "");
                Client client = ClientBuilder.newClient();
                TokenInfoDTO tokenInfo = client.target(oauthService)
                        .queryParam(TOKEN_VERIFICATION_PARAM, bearerToken)
                        .request().get(TokenInfoDTO.class);

                if (tokenInfo == null) {
                    requestContext.abortWith(SERVER_ERROR);
                    return;
                }

                //Verify user access
                if (method.isAnnotationPresent(RolesAllowed.class)) {
                    RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                    Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));

                    //Is user valid?
                    if (!isUserAllowed(tokenInfo, rolesSet)) {
                        requestContext.abortWith(ACCESS_DENIED);
                    }
                }
            }
        } catch (URISyntaxException |BadRequestException e) {
            requestContext.abortWith(SERVER_ERROR);

        }
    }

    private boolean isUserAllowed(final TokenInfoDTO tokenInfo, final Set<String> rolesSet)
    {
        return tokenInfo.getScope().containsAll(rolesSet);
    }
}