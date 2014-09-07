package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.rewyndr.reflectbig.R;

import java.util.Calendar;


public class CreateEventActivity extends Activity {

    private Button startChangeDate;
    private Button endChangeDate;
    private Button startChangeTime;
    private Button endChangeTime;

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
                    hour = selectedHour;
                    minute = selectedMinute;

                    // set current time into textview
                    startChangeTime.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)));

                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private TimePickerDialog.OnTimeSetListener endTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;

                    // set current time into textview
                    endChangeTime.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)));

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

            // Show selected date
            startChangeDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

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

            // Show selected date
            endChangeDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

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
