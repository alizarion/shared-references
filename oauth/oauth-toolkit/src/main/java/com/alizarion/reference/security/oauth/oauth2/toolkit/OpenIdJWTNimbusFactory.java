package com.alizarion.reference.security.oauth.oauth2.toolkit;

import com.alizarion.reference.exception.NotImplementedException;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthAccessToken;
import com.alizarion.reference.security.oauth.oauth2.entities.server.OAuthSignatureKeyPair;
import com.alizarion.reference.security.oauth.oauth2.exception.OAuthOpenIDSignatureException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.id.Audience;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.id.Subject;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.openid.connect.sdk.claims.AccessTokenHash;
import com.nimbusds.openid.connect.sdk.claims.Gender;
import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;

import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author selim@openlinux.fr.
 */
public  class OpenIdJWTNimbusFactory {


    public static class Server {
        public static String getUserInfo(final OAuthAccessToken accessToken) throws URISyntaxException {
            UserInfo userInfo =
                    new UserInfo(new Subject(
                            accessToken
                                    .getAuthorization()
                                    .getCredential()
                                    .getIdToString()));
            if (accessToken.getAuthorization().getScopeKeys().contains("profile") ||
                    accessToken.getAuthorization().getScopeKeys().contains("email")){
                userInfo.setEmail(accessToken
                        .getAuthorization()
                        .getCredential()
                        .getEmail());
                userInfo.setEmailVerified(accessToken
                        .getAuthorization()
                        .getCredential()
                        .isEmailVerified());

            }
            if (accessToken.getAuthorization().getScopeKeys().contains("profile")){
                try {
                    userInfo.setGender(new Gender(accessToken
                            .getAuthorization()
                            .getCredential()
                            .getGender()));
                } catch (NotImplementedException ignored) {
                }
                try {
                    userInfo.setGivenName(accessToken
                            .getAuthorization()
                            .getCredential()
                            .getGivenName());
                } catch (NotImplementedException ignored) {
                }
                try {
                    userInfo.setFamilyName(accessToken
                            .getAuthorization()
                            .getCredential()
                            .getFamilyName());
                } catch (NotImplementedException ignored) {
                }
                try {
                    userInfo.setName(accessToken
                            .getAuthorization()
                            .getCredential()
                            .getName());
                } catch (NotImplementedException ignored) {
                }
                try {
                    userInfo.setProfile(accessToken
                            .getAuthorization()
                            .getCredential()
                            .getProfile().toURI());
                } catch (NotImplementedException ignored) {
                }
                try {
                    userInfo.setPicture(accessToken
                            .getAuthorization()
                            .getCredential()
                            .getPicture().toURI());
                } catch (NotImplementedException ignored) {
                }
                try {
                    userInfo.setPicture(accessToken
                            .getAuthorization()
                            .getCredential()
                            .getPicture().toURI());
                } catch (NotImplementedException ignored) {
                }
                try {
                    userInfo.setWebsite(accessToken
                            .getAuthorization()
                            .getCredential()
                            .getWebSite().toURI());
                } catch (NotImplementedException ignored) {
                }

            }

            return userInfo.toJSONObject().toJSONString();
        }


        public static String getSignedIDToken(final OAuthAccessToken accessToken,
                                              final Key aesKey,
                                              final OAuthSignatureKeyPair keyPair,
                                              final String issuer) throws OAuthOpenIDSignatureException {

            List<Audience> audiences = new ArrayList<>();
            audiences.add(new Audience(accessToken.getAuthorization()
                    .getAuthApplication()
                    .getApplicationKey()
                    .getClientId()));
            IDTokenClaimsSet claimsSet =
                    new IDTokenClaimsSet(
                            new Issuer(issuer),new Subject(accessToken
                            .getAuthorization()
                            .getCredential()
                            .getIdToString()),audiences,accessToken.getBearer()
                            .getCreationDate(),accessToken
                            .getBearer()
                            .getCreationDate());
            claimsSet.setAccessTokenHash(
                    AccessTokenHash.compute(
                            new BearerAccessToken(accessToken.
                                    getBearer().
                                    getValue()),
                            new JWSAlgorithm(keyPair.getSignatureAlgorithm())));
            try {

                JWSSigner signer = new RSASSASigner((RSAPrivateKey)
                        keyPair.decryptPrivateKey(aesKey));
                SignedJWT signedJWT = new SignedJWT(new
                        JWSHeader.Builder(new
                        JWSAlgorithm(keyPair.getSignatureAlgorithm()))
                        .keyID(keyPair
                                .getKid())
                        .build(),
                        claimsSet
                                .toJWTClaimsSet());
                signedJWT.sign(signer);
                return signedJWT.serialize();
            } catch (GeneralSecurityException | JOSEException | ParseException e) {
                throw new OAuthOpenIDSignatureException("cannot sign the JWT IDTOKEN " + e);
            }
        }

        public static String getJSONWebKeyCert(List<OAuthSignatureKeyPair> oAuthSignatureKeyPairs){


            List<JWK> jwks =  new ArrayList<>();
            for (OAuthSignatureKeyPair signatureKeyPair : oAuthSignatureKeyPairs){
                try {
                    JWK jwk =  new RSAKey.Builder((RSAPublicKey)signatureKeyPair.getPublicKey())
                            .keyID(signatureKeyPair.getKid()).
                                    keyUse(KeyUse.SIGNATURE).
                                    algorithm(JWSAlgorithm.RS256).build();
                    jwks.add(jwk);

                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
            JWKSet jwkSet = new JWKSet(jwks);
            return jwkSet.toString();
        }
    }




}
