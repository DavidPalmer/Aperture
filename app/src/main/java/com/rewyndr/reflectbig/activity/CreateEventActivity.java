package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.common.Constants;
import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.service.ServiceFactory;
import com.rewyndr.reflectbig.util.DateUtils;
import com.rewyndr.reflectbig.util.Utils;

import java.util.Calendar;
import java.util.Date;


public class CreateEventActivity extends FragmentActivity {
    private String CLASS_NAME = this.getClass().getName();
    private static Button currentDateButton;
    private static Button currentTimeButton;
    private String eventType;
    private static Activity act;
    private boolean isStartDatePicked = false;
    private boolean isEndDatePicked = false;
    private boolean isStartTimePicked = false;
    private boolean isEndTimePicked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        setTitle("Create New Event");
        setContentView(R.layout.activity_create_event);
        setPageAccordingToEventType();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            currentDateButton.setText(Utils.appendStrings(Constants.DATE_DELIMITER, String.valueOf(month + 1), String.valueOf(day), String.valueOf(year)));
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            currentTimeButton.setText(Utils.appendStrings(Constants.TIME_DELIMITER, String.valueOf(hourOfDay), String.valueOf(minute)));
        }
    }

    public void showStartDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        currentDateButton = (Button) act.findViewById(R.id.btnStartChangeDate);
        isStartDatePicked = true;
    }

    public void showEndDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        currentDateButton = (Button) act.findViewById(R.id.btnEndChangeDate);
        isEndDatePicked = true;
    }

    public void showStartTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        currentTimeButton = (Button) act.findViewById(R.id.btnStartChangeTime);
        isStartTimePicked = true;
    }

    public void showEndTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        currentTimeButton = (Button) act.findViewById(R.id.btnEndChangeTime);
        isEndTimePicked  = true;
    }

    public void onCheckedChanged(View view) {
        setPageAccordingToEventType();
    }

    public void onClickCreate(View view) {
        Event newEvent = new Event();
        String eventName = ((EditText) findViewById(R.id.createEvent_text_event_name)).getText().toString();
        String eventDescription = ((EditText) findViewById(R.id.createEvent_text_event_description)).getText().toString();
        String location = ((EditText) findViewById(R.id.createEvent_text_where)).getText().toString();
        /*String shortLocation = Utils.getShortLocation(location);*/
        String shortLocation = location;
        String[] sDate = ((Button) findViewById(R.id.btnStartChangeDate)).getText().toString().split(Constants.DATE_DELIMITER);
        String[] eDate = ((Button) findViewById(R.id.btnEndChangeDate)).getText().toString().split(Constants.DATE_DELIMITER);
        String[] sTime = ((Button) findViewById(R.id.btnStartChangeTime)).getText().toString().split(Constants.TIME_DELIMITER);
        String[] eTime = ((Button) findViewById(R.id.btnEndChangeTime)).getText().toString().split(Constants.TIME_DELIMITER);
        if (Constants.EMPTY_STRING.equals(eventName.trim()) || Constants.EMPTY_STRING.equals(eventDescription.trim())
                || Constants.EMPTY_STRING.equals(location.trim()) || !(isStartDatePicked) || !(isStartTimePicked && isEndTimePicked)) {
            Toast.makeText(this, "Invalid data entered", Toast.LENGTH_SHORT).show();
        } else {
            Date startDate = DateUtils.convertToDate(Integer.valueOf(sDate[1]), Integer.valueOf(sDate[0]) - 1, Integer.valueOf(sDate[2]), Integer.valueOf(sTime[0]), Integer.valueOf(sTime[1]));
            Date endDate = null;
            if(isEndDatePicked)
                endDate = DateUtils.convertToDate(Integer.valueOf(eDate[1]), Integer.valueOf(eDate[0]) - 1, Integer.valueOf(eDate[2]), Integer.valueOf(eTime[0]), Integer.valueOf(eTime[1]));
            else
                endDate = DateUtils.convertToDate(Integer.valueOf(sDate[1]), Integer.valueOf(sDate[0]) - 1, Integer.valueOf(sDate[2]), Integer.valueOf(eTime[0]), Integer.valueOf(eTime[1]));
            if(endDate.getTime() - startDate.getTime() <= 0) {
                Toast.makeText(this, "Error in start and end date", Toast.LENGTH_SHORT).show();
            } else {
                newEvent.setEventName(eventName);
                newEvent.setEventDesc(eventDescription);
                newEvent.setStartDate(startDate);
                newEvent.setEndDate(endDate);
                newEvent.setLocation(location);
                newEvent.setLatitude(0);
                newEvent.setLongitude(0);
                newEvent.setShortLocation(shortLocation);
                new UseDBService().execute(newEvent);
            }
        }
    }

    public void onClickCancel(View view) {
        setPageAccordingToEventType();
    }

    private void setPageAccordingToEventType() {
        RadioGroup eventTypeOption = (RadioGroup) findViewById(R.id.rad_event_type);
        int selected = eventTypeOption.getCheckedRadioButtonId();
        this.eventType = ((RadioButton) findViewById(selected)).getText().toString();
        if (this.eventType.contains("Single")) {
            Button edt = (Button) findViewById(R.id.btnEndChangeDate);
            edt.setEnabled(false);
        } else {
            Button edt = (Button) findViewById(R.id.btnEndChangeDate);
            edt.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class UseDBService extends AsyncTask<Event, Void, String> {
        Event newEvent;

        public UseDBService() {
        }

        @Override
        protected String doInBackground(Event... events) {
            newEvent = events[0];
            EventService service = ServiceFactory.getEventServiceInstance(CreateEventActivity.this);
            String eventId = "";
            try {
                eventId = service.createEvent(events[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return eventId;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "";
            if(result.equals("")) {
                status = "Failure";
            } else {
                newEvent.setEventId(result);
                status = "Success";
                Intent intent = new Intent(act, InviteEventActivity.class);
                intent.putExtra("Error", Constants.CREATE_SCREEN_ERROR_MSG);
                intent.putExtra("event", newEvent);
                startActivity(intent);
            }
            Toast.makeText(act, status, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}