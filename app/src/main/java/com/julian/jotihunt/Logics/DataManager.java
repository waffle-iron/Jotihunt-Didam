package com.julian.jotihunt.Logics;

public class DataManager {

    private static String name;
    private static String api_url;
    private static String api_key;
    private static Boolean error = false;
    private static String invitecode;
    private static String latitude;
    private static String longitude;
    private static double latitudedouble;
    private static double longitudedouble;

    public static String getApi_key() {
        return api_key;
    }

    public static void setApi_key(String api_key) {
        DataManager.api_key = api_key;
    }

    public static double getLatitudedouble() {
        return latitudedouble;
    }

    public static void setLatitudedouble(double latitudedouble) {
        DataManager.latitudedouble = latitudedouble;
    }

    public static double getLongitudedouble() {
        return longitudedouble;
    }

    public static void setLongitudedouble(double longitudedouble) {
        DataManager.longitudedouble = longitudedouble;
    }

    public static String getLatitude() {
        return latitude;
    }

    public static void setLatitude(String latitude) {
        DataManager.latitude = latitude;
    }

    public static String getLongitude() {
        return longitude;
    }

    public static void setLongitude(String longitude) {
        DataManager.longitude = longitude;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        DataManager.name = name;
    }


    public static String getApi_url() {
        return api_url;
    }

    public static void setApi_url(String api_url) {
        DataManager.api_url = api_url;
    }



    public static String getInvitecode() {
        return invitecode;
    }

    public static void setInvitecode(String invitecode) {
        DataManager.invitecode = invitecode;
    }

    public static Boolean getError() {
        return error;
    }

    public static void setError(Boolean error) {
        DataManager.error = error;
    }


}
