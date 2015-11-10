package com.sayagodshala.livesplash.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by sayagodshala on 9/19/2015.
 */

public class Constants {

    public static final String BASE_URL = "http://ax.itunes.apple.com";
    public static final String BASE_PATH = "/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/sf=143441/limit=100/genre=6007/json";
    public static final String BASE_URL_GOOGLE = "https://maps.googleapis.com";
    public static final String GOOGLE_APP_API_SERVER_KEY = "AIzaSyAiOCq4qJqcqv9TsauOa8knOtwPXmHZeiI";
    public static final String GOOGLE_APP_API_BROWSER_KEY = "AIzaSyCm2EHuPFU5AvgapK-RLpRmCQTyXc2N9JA";
    public static final int GCM_NOTIFICATION_ID = 1111;

    public static final String WARNING_PASSWORD = "Password should be minimum 6 characters";
    public static final String WARNING_MOBILENO = "Mobile number should be of 10 digits";
    public static final String WARNING_EMAILID = "Invalid EmailId";
    public static final String WARNING_NAME = "Name Required";
    public static final String WARNING_USERNAME = "Email/Mobile Required";
    public static final String WARNING_PASSWORD1 = "Password Required";

    public static final String ERROR_MESSAGE = "Something went wrong";
    public static final String ERROR_MESSAGE1 = "Oops, Something went wrong";

    public static final String FEEDBACK_EMAIL = "feedback@getdingdong.me";

    // used for location search
    public static String GET_GOOGLE_PLACE(String input) {
        String url = "";
        try {
            url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?sensor=false&language=en&key=" + GOOGLE_APP_API_SERVER_KEY + "&input=" + URLEncoder.encode(input, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String GET_GOOGLE_PLACE_BY_LOCATION(String input, String location, int radius) {

        String url = "";
        try {
            url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?sensor=false&language=en&key=" + GOOGLE_APP_API_SERVER_KEY + "&input=" + URLEncoder.encode(input, "utf8") + "&location=" + location + "&radius=" + radius + "components=country:in";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String GET_LAT_LNG_FROM_PLACE(String input) {
        String url = "";
        url = "https://maps.googleapis.com/maps/api/geocode/json?key=" + GOOGLE_APP_API_SERVER_KEY + "&address=" + input;

        return url;
    }

    public static String GET_PLACES_FROM_LATLONG(String input) {
        String url = "";
        url = "https://maps.googleapis.com/maps/api/geocode/json?key=" + GOOGLE_APP_API_SERVER_KEY + "&latlng=" + input;

        return url;
    }


}
