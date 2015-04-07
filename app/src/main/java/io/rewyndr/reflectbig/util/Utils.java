package io.rewyndr.reflectbig.util;

import io.rewyndr.reflectbig.common.Constants;
import io.rewyndr.reflectbig.model.AttendeeStatus;
import io.rewyndr.reflectbig.model.EventStatus;

/**
 * This package contains utility functions for the application
 * Created by Satish on 9/6/2014.
 */
public class Utils {
    private Utils() {

    }

    /**
     * This method gets the attendee status based on the status of the event and whether the user has accepted or rejected the event
     *
     * @param eventStatus the status of the event PAST, CURRENT or UPCOMING
     * @param status      the user status Y or N
     * @return
     */
    public static AttendeeStatus getAttendeeStatus(EventStatus eventStatus, String status) {
        if (status != null) {
            switch (eventStatus) {
                case CURRENT:
                case UPCOMING:
                    if (Constants.YES.equals(status)) {
                        return AttendeeStatus.ACCEPTED;
                    } else {
                        return AttendeeStatus.DECLINED;
                    }
                case PAST:
                    if (Constants.YES.equals(status)) {
                        return AttendeeStatus.ATTENDED;
                    } else {
                        return AttendeeStatus.MISSED;
                    }
            }
        }
        return AttendeeStatus.NOT_RESPONDED;
    }

    public static String appendStrings(String delimiter, String... str) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length; i++) {
            if (i > 0) {
                builder.append(delimiter);
            }
            builder.append(str[i]);
        }
        return builder.toString();
    }

    public static String getShortLocation(String location) {
        String[] address = location.split(",");
        return address[1] + address[2];
    }

    public static Float convertToDegree(String latLong, String latLongRef) {
        if (latLongRef.equals("N") || latLongRef.equals("E"))
            return convertToDegreeHelper(latLong);
        else
            return 0 - convertToDegreeHelper(latLong);
    }

    private static Float convertToDegreeHelper(String stringDMS) {
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;
    }

    public static double getLatLongDistance(double lat1, double long1, double lat2, double long2) {
        int R = 6371;
        double dLat = degreeToRadius(lat2 - lat1);
        double dLong = degreeToRadius(long2 - long1);
        double val = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(degreeToRadius(lat1)) * Math.cos(degreeToRadius(lat2)) *
                        Math.sin(dLat / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(val), Math.sqrt(1 - val));
        double value = R * c;
        return value;

    }

    private static double degreeToRadius(double degree) {
        return degree * (Math.PI / 180);
    }
}