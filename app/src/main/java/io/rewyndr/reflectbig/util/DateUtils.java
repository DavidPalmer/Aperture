package io.rewyndr.reflectbig.util;

import io.rewyndr.reflectbig.model.EventStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Satish on 9/2/2014.
 */
public class DateUtils {

    public static EventStatus getEventType(Date start, Date end) {
        Date d = new Date();
        if (d.getTime() < start.getTime()) {
            return EventStatus.UPCOMING;
        } else if (d.getTime() <= end.getTime()) {
            return EventStatus.CURRENT;
        } else {
            return EventStatus.PAST;
        }
    }

    public static Date convertToDate(int day, int month, int year, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        return cal.getTime();
    }

    public static String getDateInFormat(Date date, String format) {
        DateFormat formatter  = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static boolean checkDateCorrectness(Date startDate, Date endDate) {
        Date today = new Date();
        if (startDate.getTime() - today.getTime() < 0 || endDate.getTime() - today.getTime() < 0) {
            return false;
        }
        return true;
    }
}
