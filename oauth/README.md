OAuth
=====

Auteur : Selim Bensenouci

What is that?
==============

This is a simple implementation Server/Client of OAuth2/OpenID protocol  using hibernate, ejb3 and jax-rs. 

The project consists in 3 parts :
 
 * `oauth2` : contains the JPA implementation of the OAuth protocol for Server and Client.
 * `oauth2-services-ejb` : provide a set of EJBs that manage entities contained in`oauth2`.
 * `oauth2-authorization-web` : provide different HTTP EndPoint that implement server side of the OAuth2 protocol, server authorization (OAuth2) and identity (OpenID connect).  

To simplify implementations and increase flexibility, OpenID Connect allows the use of a "Discovery document," a    JSON document found at a well-known location containing key-value pairs which provide details about the OpenID    Connect provider's configuration, including the URIs of the authorization, token, userinfo, and public-keys    endpoints. The Discovery document  may be retrieved from:    

http://localhost:8080/oauth2-authorization-web/.well-known/openid-configuration    

Here is an example of such a document; the field names are those specified in OpenID Connect Discovery 1.0      (refer to that document for their meanings). The values are purely illustrative and might change.
 
[http://localhost:8080/oauth2-authorization-web/.well-known/openid-configuration](http://localhost:8080/oauth2-authorization-web/.well-known/openid-configuration)   

```json
{
 "issuer": "localhost",
 "authorization_endpoint": "https://localhost/oauth2/auth",
 "token_endpoint": "https://localhost/oauth2/token",
 "userinfo_endpoint": "https://localhost/plus/v1/people/me/openIdConnect",
 "revocation_endpoint": "https://localhost/o/oauth2/revoke",
 "jwks_uri": "https://localhost/oauth2/certs",
 "response_types_supported": [
  "code",
  "token"
 ],
 "subject_types_supported": [
  "public"
 ],
 "id_token_alg_values_supported": [
  "RS256"
 ],
 "token_endpoint_auth_methods_supported": [
  "client_secret_post"
 ]
}
```   

System requirements
====================

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.   

The application this project produces is designed to be run on JBoss WildFly.

Configuration
==============

/!\ if some points are not clear, you can always consult the [ShowCase](https://github.com/alizarion/shared-references/tree/master/showcase) for further information.   

for the sake of modularity, the project has two interfaces (`OAuthCredential`,`OAuthRole`) that you MUST implement    to connect OAuth to your own app persisted credentials struture, and to prevent maven cyclic dependency the JPA     relationship for the target entity MUST be declared in orm.xml.    

```xml
<entity-mappings xmlns="http://java.sun.com/xml/ns/persistence/orm"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://java.sun.com/xml/ns/persistence/orm
                http://java.sun.com/xml/ns/persistence/orm_2_0.xsd"
                 version="2.0">

    <entity class="com.alizarion.reference.security.oauth.oauth2.entities.OAuthAuthorization">
        <attributes>
            <many-to-one name="credential" target-entity="my.own.app.code.MyCredential">
            </many-to-one>
        </attributes>
    </entity>

    <entity class="com.alizarion.reference.security.oauth.oauth2.entities.server.OAuthScopeServer">
        <attributes>
            <many-to-one name="role" target-entity="my.own.app.code.MyRole">
                <join-column name="oauth_role_id" ></join-column>
            </many-to-one>
        </attributes>
    </entity>

    <entity class="com.alizarion.reference.security.oauth.oauth2.entities.client.OAuthServerApplication">
        <attributes>
            <many-to-many name="grantedRoles" target-entity="my.own.app.code.MyRole">
            </many-to-many>
        </attributes>
    </entity>

</entity-mappings>

```
your persistence unit must also reference oauth entities, to do, we strongly advise to link  all your project entities  with the OAuth entities in a JAR PersistenceUnit `@see` [showcase-persistence](https://github.com/alizarion/shared-references/tree/master/showcase/showcase-persistence/src/main/resources/META-INF). 

the project already contain EJB JAR package and webapp WAR, so it MUST be deployed in a EAR package which can    also contain your application.









1. Application Server (JBoss wildfly)
-------------------------------------




