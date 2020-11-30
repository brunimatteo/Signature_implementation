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

    /**
     This method, given a string, produce the Digest string header Hashing
     the content of the body with sha256 algorithm using Base64 as output
     @param stringToHash The string to hash
     @return The Digest string header
     */
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

    /**
     This method read and return the private key (cleaned from \n, ecc...) in the file .pem inside
     the current project.
     @return The clean private key as String
     */
    public String readAndGetPrivateKeyFromFile() {
        try {
            File file = new File("src/main/resources/keys/client-rsa-private-key.pem");
            String key = null;
            key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
            String privateKeyClean = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("\n", "")
                    .replace("-----END PRIVATE KEY-----", "");

            System.out.println("PRIVATE_KEY:   " +  privateKeyClean);
            System.out.println("----------------------------------");

            return privateKeyClean;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Something went wrong in method readAndGetPrivateKeyFromFile() inside Class Service" + e);
            return null;
        }
    }

    /**
     This method read and return the public key (cleaned from \n, ecc...) in the file .txt inside
     the current project.
     @return The clean public key as String
     */
    public static String readAndGetPublicKeyFromFile() {
        try {
            File file = new File("src/main/resources/keys/client-rsa-public-key.txt");
            String key = null;
            key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
            String publicKeyClean = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("\n", "")
                    .replace("-----END PUBLIC KEY-----", "");

            System.out.println("PUBLIC_KEY:   " +  publicKeyClean);
            System.out.println("----------------------------------");

            return publicKeyClean;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Something went wrong in method readAndGetPublicKeyFromFile() inside Class Service" + e);
            return null;
        }
    }

    /**
     This method create the string needed to produce the signature
     @param method The current verb of the call to Satispay server
     @param stringToHash The string to hash to produce the Digest
     @param printOnConsole This is just a simple flag in order to print the String at console one time only
     @return The String formatted as per Documentation
     */
    public String createTheString(String method, String stringToHash, boolean printOnConsole){
        if(printOnConsole){
            System.out.println("STRING_TO_SIGN:\n" +  getRequestTarget(method.toLowerCase()) + "\n" + "host: " + Constants.HOST + "\n" + "date: " + getDateFormatted() + "\n" + "digest: " + createTheDigest(stringToHash));
            System.out.println("----------------------------------");
        }
        return getRequestTarget(method.toLowerCase()) + "\n" + "host: " + Constants.HOST + "\n" + "date: " + getDateFormatted() + "\n" + "digest: " + createTheDigest(stringToHash);
    }

    /**
     This method produce the signature
     @param stringToSign The string to sign
     @param privateKey The private key needed to produce the signature
     @return The signature as String
     */
    public String createTheSignature(String stringToSign, String privateKey){
        try {
            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            PKCS8EncodedKeySpec encodedPrivateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKeyObject = keyFactory.generatePrivate(encodedPrivateKeySpec);
            privateSignature.initSign(privateKeyObject);
            privateSignature.update(stringToSign.getBytes("UTF-8"));
            byte[] signatureValue = privateSignature.sign();

            verifyTheSignature(createTheString("GET","",true),Base64.getEncoder().encodeToString(signatureValue),readAndGetPublicKeyFromFile());

            System.out.println("SIGNATURE:   " +  Base64.getEncoder().encodeToString(signatureValue));
            System.out.println("----------------------------------");

            return Base64.getEncoder().encodeToString(signatureValue);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Something went wrong in method createTheSignature() inside Class Service" + e);
            return null;
        }
    }

    /**
     This method make sure that a message indeed comes from the creator of our public key
     @param stringUsedToProduceTheSignature The string used to produce the signature
     @param signature The signature
     @param publicKey The public key needed for the verification
     @return true if they do match, false if they don't
     */
    public static boolean verifyTheSignature(String stringUsedToProduceTheSignature, String signature, String publicKey) {
        try {
            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            byte[] data = Base64.getDecoder().decode((publicKey.getBytes()));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey publicKeyObject = fact.generatePublic(spec);
            publicSignature.initVerify(publicKeyObject);
            publicSignature.update(stringUsedToProduceTheSignature.getBytes(UTF_8));
            byte[] signatureBytes = Base64.getDecoder().decode(signature);

            System.out.println("MATCH:   " +  publicSignature.verify(signatureBytes));
            System.out.println("----------------------------------");

            return publicSignature.verify(signatureBytes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Something went wrong in method verifyTheSignature() inside Class Service" + e);
            return false;
        }
    }

    /**
     This method compose the Authorization Header value
     @param signature The signature
     @return The Authorization Header value formatted as per Documentation
     */
    public String composeTheAuthorizationHeader(String signature){
        String authorization = "Signature " + "keyId=" + "\"" + Constants.KEY_ID +
                "\", " + "algorithm=" + "\"" + Constants.ALGORITHM_AUTHORIZATION_VALUE + "\", " + "headers=" +
                "\"(request-target) host date digest\", " + "signature=" + "\"" + signature + "\"";

        System.out.println("AUTHORIZATION_HEADER:\n" + authorization);
        System.out.println("----------------------------------");

        return authorization;
    }

    /**
     This method get the current date well formatted
     @return The current date formatted as per Documentation
     */
    public String getDateFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 0", Locale.ENGLISH);
        return formatter.format(new Date());
        //return "Mon, 18 Mar 2019 15:10:24 +0000";
    }

    /**
     This method compose the Request Target string value
     @param method The current verb of the call to Satispay server
     @return The Request Target string value formatted as per Documentation
     */
    public String getRequestTarget(String method) {
        return Constants.REQUEST_TARGET_FIELD_NAME + " " + method + " " + Constants.REQUEST_TARGET_VALUE;
    }
}
