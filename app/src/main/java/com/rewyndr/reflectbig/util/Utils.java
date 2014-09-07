package com.rewyndr.reflectbig.util;

import com.rewyndr.reflectbig.common.Constants;
import com.rewyndr.reflectbig.model.AttendeeStatus;
import com.rewyndr.reflectbig.model.EventStatus;

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
     * @param status the user status Y or N
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
}
