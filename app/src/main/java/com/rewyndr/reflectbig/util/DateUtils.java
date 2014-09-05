package com.rewyndr.reflectbig.util;

import com.rewyndr.reflectbig.model.EventStatus;

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
}
