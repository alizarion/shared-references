package com.alizarion.reference.security;

import com.alizarion.reference.security.entities.*;
import com.alizarion.reference.security.entities.oauth.OAuthApplicationKey;
import com.alizarion.reference.security.entities.oauth.client.OAuthScope;
import com.alizarion.reference.security.entities.oauth.client.OAuthScopeGroup;
import com.alizarion.reference.security.entities.oauth.client.OAuthServerApplication;
import com.alizarion.reference.security.entities.oauth.server.OAuthClientApplication;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author selim@openlinux.fr.
 */
public class SecurityTestFactory  {

    public static Set<RoleKey> getRoleKeys(){
        Set<RoleKey>  roleKeys  =  new HashSet<>();
        roleKeys.add(new RoleKey("test","test-key","mon role de test"));
        roleKeys.add(new RoleKey("test2","test2-key","mon role de test2"));

        return roleKeys;
    }

    public static Set<Role> getRoles(){
        Set<Role>  roles  =  new HashSet<>();
        for(RoleKey roleKey  :  getRoleKeys()){
            roles.add(new Role(roleKey));
        }
        return roles;
    }

    public static Set<OAuthScope> getScopes(){
        Set<OAuthScope>  scopes  =  new HashSet<>();
        for(RoleKey roleKey  :  getRoleKeys()){
            scopes.add(new OAuthScope(roleKey));
        }
        return scopes;
    }

    public static OAuthScopeGroup getScopeGroup(){
        return new OAuthScopeGroup(new RoleGroupKey("users","all users"), getScopes());
    }


    public static RoleGroup getRoleGroup(){
        return new RoleGroup(new RoleGroupKey("user-inapp","logged users"),getRoles());
    }

    public static Credential getPasswordRegistredCredential(String discriminator){
        return new Credential(discriminator + "FirstName",discriminator+"lastName",getRoles());
    }

    public static OAuthServerApplication getOAuthServerApplication() throws MalformedURLException {
        OAuthServerApplication application =
                new OAuthServerApplication("facebook",
                        new URL("http://facebook.com"),
                        new URL("http://facebook.com/api"),
                        new OAuthApplicationKey("clientId",
                                UUID.randomUUID().toString()));
        application.setAllowedScopes(getScopes());
        application.setRequestedScopeByDefault(getScopeGroup());
        application.setDefaultRoles(getRoleGroup());
        return application;
    }

    public static OAuthClientApplication getOAuthClientApplication() throws MalformedURLException {
        OAuthClientApplication application =
                new OAuthClientApplication("facebook",
                        new URL("http://facebook.com"),
                        new URL("http://facebook.com/api"));
        application.generateApplicationKey();
        application.setDefaultRoles(getRoleGroup());
        return application;
    }

}
