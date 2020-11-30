package it.satispay.signatureimpl.service;

import it.satispay.signatureimpl.utilities.Constants;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Service {

    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    public String createTheDigest(String stringToHash){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(Constants.ALGORITHM);
            byte[] hashBytes = digest.digest(stringToHash.getBytes(UTF_8));
            String finalHashString = Base64.getEncoder().encodeToString(hashBytes);
            return Constants.ALGORITHM + "=" + finalHashString;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Something went wrong in method createTheDigest() inside Class Service" + e);
            return null;
        }
    }

    public String readAndGetPrivateKeyFromFile() {
        try {
            File file = new File("src/main/resources/keys/client-rsa-private-key.pem");
            String key = null;
            key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
            String privateKeyClean = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("\n", "")
                    .replace("-----END PRIVATE KEY-----", "");

            return privateKeyClean;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Something went wrong in method readAndGetPrivateKeyFromFile() inside Class Service" + e);
            return null;
        }
    }

    public String readAndGetPublicKeyFromFile() {
        try {
            File file = new File("src/main/resources/keys/client-rsa-public-key.txt");
            String key = null;
            key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
            String publicKeyClean = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("\n", "")
                    .replace("-----END PUBLIC KEY-----", "");

            return publicKeyClean;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Something went wrong in method readAndGetPublicKeyFromFile() inside Class Service" + e);
            return null;
        }
    }

    public String createTheString(String method, String stringToHash){
        return getRequestTarget(method.toLowerCase()) + "\n" + "host: " + Constants.HOST + "\n" + "date: " + getDateFormatted() + "\n" + "digest: " + createTheDigest(stringToHash);
    }

    //This method Sign with RSA (rsa-sha256) algorithm a String with a private key, using Base64 as output.
    public String createTheSignature(String stringToSign, String privateKey){
        try {
            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            PKCS8EncodedKeySpec encodedPrivateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKeyObject = keyFactory.generatePrivate(encodedPrivateKeySpec);
            privateSignature.initSign(privateKeyObject);
            privateSignature.update(stringToSign.getBytes("UTF-8"));
            byte[] signatureValue = privateSignature.sign();
            return Base64.getEncoder().encodeToString(signatureValue);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Something went wrong in method createTheSignature() inside Class Service" + e);
            return null;
        }
    }

    public boolean verifyTheSignature(String stringSigned, String signature, String publicKey) {
        try {
            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            byte[] data = Base64.getDecoder().decode((publicKey.getBytes()));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey publicKeyObject = fact.generatePublic(spec);
            publicSignature.initVerify(publicKeyObject);
            publicSignature.update(stringSigned.getBytes(UTF_8));
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return publicSignature.verify(signatureBytes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Something went wrong in method verifyTheSignature() inside Class Service" + e);
            return false;
        }
    }

    public String composeTheAuthorizationHeader(String signature){
        String authorization = "Signature " + "keyId=" + "\"" + Constants.KEY_ID +
                "\", " + "algorithm=" + "\"" + Constants.ALGORITHM_AUTHORIZATION_VALUE + "\", " + "headers=" +
                "\"(request-target) host date digest\", " + "signature=" + "\"" + signature + "\"";
        return authorization;
    }

    public String getDateFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 0", Locale.ENGLISH);
        return formatter.format(new Date());
        //return "Mon, 18 Mar 2019 15:10:24 +0000";
    }

    public String getRequestTarget(String method) {
        return Constants.REQUEST_TARGET_FIELD_NAME + " " + method + " " + Constants.REQUEST_TARGET_VALUE;
    }
}
