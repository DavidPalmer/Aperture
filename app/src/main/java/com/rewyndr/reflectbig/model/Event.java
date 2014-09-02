package com.rewyndr.reflectbig.model;

import java.util.Date;

/**
 * Created by Satish on 9/1/2014.
 */
public class Event {

    private String eventId;

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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(String invitedBy) {
        this.invitedBy = invitedBy;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", location='" + location + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", myStatus=" + myStatus +
                ", attendeesCount=" + attendeesCount +
                ", photosCount=" + photosCount +
                ", createdBy='" + createdBy + '\'' +
                ", invitedBy='" + invitedBy + '\'' +
                '}';
    }
}
