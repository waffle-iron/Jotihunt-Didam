package com.julian.jotihunt.Logics;

/**
 * Created by Julian on 9-3-2016.
 */
public class DataManager {

    private static String name;
    private static String mail;
    private static String password;
    private static String api_url;
    private static Boolean error = false;
    private static String invitecode;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        DataManager.name = name;
    }

    public static String getMail() {
        return mail;
    }

    public static void setMail(String mail) {
        DataManager.mail = mail;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DataManager.password = password;
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
