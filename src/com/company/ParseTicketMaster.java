package com.company;
import org.json.JSONObject;
import org.json.JSONArray;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ParseTicketMaster {

    private static HttpURLConnection connection;

    public static void main(String[] args) {

        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();

        //Method 1 java.net.HttpURLConnection
        try {
            //URL url = new URL("https://app.ticketmaster.com/discovery/v2/events?apikey=Hs3XjORs8LvOMQGeGmGL1m5aC934G5KS&locale=*&page=2&city=Toronto&countryCode=CA&stateCode=ON");
            URL url = new URL("https://app.ticketmaster.com/discovery/v2/events?apikey=Hs3XjORs8LvOMQGeGmGL1m5aC934G5KS&locale=*&size=2&page=1&city=Toronto&countryCode=CA&stateCode=ON");
            connection = (HttpURLConnection) url.openConnection();

            //Request Setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(500);
            connection.setReadTimeout(500);

            int status = connection.getResponseCode();
            //System.out.println(status);

            if(status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }

            //System.out.println(responseContent.toString());
            parse(responseContent.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect(); // need to close the connection
        }

    }

    public static String parse(String resonseBody) {
        JSONObject obj = new JSONObject(resonseBody);
        JSONObject events = obj.getJSONObject("_embedded");
        //System.out.println(events);
        JSONArray arr = events.getJSONArray("events");
        System.out.println(arr);

        for (int i = 0; i < arr.length(); ++i) {
            JSONObject rec = arr.getJSONObject(i);

            //GET LOCAL_DATE AND LOCAL_TIME
            JSONObject date = rec.getJSONObject("dates");
            JSONObject start = date.getJSONObject("start");
//            String localDate = start.getString("localDate");
//            String localTime = start.getString("localTime");
//            String name = rec.getString("name");

            //GET DESCRIPTION
            String description = rec.getString("pleaseNote");
//            System.out.println("DESCRIPTION FOR: " + i + ": " + description);

            /* GET:
             * city name
             * province name
             * country name
             * address (line1)
             * venue name
             * */
            JSONObject embedded = rec.getJSONObject("_embedded");
            JSONArray venues = embedded.getJSONArray("venues");
            for(int j = 0; j < venues.length(); j++) {
                JSONObject venueObjects = venues.getJSONObject(j);
                JSONObject address = venueObjects.getJSONObject("address");
                JSONObject city = venueObjects.getJSONObject("city");
                JSONObject province = venueObjects.getJSONObject("state");
                JSONObject country = venueObjects.getJSONObject("country");

                String line1 = address.getString("line1");
                String cityString = city.getString("name");
                String provinceString = province.getString("name");
                String countryString = country.getString("name");
                String postalCode = venueObjects.getString("postalCode"); //its own object
                String venueName = venueObjects.getString("name");
//                System.out.println("VENUE NAME: " + venueName);
//                System.out.println(i + "---" + cityString);
//                System.out.println(i + "---" + provinceString);
//                System.out.println(i + "---" + countryString);
//                System.out.println(i + "---" + line1);
//                System.out.println(postalCode);
            }

            //GET GENRE
            JSONArray classifications = rec.getJSONArray("classifications");
            for(int k = 0; k < classifications.length(); k++) {
                JSONObject for_genre = classifications.getJSONObject(k);
                JSONObject genre_object = for_genre.getJSONObject("genre");
                String genre_name = genre_object.getString("name");
                //System.out.println(genre_name);

            }
        }
        return null;
    }

}