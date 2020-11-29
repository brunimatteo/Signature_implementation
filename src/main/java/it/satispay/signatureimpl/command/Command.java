package it.satispay.signatureimpl.command;

import com.google.gson.Gson;
import it.satispay.signatureimpl.pojo.Body;
import it.satispay.signatureimpl.service.Service;
import it.satispay.signatureimpl.utilities.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Command {

    Service serviceInstance = new Service();
    Gson gson = new Gson();

    public String callingSatispayURL(Body body) {

        try {

            URL url = new URL(Constants.SATISPAY_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //SET HEADER FIELDS
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Host", Constants.HOST);
            conn.setRequestProperty("Date", serviceInstance.getDateFormatted());
            conn.setRequestProperty("Digest", serviceInstance.createTheDigest(gson.toJson(body).intern()));
            //conn.setRequestProperty("Authorization","");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }

        return null;
    }

    public String preparingAuthentication(){
        return null;
    }


}
