package it.satispay.signatureimpl.command;

import com.google.gson.Gson;
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

    Service serviceInstance = new Service();
    Gson gson = new Gson();

    public void callingSatispayURL(String method) {

        try {

            URL url = new URL(Constants.SATISPAY_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            //SET HEADER FIELDS
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("host", Constants.HOST);
            conn.setRequestProperty("date", serviceInstance.getDateFormatted());
            conn.setRequestProperty("digest", serviceInstance.createTheDigest(""));
            conn.setRequestProperty("Authorization", serviceInstance.composeTheAuthorizationHeader(serviceInstance.createTheSignature(serviceInstance.createTheString("GET","",false), serviceInstance.readAndGetPrivateKeyFromFile())));

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println("RESPONSE FROM SATISPAY:\n" + output);
                System.out.println("----------------------------------");
                System.out.println("----------------------------------");
            }
            conn.disconnect();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Something went wrong in method callingSatispayURL() inside Class Command" + e);
        }
    }
}
