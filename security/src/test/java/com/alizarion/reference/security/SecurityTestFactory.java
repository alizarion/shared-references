package com.alizarion.reference.security;

import com.alizarion.reference.security.entities.*;

import com.alizarion.reference.security.oauth.entities.OAuthApplicationKey;
import com.alizarion.reference.security.oauth.entities.OAuthRole;
import com.alizarion.reference.security.oauth.entities.ScopeGroupKey;
import com.alizarion.reference.security.oauth.entities.ScopeKey;
import com.alizarion.reference.security.oauth.entities.client.OAuthScopeClient;
import com.alizarion.reference.security.oauth.entities.client.OAuthScopeClientGroup;
import com.alizarion.reference.security.oauth.entities.client.OAuthServerApplication;
import com.alizarion.reference.security.oauth.entities.server.OAuthClientApplication;
import com.alizarion.reference.security.oauth.entities.server.OAuthScopeServer;
import com.alizarion.reference.security.oauth.entities.server.OAuthScopeServerGroup;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
public class SecurityTestFactory  {

    public static RoleKey getRoleKeys(String disc){
        return new RoleKey(disc+" role key",disc+"-key","ma rolekey " +disc);
    }


    public static ScopeKey getScopeKeys(String disc){
        return new ScopeKey(disc+" scope key",disc+"-key","ma scopekey " +disc);
    }
    public static Role getRole(String disc){
        return new Role(getRoleKeys(disc));
    }

    public static OAuthScopeServer getServerScope(
            String scopeDisc,
            Role role){
        return new OAuthScopeServer(
                getScopeKeys(scopeDisc),
                role);
    }


    public static OAuthScopeClient getClientScopes(String scopeDisc ){
        return new OAuthScopeClient(
                getScopeKeys(scopeDisc));
    }

    public static OAuthScopeServerGroup getServerScopeGroup(
            String groupDisc,Set<OAuthScopeServer> scopeServers){
        return new OAuthScopeServerGroup(new ScopeGroupKey(
                groupDisc+"-group-server-scope-group",
                "all  "+groupDisc+" server scopes"),
                scopeServers);
    }


    public static OAuthScopeClientGroup getClientScopeGroup(String groupDisc,Set<OAuthScopeClient> scopeClients ){
        return new OAuthScopeClientGroup(new ScopeGroupKey(
                groupDisc+"-group-client-scope-group",
                "all  "+groupDisc+" client scopes"),scopeClients);
    }


    public static RoleGroup getRoleGroup(String groupDisc,Set<Role>  roles){
        return new RoleGroup(new RoleGroupKey(groupDisc+"-app-roles-group","logged users"),roles);
    }

    public static Credential getPasswordRegistredCredential(String discriminator,Set<Role> roles){
        Credential credential =  new Credential(discriminator + "username",roles);
        credential.setPassword("toto");
        return credential;
    }

    public static OAuthServerApplication getOAuthServerApplication(Set<Role> clientRoles) throws MalformedURLException, URISyntaxException {
        OAuthServerApplication application =
                new OAuthServerApplication("myproject",
                        new URL("http://myproject.com"),
                        new URI("http://myproject.com/api/callback"),
                        new URL("http://facebook.com/api/authz"),
                        new URL("http://facebook.com/api/token"),
                        new OAuthApplicationKey("clientId","clientSecret"));
        application.addClientAuthorizedScope(getClientScopes("email"));
        application.addClientAuthorizedScope(getClientScopes("notification"));
        application.setGrantedRoles((Set<OAuthRole>) (Set<?>) clientRoles);
        return application;
    }

    public static OAuthClientApplication getOAuthClientApplication(Set<Role> serverRoles) throws MalformedURLException, URISyntaxException {
        OAuthClientApplication application =
                new OAuthClientApplication("myproject",
                        new URL("http://myproject.com"),
                        new URI("http://myproject.com/api/callback"));
        application.generateApplicationKey();
        int i = 0;
        for (Role role : serverRoles){
            application.addAllowedServerScope(getServerScope("scope"+i,role));
            i++;
        }
        //same getApplicationKey that that the generated OAuthServerApplication
        application.setApplicationKey(
                getOAuthServerApplication(serverRoles).
                        getApplicationKey());
        return application;
    }

}
