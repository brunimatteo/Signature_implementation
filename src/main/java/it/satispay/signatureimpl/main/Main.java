package it.satispay.signatureimpl.main;

import it.satispay.signatureimpl.command.Command;
import it.satispay.signatureimpl.service.Service;

public class Main {

    public static void main(String[] args) {

        Command command = new Command();

        System.out.println("CALLING URL SATISPAY WITH (GET) METHOD");
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");
        System.out.println("BODY:   NONE");
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
