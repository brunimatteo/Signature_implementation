package it.satispay.signatureimpl.main;

import com.google.gson.Gson;
import it.satispay.signatureimpl.command.Command;
import it.satispay.signatureimpl.pojo.Body;
import it.satispay.signatureimpl.service.Service;

public class Main {

    public static void main(String[] args) {

        Command command = new Command();
        Service service = new Service();
        service.getDateFormatted();
        service.getRequestTarget("GET");

        Body body = new Body();
        body.setFlow("MATCH_CODE");
        body.setAmount_unit((long) 100);
        body.setCurrency("EUR");

        command.callingSatispayURL(body);

    }
}
