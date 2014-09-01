package com.rewyndr.reflectbig.model;

import java.util.List;

/**
 * Created by Raja on 8/30/2014.
 */
public class Event {
    private String event_id;
    private String event_name;
    private String event_invited_by;
    private String event_when;
    private String event_where;
    private boolean event_status;
    private Boolean event_myStatus;

    public Event() {
    }

    public Event(String event_id, String event_name, String event_invited_by, String event_when, String event_where, boolean event_status, Boolean event_myStatus, List<String> event_attendees) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_invited_by = event_invited_by;
        this.event_when = event_when;
        this.event_where = event_where;
        this.event_status = event_status;
        this.event_myStatus = event_myStatus;
        this.event_attendees = event_attendees;
    }

    private List<String> event_attendees;

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_invited_by() {
        return event_invited_by;
    }

    public void setEvent_invited_by(String event_invited_by) {
        this.event_invited_by = event_invited_by;
    }

    public String getEvent_when() {
        return event_when;
    }

    public void setEvent_when(String event_when) {
        this.event_when = event_when;
    }

    public String getEvent_where() {
        return event_where;
    }

    public void setEvent_where(String event_where) {
        this.event_where = event_where;
    }

    public boolean isEvent_status() {
        return event_status;
    }

    public void setEvent_status(boolean event_status) {
        this.event_status = event_status;
    }

    public List<String> getEvent_attendees() {
        return event_attendees;
    }

    public void setEvent_attendees(List<String> event_attendees) {
        this.event_attendees = event_attendees;
    }

    public Boolean isEvent_myStatus() {
        return event_myStatus;
    }

    public void setEvent_myStatus(boolean event_myStatus) {
        this.event_myStatus = event_myStatus;
    }
}
