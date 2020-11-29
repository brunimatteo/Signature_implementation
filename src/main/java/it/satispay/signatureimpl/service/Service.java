package it.satispay.signatureimpl.service;

import it.satispay.signatureimpl.utilities.Constants;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

public class Service {

    public String createTheDigest(String stringToHash){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(Constants.ALGORITHM);
            byte[] hashBytes = digest.digest(stringToHash.getBytes(StandardCharsets.UTF_8));
            String finalHashString = Base64.getEncoder().encodeToString(hashBytes);
            System.out.println(Constants.ALGORITHM + "=" + finalHashString);
            return Constants.ALGORITHM + "=" + finalHashString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createTheString(){

        //STRING="(request-target): post /wally-services/protocol/tests/signature\nhost: staging.authservices.satispay.com\ndate: Mon, 18 Mar 2019 15:10:24 +0000\ndigest: SHA-256=$DIGEST"



        return null;
    }

    //This method Sign with RSA (rsa-sha256) algorithm a String with a private key, using Base64 as output.
    public String createTheSignature(String stringToSign, String privateKey){
        try {
        // Remove markers and new line characters in private key
        String realPK = privateKey.replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("\n", "");

        byte[] b1 = Base64.getMimeDecoder().decode(realPK);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
        KeyFactory kf = null;

        kf = KeyFactory.getInstance("RSA");

        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(kf.generatePrivate(spec));
        privateSignature.update(stringToSign.getBytes("UTF-8"));
        byte[] s = privateSignature.sign();

        return Base64.getEncoder().encodeToString(s);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String ComposeTheAuthorizationHeader(){
        return null;
    }

    public String getDateFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ssZ", Locale.ENGLISH);
        //System.out.println(formatter.format(new Date()));
        return formatter.format(new Date());
    }

    public String getRequestTarget(String verb) {
        System.out.println(Constants.REQUEST_TARGET_FIELD_NAME + " " + verb + " " + Constants.REQUEST_TARGET_VALUE);
        return Constants.REQUEST_TARGET_FIELD_NAME + " " + verb + " " + Constants.REQUEST_TARGET_VALUE;
    }


}
