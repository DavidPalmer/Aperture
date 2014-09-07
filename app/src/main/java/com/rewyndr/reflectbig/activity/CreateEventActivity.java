package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.service.ServiceFactory;
import com.rewyndr.reflectbig.util.DateUtils;
import com.rewyndr.reflectbig.util.Utils;

import java.util.Calendar;


public class CreateEventActivity extends Activity {
    private String CLASS_NAME = this.getClass().getName();
    private int startDay;
    private int startMonth;
    private int startYear;
    private int startHour;
    private int startMinute;

    private int endDay;
    private int endMonth;
    private int endYear;
    private int endHour;
    private int endMinute;
    
    private Button startChangeDate;
    private Button endChangeDate;
    private Button startChangeTime;
    private Button endChangeTime;
    private String eventType;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    static final int START_DATE_PICKER_ID = 1111;
    static final int END_DATE_PICKER_ID = 2222;
    static final int START_TIME_PICKER_ID = 3333;
    static final int END_TIME_PICKER_ID = 4444;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        startChangeDate = (Button) findViewById(R.id.btnStartChangeDate);
        endChangeDate = (Button) findViewById(R.id.btnEndChangeDate);
        startChangeTime = (Button) findViewById(R.id.btnStartChangeTime);
        endChangeTime = (Button) findViewById(R.id.btnEndChangeTime);
        setPageAccordingToEventType();

        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        startChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(START_DATE_PICKER_ID);
            }
        });

        endChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(END_DATE_PICKER_ID);
            }
        });

        startChangeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(START_TIME_PICKER_ID);
            }
        });

        endChangeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(END_TIME_PICKER_ID);
            }
        });
    }

    public void onCheckedChanged(View view) {
        setPageAccordingToEventType();
    }

    public void onClickCreate(View view) {
        Event newEvent = new Event();
        String eventName = ((EditText) findViewById(R.id.createEvent_text_event_name)).getText().toString();
        String eventDescription = ((EditText) findViewById(R.id.createEvent_text_event_description)).getText().toString();
        String location = ((EditText) findViewById(R.id.createEvent_text_where)).getText().toString();
        String shortLocation = Utils.getShortLocation(location);

        if (eventType.contains("Single")) {
            endYear = startYear;
            endMonth = startMonth;
            endDay = startDay;
        }

        newEvent.setEventName(eventName);
        newEvent.setEventDesc(eventDescription);
        newEvent.setStartDate(DateUtils.convertToDate(startDay, startMonth, startYear, startHour, startMinute));
        newEvent.setEndDate(DateUtils.convertToDate(endDay, endMonth, endYear, endHour, endMinute));
        newEvent.setLocation(location);
        newEvent.setLatitude(0);
        newEvent.setLongitude(0);
        newEvent.setShortLocation(shortLocation);

        String status = "";
        EventService service = ServiceFactory.getEventServiceInstance(this);
        try {
            service.createEvent(newEvent);
            status = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            status = "Failure";
        }
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
    }

    public void onClickCancel(View view) {
        setPageAccordingToEventType();
    }

    private void setPageAccordingToEventType() {
        RadioGroup eventTypeOption = (RadioGroup) findViewById(R.id.rad_event_type);
        int selected = eventTypeOption.getCheckedRadioButtonId();
        this.eventType = ((RadioButton) findViewById(selected)).getText().toString();
        Log.d(CLASS_NAME, this.eventType);

        if (this.eventType.contains("Single")) {
            Button edt = (Button) findViewById(R.id.btnEndChangeDate);
            edt.setEnabled(false);
        } else {
            Button edt = (Button) findViewById(R.id.btnEndChangeDate);
            edt.setEnabled(true);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_PICKER_ID:
                return new DatePickerDialog(this, startPickerListener, year, month, day);
            case END_DATE_PICKER_ID:
                return new DatePickerDialog(this, endPickerListener, year, month, day);
            case START_TIME_PICKER_ID:
                return new TimePickerDialog(this, startTimePickerListener, hour, minute, false);
            case END_TIME_PICKER_ID:
                return new TimePickerDialog(this, endTimePickerListener, hour, minute, false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener startTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    // These variables are for setting the time picker values
                    hour = selectedHour;
                    minute = selectedMinute;

                    startHour = selectedHour;
                    startMinute = selectedMinute;

                    String delimiter = ":";
                    startChangeTime.setText(Utils.appendStrings(delimiter, String.valueOf(hour), String.valueOf(minute)));

                }
            };

    private TimePickerDialog.OnTimeSetListener endTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    // These variables are for setting the time picker values
                    hour = selectedHour;
                    minute = selectedMinute;

                    endHour = selectedHour;
                    endMinute = selectedMinute;

                    String delimiter = ":";
                    endChangeTime.setText(Utils.appendStrings(delimiter, String.valueOf(hour), String.valueOf(minute)));
                }
            };

    private DatePickerDialog.OnDateSetListener startPickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            startYear = selectedYear;
            startMonth = selectedMonth;
            startDay = selectedDay;

            String delimiter = "-";
            startChangeDate.setText(Utils.appendStrings(delimiter, String.valueOf(year), String.valueOf(month), String.valueOf(day)));
        }
    };

    private DatePickerDialog.OnDateSetListener endPickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            endYear = selectedYear;
            endMonth = selectedMonth;
            endDay = selectedDay;

            String delimiter = "-";
            endChangeDate.setText(Utils.appendStrings(delimiter, String.valueOf(year), String.valueOf(month), String.valueOf(day)));
        }
    };

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
}
