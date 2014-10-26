package com.alizarion.reference.security.oauth.services.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * @author selim@openlinux.fr.
 */
public class CipherHelper {

    public static boolean writeKeyStore(final KeyStore keyStore ,
                                        final URI uri,
                                        final String storePassword) {

        java.io.FileOutputStream fos = null;


        try {
            fos = new java.io.FileOutputStream(
                    uri.getPath());
            keyStore.store(fos, storePassword.toCharArray());
        } catch (FileNotFoundException e) {
            throw new SecurityException("keystore not found " +e);
        } catch (CertificateException e) {
            throw new SecurityException("CertificateException " +e);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException("NoSuchAlgorithmException " +e);
        } catch (KeyStoreException e) {
            throw new SecurityException("KeyStoreException " +e);
        } catch (IOException e) {
            throw new SecurityException("IOException " +e);

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return true;
    }
}
