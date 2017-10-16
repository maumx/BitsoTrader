package com.maumx.bitsotrader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.maumx.bitsotrader.JsonResponse.GeneralResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.HttpURLConnection;
import java.lang.reflect.Type;
import java.util.IllegalFormatCodePointException;
import java.util.StringTokenizer;

public class BitsoCreator {

    private  String BitsoSecret;
    private String BitsoKey;


    public BitsoCreator (String bitsoKey, String bitsoSecret)
    {
     BitsoKey=bitsoKey;

        BitsoSecret=bitsoSecret;



    }

    private String GenerarAutorizacion(String httpMetodo, String requestPath) throws  Exception
    {
        return GenerarAutorizacion( httpMetodo,  requestPath,"");

    }


    private String GenerarAutorizacion(String httpMetodo, String requestPath, String JSONPayload) throws  Exception
    {
        long nonce = System.currentTimeMillis()+636299727214875764L;

        while (  nonce<636299727214875764L  )
        {
            nonce=System.currentTimeMillis()+636256395171385017L;
        }

        String message = nonce + httpMetodo + requestPath + JSONPayload;
        String signature = "";
        byte[] secretBytes = BitsoSecret.getBytes();
        SecretKeySpec localMac = new SecretKeySpec(secretBytes, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(localMac);
        byte[] arrayOfByte = mac.doFinal(message.getBytes());
        BigInteger localBigInteger = new BigInteger(1, arrayOfByte);
        signature = String.format("%0" + (arrayOfByte.length << 1) + "x", new Object[] { localBigInteger });
        String authHeader = String.format("Bitso %s:%s:%s", BitsoKey, nonce, signature);

        return authHeader;
    }

    public  GeneralResponse ObtenerFee() throws  Exception
    {

        GeneralResponse repuesta= new GeneralResponse();

        String authHeader=GenerarAutorizacion("GET","/v3/fees/");
        String url = "https://api.bitso.com/v3/fees/";


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Bitso Java Example");
        con.setRequestProperty("Authorization", authHeader);


        // Send request
        int responseCode = con.getResponseCode();

        repuesta.setmHttpStatusCode(responseCode);
        StringBuffer response = new StringBuffer();
        if (   responseCode == 200)
        {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


        }else
            {

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;


                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

            }

        repuesta.setmJson(response.toString());



     /*
*/

        return repuesta;


    }

    public  String ObtenerBalance() throws  Exception
    {
        String authHeader=GenerarAutorizacion("GET","/v3/balance/");
        String url = "https://api.bitso.com/v3/balance/";

        StringBuilder response = new StringBuilder();

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Bitso Java Example");
        con.setRequestProperty("Authorization", authHeader);


        // Send request
        int responseCode = con.getResponseCode();



        if (  responseCode== 200 ) {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }else
        {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        }


        Gson gson = new Gson();


     /*
*/

        return response.toString();


    }


    public  String ObtenerOpenOrders(String tipoLibro) throws  Exception
    {
        String authHeader=GenerarAutorizacion("GET","/v3/open_orders?book="+tipoLibro);
        String url = "https://api.bitso.com/v3/open_orders?book="+tipoLibro ;


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Bitso Java Example");
        con.setRequestProperty("Authorization", authHeader);


        StringBuilder response =  new StringBuilder();

        // Send request
        int responseCode = con.getResponseCode();

        if (  responseCode== 200 ) {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }else
        {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        }

        Gson gson = new Gson();


     /*
*/

        return response.toString();


    }

    public  String ObtenerLibroOrdenes(String tipoLibro) throws  Exception
    {

        String url = "https://api.bitso.com/v3/order_book/?aggregate=false&book="+tipoLibro;


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Bitso Java Example");



        // Send request
        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


     /*
*/

        return response.toString();


    }


    public  String CancelarOrden(String oid) throws  Exception
    {

        String url = "https://api.bitso.com/v3/orders/"+oid +"/";
        String authHeader= GenerarAutorizacion("DELETE","/v3/orders/"+oid +"/");

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("DELETE");
        con.setRequestProperty("User-Agent", "Bitso Java Example");
        con.setRequestProperty("Authorization", authHeader);


        // Send request
        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


     /*
*/

        return response.toString();


    }



    public GeneralResponse PlaceOrder(String book, String side, String type, String major, String minor, String price) throws  Exception
    {

        GeneralResponse respuesta = new GeneralResponse();

        String url = "https://api.bitso.com/v3/orders/";

        JSONObject nuevo= new JSONObject();


        nuevo.put("book",book);
        nuevo.put("side",side);
        nuevo.put("type",type);

        if (  !major.isEmpty()  )
        nuevo.put("major",major );
        if (  !minor.isEmpty()  )
        nuevo.put("minor",minor  );

        nuevo.put("price",price);



        String json = nuevo.toString();
        String authHeader= GenerarAutorizacion("POST","/v3/orders/",json);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Bitso Java Example");
        con.setRequestProperty("Authorization", authHeader);
        con.setRequestProperty("Content-Type","application/json");
        con.setDoOutput(true);


        OutputStream op =con.getOutputStream();
       op. write(json.getBytes("UTF-8"));
        op.close();



        StringBuffer response = new StringBuffer();

        // Send request
        int responseCode = con.getResponseCode();
        respuesta.setmHttpStatusCode(responseCode);
        if (  responseCode== 200 ) {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }else
            {

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;


                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

            }

            respuesta.setmJson(response.toString());

     /*
*/


        return respuesta;


    }



    public  GeneralResponse ObtenerUserTrades(String tipoLibro,String marker) throws  Exception
    {


        StringBuilder uri = new StringBuilder();
        uri.append("/v3/user_trades/?");

        GeneralResponse respuesta=  new GeneralResponse();


        if (  !tipoLibro.isEmpty()  )
        {


            uri.append("book=");
            uri.append(tipoLibro);
            uri.append("&limit=100");

        }

        if (  !marker.isEmpty()  )
        {
            uri.append("&marker=");
            uri.append(marker);

        }


        String authHeader=GenerarAutorizacion("GET",    uri.toString());
        String url = "https://api.bitso.com"+ uri.toString();



        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Bitso Java Example");
        con.setRequestProperty("Authorization",authHeader );



        int responseCode = con.getResponseCode();
        StringBuilder response= new StringBuilder();
        respuesta.setmHttpStatusCode(responseCode);
        if (  responseCode== 200 ) {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }else
        {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        }

        respuesta.setmJson(response.toString());

     /*
*/

        return respuesta;


    }


    public  GeneralResponse ObtenerRetiros() throws  Exception
    {


        StringBuilder uri = new StringBuilder();
        uri.append("/v3/withdrawals/");

        GeneralResponse respuesta=  new GeneralResponse();



        String authHeader=GenerarAutorizacion("GET",    uri.toString());
        String url = "https://api.bitso.com"+ uri.toString();



        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Bitso Java Example");
        con.setRequestProperty("Authorization",authHeader );



        int responseCode = con.getResponseCode();
        StringBuilder response= new StringBuilder();
        respuesta.setmHttpStatusCode(responseCode);
        if (  responseCode== 200 ) {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }else
        {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        }

        respuesta.setmJson(response.toString());

     /*
*/

        return respuesta;


    }

    public  GeneralResponse ObtenerFondeos() throws  Exception
    {


        StringBuilder uri = new StringBuilder();
        uri.append("/v3/fundings/");

        GeneralResponse respuesta=  new GeneralResponse();



        String authHeader=GenerarAutorizacion("GET",    uri.toString());
        String url = "https://api.bitso.com"+ uri.toString();



        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Bitso Java Example");
        con.setRequestProperty("Authorization",authHeader );



        int responseCode = con.getResponseCode();
        StringBuilder response= new StringBuilder();
        respuesta.setmHttpStatusCode(responseCode);
        if (  responseCode== 200 ) {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }else
        {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        }

        respuesta.setmJson(response.toString());

     /*
*/

        return respuesta;


    }


    public  GeneralResponse ObtenerConsultarOrden(String oid) throws  Exception
    {


        GeneralResponse respuesta=  new GeneralResponse();

        String authHeader=GenerarAutorizacion("GET","/v3/orders/"+oid+"/");
        String url = "https://api.bitso.com/v3/orders/"+oid+"/";



        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Bitso Java Example");
        con.setRequestProperty("Authorization",authHeader );



        int responseCode = con.getResponseCode();
        StringBuilder response= new StringBuilder();
        respuesta.setmHttpStatusCode(responseCode);
        if (  responseCode== 200 ) {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }else
        {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        }

        respuesta.setmJson(response.toString());

     /*
*/

        return respuesta;


    }

    /*public static void main(String[] args) throws Exception {
        String bitsoKey = "BITSO API KEY";
        String bitsoSecret = "BITSO API SECRET";
        long nonce = System.currentTimeMillis();
        String HTTPMethod = "GET";
        String RequestPath = "/v3/balance/";
        String JSONPayload = "";

        // Create the signature
        String message = nonce + HTTPMethod + RequestPath + JSONPayload;
        String signature = "";
        byte[] secretBytes = bitsoSecret.getBytes();
        SecretKeySpec localMac = new SecretKeySpec(secretBytes, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(localMac);
        byte[] arrayOfByte = mac.doFinal(message.getBytes());
        BigInteger localBigInteger = new BigInteger(1, arrayOfByte);
        signature = String.format("%0" + (arrayOfByte.length << 1) + "x", new Object[] { localBigInteger });

        String authHeader = String.format("Bitso %s:%s:%s", bitsoKey, nonce, signature);
        String url = "https://api.bitso.com/v3/balance/";


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Bitso Java Example");
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", authHeader);

        // Send request
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
    }*/
}
