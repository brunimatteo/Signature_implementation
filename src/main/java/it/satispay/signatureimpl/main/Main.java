package it.satispay.signatureimpl.main;

import com.google.gson.Gson;
import it.satispay.signatureimpl.command.Command;
import it.satispay.signatureimpl.entity.Body;
import it.satispay.signatureimpl.utilities.Constants;

public class Main {

    public static void main(String[] args) {

        Gson gson = new Gson();
        Command command = new Command();

        /*------------------START GET CALL--------------------*/

        System.out.println("               GET                ");
        System.out.println("----------------------------------");
        System.out.println("CALLING URL SATISPAY WITH (GET) METHOD");
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");
        System.out.println("BODY:   NONE");
        System.out.println("----------------------------------");
        //CALL TO SATISPAY API WITH GET METHOD
        command.callingSatispayURL(Constants.SATISPAY_URL, "", "GET");

        /*------------------END GET CALL----------------------*/
        /*------------------START POST CALL-------------------*/

        System.out.println("                                  ");
        System.out.println("                                  ");
        System.out.println("                                  ");
        System.out.println("                                  ");
        System.out.println("              POST                ");
        System.out.println("----------------------------------");
        System.out.println("CALLING URL SATISPAY WITH (POST) METHOD");
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");
        //DEFINING BODY OBJECT
        Body bodyPost = new Body("MATCH_CODE",(long) 100,"EUR");
        String bodyPostString = gson.toJson(bodyPost);
        System.out.println("BODY:   " + bodyPostString);
        System.out.println("----------------------------------");
        //CALL TO SATISPAY API WITH POST METHOD
        command.callingSatispayURL(Constants.SATISPAY_URL, bodyPostString,"POST");

        /*------------------END POST CALL---------------------*/
        /*------------------START PUT CALL--------------------*/

        System.out.println("                                  ");
        System.out.println("                                  ");
        System.out.println("                                  ");
        System.out.println("                                  ");
        System.out.println("               PUT                ");
        System.out.println("----------------------------------");
        System.out.println("CALLING URL SATISPAY WITH (PUT) METHOD");
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");
        //DEFINING BODY OBJECT
        Body bodyPut = new Body("MATCH_CODE",(long) 200,"USD");
        String bodyPutString = gson.toJson(bodyPut);
        System.out.println("BODY:   " + bodyPutString);
        System.out.println("----------------------------------");
        //CALL TO SATISPAY API WITH PUT METHOD
        command.callingSatispayURL(Constants.SATISPAY_URL, bodyPutString,"PUT");

        /*------------------END PUT CALL--------------------*/
        /*------------------START DELETE CALL---------------*/

        System.out.println("                                  ");
        System.out.println("                                  ");
        System.out.println("                                  ");
        System.out.println("                                  ");
        System.out.println("              DELETE              ");
        System.out.println("----------------------------------");
        System.out.println("CALLING URL SATISPAY WITH (DELETE) METHOD");
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");
        System.out.println("BODY:   NONE");
        System.out.println("----------------------------------");
        //CALL TO SATISPAY API + A QUERY PARAM WITH DELETE METHOD
        command.callingSatispayURL(Constants.SATISPAY_URL+"?storeId={1234}", "","DELETE");

        /*------------------END DELETE CALL---------------*/
    }
}
