OAuth
=====

Auteur : Selim Bensenouci

Mais quoi que c'est?
===================

c'est une simple implémentation Server/Client du protocole OAuth2/OpenID plugable sur un projet utilisant hibernate, ejb3 et jax-rs.   

Le projet se décompose en 3 partie :
 
 * `oauth2` : contient l'implémentation JPA du protocole OAuth partie Serveur et Client.
 * `oauth2-services-ejb` : expose un ensemble d'EJBs  qui permettent d'expoiter les entités contenu dans `oauth2`.
 * `oauth2-authorization-web` : expose les differents EndPoint, implémentation http de la partie serveur du protocole oauth2,   
 serveur d'authorization(oauth2) et d'identification(openID connect).   

Pour simplifier les implémentations et d'accroître la flexibilité, OpenID Connect permet l'utilisation d'un «Discovery Document»,   
un document JSON trouvé à un endroit précis contenant la configuration du serveur OpenID Connect, y compris les URI de l'autorisation(oauth2)   
token, identifications et certs JWK permettant de valider les signature openID.   

voici pour exemple, la configuration actuel   

[http://localhost:8080/oauth2-authorization-web/.well-known/openid-configuration]

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
Comment ca marche?
==================



