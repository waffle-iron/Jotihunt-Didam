package com.julian.jotihunt.Logics;


public class MessageParser {

    public static final int GPS_STATUS_NO_SIGNAL = 0;
    public static final int GPS_STATUS_SIGNAL = 1;
    public double latitudeDouble;
    public double longitudeDouble;

    private String[] messageList;

    public MessageParser(String message) {
        this.messageList = message.split("\n");
    }

    public String parseLatitude() {
        if ( getStatus() == GPS_STATUS_SIGNAL ) {
            return messageList[1].substring(4);
        } else if ( getStatus() == GPS_STATUS_NO_SIGNAL ) {
            return messageList[2].substring(4);
        } else {
            return null;
        }
    }

    public String parseLongitude() {
        if ( getStatus() == GPS_STATUS_SIGNAL ) {
            return messageList[2].substring(4);
        } else if ( getStatus() == GPS_STATUS_NO_SIGNAL ) {
            return messageList[3].substring(4);
        } else {
            return null;
        }
    }

    public Double parseLatitudeDouble() {
        try {
            latitudeDouble = Double.parseDouble(parseLatitude()); // Make use of autoboxing.  It's also easier to read.
        } catch (NumberFormatException e) {
            // p did not contain a valid double
        }
        return latitudeDouble;
    }

    public Double parseLongitudeDouble() {
        try {
            longitudeDouble = Double.parseDouble(parseLongitude()); // Make use of autoboxing.  It's also easier to read.
        } catch (NumberFormatException e) {
            // p did not contain a valid double
        }
        return longitudeDouble;
    }

    public String parseSpeed() {
        if ( getStatus() == GPS_STATUS_SIGNAL ) {
            return messageList[3].substring(6);
        } else {
            return null;
        }
    }

    public String parseCurrentTime() {
        if ( getStatus() == GPS_STATUS_SIGNAL ) {
            return messageList[4].substring(2);
        } else if ( getStatus() == GPS_STATUS_NO_SIGNAL ) {
            return messageList[8].substring(2);
        } else {
            return null;
        }
    }

    public String parseBatteryLife() {
        if ( getStatus() == GPS_STATUS_SIGNAL ) {
            return messageList[5].substring(4);
        } else if ( getStatus() == GPS_STATUS_NO_SIGNAL ) {
            return messageList[9].substring(4);
        }
            return null;
    }

    // Haal online of ofline status op
    private int getStatus() {
        if (messageList[1].equals("Last:")) {
            return GPS_STATUS_NO_SIGNAL;
        } else {
            return GPS_STATUS_SIGNAL;
        }
    }

}

/*
lat:float
long:float
speed:float
T:date time
http://maps.google.com/



Last:
lat:double
lon:double
T:time
http://maps.google.com/
Now:
Lac:c62 c09d
T:date time
bat:50%
 */