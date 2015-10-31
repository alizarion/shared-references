package com.alizarion.reference.test;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sun.jersey.core.util.Base64;
import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;
import org.apache.oltu.jose.jws.JWS;
import org.apache.oltu.jose.jws.io.JWSWriter;
import org.apache.oltu.jose.jws.signature.impl.SignatureMethodsHMAC256Impl;
import org.apache.oltu.jose.jws.signature.impl.SymmetricKeyImpl;
import org.apache.oltu.oauth2.jwt.JWT;
import org.apache.oltu.oauth2.jwt.io.JWTClaimsSetWriter;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.crypto.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author selim@openlinux.fr.
 */
public class Test {
    public class TITI{
        private TOTO toto;

        public boolean isTotoAlive(){
            return this.toto != null && toto.isAlive();
        }
    }

    public class TOTO{

        public boolean isAlive(){
            return true;
        }
    }

    //  @org.junit.Test
    public void simpleJavaTest() throws UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException {
        KeyStore ks = KeyStore.getInstance("jks");
        ks.load(new FileInputStream("/Users/sphinx/keystore.jks"), "titoune".toCharArray());
        Key key = ks.getKey("openid","titoune".toCharArray());
        JWT jwt = new JWT.Builder()
                .setClaimsSetIssuer("788732372078-pas6c4tqtudpoco2f4au18e00suedjtb@developer.gserviceaccount.com")
                .setClaimsSetCustomField("scope", " https://www.googleapis.com/auth/plus.login")
                .setClaimsSetAudience("https://accounts.google.com/o/oauth2/token")
                .setClaimsSetIssuedAt(System.currentTimeMillis() / 1000)
                .setClaimsSetExpirationTime(System.currentTimeMillis() / 1000 + 3600)
                .build();
        String payload = new JWTClaimsSetWriter().write(jwt.getClaimsSet());
        JWS jws = new JWS.Builder()
                .setType("JWT")
                .setAlgorithm("RS256")
                .setPayload(payload).sign(new SignatureMethodsHMAC256Impl(), new SymmetricKeyImpl(key.getEncoded())).build();
        System.out.println(jws.getHeader().toString());
        System.out.println("your assertion is " + new JWSWriter().write(jws));


    }
    // @org.junit.Test
    public void teest2() throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair =  keyPairGen.genKeyPair();
        System.out.println("-----private key-----");
        PrivateKey privateKey = keyPair.getPrivate();
        String pk = new String(Base64.encode(privateKey.getEncoded()));
        System.out.println(pk);
        System.out.println("-----private key-----");
        PublicKey publicKey =  keyPair.getPublic();
        System.out.println("-----public key-----");
        System.out.println(new String(Base64.encode(publicKey.getEncoded())));
        System.out.println("-----public key-----");

        String message = "mon message a singer";


        Signature signature =  Signature.getInstance("SHA1withRSA");

        signature.initSign(privateKey);
        signature.update(message.getBytes());
        byte[] sign = signature.sign();
        System.out.println("-----signature-----");
        System.out.println(new String(Base64.encode(sign)));
        System.out.println("-----signature-----");


        signature.initVerify(publicKey);
        signature.update(message.getBytes());
        System.out.print(signature.verify(sign));



        Cipher cipher = Cipher.getInstance("RSA");

        MessageDigest md  =  MessageDigest.getInstance("SHA1");


        md.update(message.getBytes());
        byte[] digest = md.digest();






        byte[] algodig = "SHA1".getBytes();

        byte[] digedtinfo = new byte[algodig.length + digest.length];

        System.arraycopy(algodig,0,digedtinfo,0,algodig.length);
        System.arraycopy(digest,0,digedtinfo,algodig.length,digest.length);

        System.out.println("-----digest-----");
        System.out.println(new String(Base64.encode(digedtinfo)));
        System.out.println("-----digest-----");



        cipher.init(Cipher.ENCRYPT_MODE,privateKey);
        cipher.update(digedtinfo);
        byte[] sign2 =  cipher.doFinal();
        System.out.println("-----signature2-----");
        System.out.println(new String(Base64.encode(sign2)));
        System.out.println("-----signature2-----");
    }

    // @org.junit.Test
    public void encryptDecrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        System.out.println("----chiffrement asymetrique ---");

        Cipher  cipher = Cipher.getInstance("RSA");
        String message =  "mon message a encrypter";

        KeyPairGenerator keyPairGenerator =  KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        cipher.init(Cipher.ENCRYPT_MODE,keyPair.getPrivate());

        cipher.update(message.getBytes());
        byte[] encryptedMessage = cipher.doFinal();
        System.out.println("message encrypter avec clé privé : " + new String(Base64.encode(encryptedMessage)));

        cipher.init(Cipher.DECRYPT_MODE,keyPair.getPublic());
        cipher.update(encryptedMessage);
        String decryptedMessage = new String(cipher.doFinal());

        System.out.println("message encrypter avec la clé privé et decrypter avec la clé publique : " + decryptedMessage);

        cipher.init(Cipher.ENCRYPT_MODE,keyPair.getPublic());
        cipher.update(message.getBytes());
        String encryptedWithPublicKey = new String(Base64.encode(cipher.doFinal()));

        System.out.println("encrypter avec clé publique : "+encryptedWithPublicKey);

        cipher.init(Cipher.DECRYPT_MODE,keyPair.getPrivate());
        cipher.update(Base64.decode(encryptedWithPublicKey.getBytes()));

        System.out.println("encrypter avec clé publique : "+new String(cipher.doFinal()));
        System.out.println("----chiffrement asymetrique ---");

        System.out.println("----chiffrement symetrique ---");


        Key AESKey =  KeyGenerator.getInstance("AES").generateKey();
        cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE,AESKey);


        String symetriqueEncryptedMessage =  new String(Base64.encode(cipher.doFinal(message.getBytes())));

        System.out.println("symetrique encoded message :" + symetriqueEncryptedMessage) ;

        cipher.init(Cipher.DECRYPT_MODE, AESKey);

        System.out.println("symetrique decrypted message : " + new String(cipher.doFinal(Base64.decode(symetriqueEncryptedMessage.getBytes()))));

    }

    //@org.junit.Test
    public void testJWSWithNimbus() throws NoSuchAlgorithmException, JOSEException, ParseException, CertificateException, IOException {

        KeyPairGenerator generator =KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair =  generator.generateKeyPair();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        JWSSigner signer = new RSASSASigner(privateKey);

        JWTClaimsSet claimsSet = new JWTClaimsSet();
        claimsSet.setIssuer("myapp.com");
        claimsSet.setIssueTime(new Date());
        claimsSet.setExpirationTime(new Date(new Date().getTime() + 3600*1000) );
        claimsSet.setAudience("clientId");
        claimsSet.setSubject("userID");
        claimsSet.setType("JWT");
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256).keyID("1").build();
        SignedJWT signedJWT =  new SignedJWT(jwsHeader,claimsSet);

        signedJWT.sign(signer);

        String signedJWTString = signedJWT.serialize();
        System.out.println("---------JWT-------");

        System.out.println("serialized signed json web token " + signedJWTString);

        System.out.println("Signed JWT headers : "+signedJWT.getHeader().toString());
        System.out.println("JWT claimset :" + signedJWT.getJWTClaimsSet().toString());
        System.out.println("JWT Signature :" + signedJWT.getSignature());

        System.out.println("---------end-------");

        // Create a new JWK selector and configure it
        JWKSelector selector = new JWKSelector();

        // Select public keys only
        selector.setPublicOnly(true);

        // RSA keys only
        selector.setKeyType(KeyType.RSA);

        // No key use specified or signature
        selector.setKeyUses(KeyUse.SIGNATURE, null);


        JWK jwk =  new RSAKey.Builder(publicKey).keyID("1").keyUse(KeyUse.SIGNATURE).algorithm(JWSAlgorithm.RS256).build();


        JWKSet jwkSet = new JWKSet(jwk);
        System.out.println(jwkSet);

    }
    //@org.junit.Test
    public void OStemporaryFolder(){
        System.out.println(System.getProperty("java.io.tmpdir"));
    }

    //  @org.junit.Test
    public void accessTokenHash() throws NoSuchAlgorithmException {
        String accesstoken = "ya29.oQAU6ezl3wMhoiwVhVmEXfp4qi3aI_eOyT1EScHF7I1VhwToiPP95B0F";
        String tokenHash = "b_lr1ELuNFIwDF7nkUb9lg";

        byte[] decode = Base64.decode(accesstoken);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(decode);
        byte[] accesstokendigest = messageDigest.digest();

        byte[] athash = new byte[128];
        int length = accesstokendigest.length >128 ? 128 : accesstokendigest.length;
        System.arraycopy(accesstokendigest,0,athash,0,length);
        System.out.println(org.apache.commons.codec.binary.Base64.encodeBase64URLSafe(accesstokendigest));
    }

    //@org.junit.Test
    public void userKeyStoreAes() throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, UnrecoverableEntryException, URISyntaxException {
        KeyStore keyStore;

        KeyStore.SecretKeyEntry secretKeyEntry;
        KeyStore.ProtectionParameter protectionParameter;

        KeyGenerator keyGenerator;
        SecretKey secretKey, newSecretKey;


        keyStore = KeyStore.getInstance("jceks");
        keyStore.load(getClass().getResourceAsStream("/keystore.jck"), "store123".toCharArray());
        KeyStore.ProtectionParameter protParam =
                new KeyStore.PasswordProtection("key123".toCharArray());
        KeyStore.SecretKeyEntry keyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry("jceks", protParam);
        if (keyEntry != null) {
            System.out.println(new String(Base64.encode(keyEntry.getSecretKey().getEncoded())));

        }
        keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        newSecretKey = keyGenerator.generateKey();
        protectionParameter = new KeyStore.PasswordProtection("key123".toCharArray());
        secretKeyEntry = new KeyStore.SecretKeyEntry(newSecretKey);
        keyStore.setEntry("oauth", secretKeyEntry, protectionParameter);
        java.io.FileOutputStream fos = null;

        try {

            System.out.println("path" + getClass().getResource("/keystore.jck"));
            fos = new java.io.FileOutputStream(getClass().getResource("/keystore.jck").getPath());
            keyStore.store(fos, "store123".toCharArray());
        } finally {
            if (fos != null) {
                fos.close();
            }
        }


    }

    //@org.junit.Test
    public void PhantomJStest() throws InterruptedException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "/opt/local/bin/phantomjs");
        caps.setJavascriptEnabled(true);
        caps.setCapability("takesScreenshot", true);
        //caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A403 Safari/8536.25");
        caps.setCapability("ignoreZoomSetting",false);
        PhantomJSDriver driver = new PhantomJSDriver(caps);
        String url = "http://www.responsivewebdesign.co.uk/";
        for (DeviceViewPort viewPort :  DeviceViewPort.values()){
            driver.manage().window().setSize(new Dimension(viewPort.getViewportWidth(), viewPort.getViewportWidth() * 2));

            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            driver.get(url);
            //Thread.sleep(4000);
            java.io.
                    File landscape = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            System.out.println("File:" + landscape);
            landscape.renameTo(new File(System.getProperty("java.io.tmpdir") + File.separator+viewPort.toString()+".jpg"));

        }
        driver.quit();

    }

    //@org.junit.Test
    public void digestUrl() throws Exception {

        HtmlFetcher fetcher = new HtmlFetcher();
        // set cache. e.g. take the map implementation from google collections:
        // fetcher.setCache(new MapMaker().concurrencyLevel(20).maximumSize(count).
        //    expireAfterWrite(minutes, TimeUnit.MINUTES).makeMap();

        JResult res = fetcher.fetchAndExtract("http://korben.info/quoi-google.html", 5000, true);
        String text = res.getText();
        String title = res.getTitle();
        String rss = res.getRssUrl();
        String imageUrl = res.getImageUrl();
        System.out.println("title :"+title );
        System.out.println("rss :"+rss );
        System.out.println("video :"+res.getVideoUrl() );
        System.out.println("date :"+res.getDate() );

        System.out.println("getOriginalUrl :"+res.getOriginalUrl() );
        System.out.println("getCanonicalUrl :"+res.getCanonicalUrl() );

        System.out.println("description :"+res.getDescription() );


        System.out.println("imageUrl :"+imageUrl );

        for (String keyword : res.getKeywords()){
            System.out.print(keyword);
        }

    }

    public  class Animal {

        public void seDeplace(){
          System.out.println("je me déplace");
        }
    }

    public class ChauveSouri extends Animal{

        public void seDeplace(){
            super.seDeplace();
             System.out.println(" en vollant");
        }
    }


    //@org.junit.Test
    public void simpleTest() throws IOException {
        ChauveSouri chauveSouri = new ChauveSouri();
        chauveSouri.seDeplace();
        BufferedImage image = ImageIO.read(new java.net.URL("http://www.google.com/favicon.ico"));
         System.out.print(image.getHeight());

    }
}
