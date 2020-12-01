package it.satispay.signatureimpl.command;

import it.satispay.signatureimpl.service.Service;
import it.satispay.signatureimpl.utilities.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Command {

    private static final Logger LOGGER = Logger.getLogger(Command.class.getName());

    Service service = new Service();

    /**
     This method call the given Satispay URL
     @param urlSatispay The url to call
     @param body The body content, if any
     @param method The Http verb of the call ('GET', 'POST, 'PUT', 'DELETE')
     */
    public void callingSatispayURL(String urlSatispay, String body ,String method) {

        try {
            URL url = new URL(urlSatispay);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(method);
            //SET HEADER FIELDS
            if(!body.equals("")) conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("host", Constants.HOST);
            conn.setRequestProperty("date", service.getDateFormatted());
            conn.setRequestProperty("digest", service.createTheDigest(body));
            conn.setRequestProperty("Authorization", service.composeTheAuthorizationHeader(service.createTheSignature(urlSatispay, body, method, service.createTheString(urlSatispay, method, body, false), service.readAndGetPrivateKeyFromFile())));
            //SET BODY
            if(!body.equals("")){
                conn.setDoOutput(true);
                conn.getOutputStream().write(body.getBytes("UTF8"));
            }
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println("RESPONSE FROM SATISPAY API:\n" + output);
                System.out.println("----------------------------------");
                System.out.println("----------------------------------");
            }
            conn.disconnect();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Something went wrong in method callingSatispayURL() inside Class Command" + e);
        }
    }
}
