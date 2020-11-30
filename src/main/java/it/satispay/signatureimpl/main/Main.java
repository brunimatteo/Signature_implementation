package it.satispay.signatureimpl.main;

import it.satispay.signatureimpl.command.Command;
import it.satispay.signatureimpl.service.Service;

public class Main {

    public static void main(String[] args) {

        Command command = new Command();
        Service service = new Service();

        String myStringToSign = service.createTheString("get","");
        String publicKeyString = service.readAndGetPublicKeyFromFile();
        String privateKeyString = service.readAndGetPrivateKeyFromFile();
        String signature = service.createTheSignature(myStringToSign, privateKeyString);
        boolean match = service.verifyTheSignature(myStringToSign, signature, publicKeyString);

        System.out.println("CALLING URL SATISPAY WITH (GET) METHOD");
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");
        System.out.println("PUBLIC_KEY:   " +  publicKeyString);
        System.out.println("----------------------------------");
        System.out.println("PRIVATE_KEY:   " +  privateKeyString);
        System.out.println("----------------------------------");
        System.out.println("BODY:   NONE");
        System.out.println("----------------------------------");
        System.out.println("STRING_TO_SIGN:\n" +  myStringToSign);
        System.out.println("----------------------------------");
        System.out.println("SIGNATURE:   " +  signature);
        System.out.println("----------------------------------");
        System.out.println("MATCH:   " +  match);
        System.out.println("----------------------------------");
        System.out.println("AUTHORIZATION_HEADER:\n" + service.composeTheAuthorizationHeader(service.createTheSignature(service.createTheString("GET",""), service.readAndGetPrivateKeyFromFile())));
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");

        //START CHIAMATA A URL SATISPAY
        command.callingSatispayURL("GET");

        //Body body = new Body();
        //body.setFlow("MATCH_CODE");
        //body.setAmount_unit((long) 100);
        //body.setCurrency("EUR");

        //command.callingSatispayURL(body,"GET");

    }
}
