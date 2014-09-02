package com.rewyndr.reflectbig.model;

import java.util.Date;

/**
 * Created by Satish on 9/1/2014.
 */
public class Event {

    private String eventName;

    private String location;

    private Date startDate;

    private Date endDate;

    private EventStatus status;

    private AttendeeStatus myStatus;

    private int attendeesCount;

    private int photosCount;

    private String createdBy;

    private String invitedBy;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public AttendeeStatus getMyStatus() {
        return myStatus;
    }

    public void setMyStatus(AttendeeStatus myStatus) {
        this.myStatus = myStatus;
    }

    public int getAttendeesCount() {
        return attendeesCount;
    }

    public void setAttendeesCount(int attendeesCount) {
        this.attendeesCount = attendeesCount;
    }

    public int getPhotosCount() {
        return photosCount;
    }

    public void setPhotosCount(int photosCount) {
        this.photosCount = photosCount;
    }
}
