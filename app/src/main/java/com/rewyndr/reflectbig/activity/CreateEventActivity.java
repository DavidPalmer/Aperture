package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.common.Constants;
import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.model.AddressLocation;
import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.service.ServiceFactory;
import com.rewyndr.reflectbig.util.DateUtils;
import com.rewyndr.reflectbig.util.Utils;

import java.util.Calendar;
import java.util.Date;


public class CreateEventActivity extends FragmentActivity {
    private static Button currentDateButton;
    private static Button currentTimeButton;
    private static Activity act;
    private String eventType;
    private static boolean isStartDatePicked = false;
    private static boolean isEndDatePicked = false;
    private static boolean isStartTimePicked = false;
    private static boolean isEndTimePicked = false;
    private AddressLocation location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        setTitle("Create New Event");
        setContentView(R.layout.activity_create_event);
        setPageAccordingToEventType();
    }

    public void showStartDatePickerDialog(View v) {
        DialogFragment newFragment = null;
        if(isStartDatePicked) {
            String[] dateString = ((Button) findViewById(R.id.btnStartChangeDate)).getText().toString().split(Constants.DATE_DELIMITER);
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.valueOf(dateString[2]) , Integer.valueOf(dateString[0]) - 1, Integer.valueOf(dateString[1]));
            Bundle b = bundleData(true, cal.getTime().getTime());
            newFragment = new DatePickerFragment();
            newFragment.setArguments(b);
        } else {
            Bundle b = bundleData(true, null);
            newFragment = new DatePickerFragment();
            newFragment.setArguments(b);
        }
        newFragment.show(getSupportFragmentManager(), "datePicker");
        currentDateButton = (Button) act.findViewById(R.id.btnStartChangeDate);
    }

    public void showEndDatePickerDialog(View v) {
        DialogFragment newFragment = null;
        if(isEndDatePicked) {
            String[] dateString = ((Button) findViewById(R.id.btnEndChangeDate)).getText().toString().split(Constants.DATE_DELIMITER);
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.valueOf(dateString[2]) , Integer.valueOf(dateString[0]) - 1, Integer.valueOf(dateString[1]));
            Bundle b = bundleData(false, cal.getTime().getTime());
            newFragment = new DatePickerFragment();
            newFragment.setArguments(b);
        } else {
            Bundle b = bundleData(false, null);
            newFragment = new DatePickerFragment();
            newFragment.setArguments(b);
        }
        newFragment.show(getSupportFragmentManager(), "datePicker");
        currentDateButton = (Button) act.findViewById(R.id.btnEndChangeDate);
    }

    public void showStartTimePickerDialog(View v) {
        DialogFragment newFragment = null;
        if(isStartTimePicked) {
            String[] timeString = ((Button) findViewById(R.id.btnStartChangeTime)).getText().toString().split(Constants.TIME_DELIMITER);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeString[0]));
            cal.set(Calendar.MINUTE, Integer.valueOf(timeString[1]));
            Bundle b = bundleData(true, cal.getTime().getTime());
            newFragment = new TimePickerFragment();
            newFragment.setArguments(b);
        } else {
            Bundle b = bundleData(true, null);
            newFragment = new TimePickerFragment();
            newFragment.setArguments(b);
        }
        newFragment.show(getSupportFragmentManager(), "timePicker");
        currentTimeButton = (Button) act.findViewById(R.id.btnStartChangeTime);
    }

    public void showEndTimePickerDialog(View v) {
        DialogFragment newFragment = null;
        if(isEndTimePicked) {
            String[] timeString = ((Button) findViewById(R.id.btnEndChangeTime)).getText().toString().split(Constants.TIME_DELIMITER);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeString[0]));
            cal.set(Calendar.MINUTE, Integer.valueOf(timeString[1]));
            Bundle b = bundleData(false, cal.getTime().getTime());
            newFragment = new TimePickerFragment();
            newFragment.setArguments(b);
        } else {
            Bundle b = bundleData(false, null);
            newFragment = new TimePickerFragment();
            newFragment.setArguments(b);
        }
        newFragment.show(getSupportFragmentManager(), "timePicker");
        currentTimeButton = (Button) act.findViewById(R.id.btnEndChangeTime);
    }

    private Bundle bundleData(boolean isStart, Long timeInMilli) {
        Bundle b = new Bundle();
        b.putBoolean("isStart", isStart);
        if (timeInMilli != null)
            b.putLong("time", timeInMilli);
        return b;
    }

    public void onCheckedChanged(View view) {
        setPageAccordingToEventType();
    }

    public void selectFromMap(View view) {
        Intent intent = new Intent(getApplicationContext(), MapAddressActivity.class);
        if(location != null) {
            intent.putExtra("address", location);
        }
        startActivityForResult(intent, 12345);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (12345):
                if (resultCode == Activity.RESULT_OK) {
                    location = (AddressLocation) data.getSerializableExtra("address");
                    ((TextView) findViewById(R.id.createEvent_text_where)).setText(location.getAddress());
                }
                break;
        }
    }

    public void onClickCreate(View view) {
        Event newEvent = new Event();
        String eventName = ((EditText) findViewById(R.id.createEvent_text_event_name)).getText().toString();
        String eventDescription = ((EditText) findViewById(R.id.createEvent_text_event_description)).getText().toString();
        String location = ((TextView) findViewById(R.id.createEvent_text_where)).getText().toString();
        String[] sDate = ((Button) findViewById(R.id.btnStartChangeDate)).getText().toString().split(Constants.DATE_DELIMITER);
        String[] eDate = ((Button) findViewById(R.id.btnEndChangeDate)).getText().toString().split(Constants.DATE_DELIMITER);
        String[] sTime = ((Button) findViewById(R.id.btnStartChangeTime)).getText().toString().split(Constants.TIME_DELIMITER);
        String[] eTime = ((Button) findViewById(R.id.btnEndChangeTime)).getText().toString().split(Constants.TIME_DELIMITER);
        boolean isCorrectDates = false;

        if (Constants.EMPTY_STRING.equals(eventName.trim()) || Constants.EMPTY_STRING.equals(eventDescription.trim())
                || Constants.EMPTY_STRING.equals(location.trim()) || !(isStartDatePicked) || !(isStartTimePicked && isEndTimePicked)) {
            Toast.makeText(this, "Invalid data entered", Toast.LENGTH_SHORT).show();
        } else {
            Date startDate = DateUtils.convertToDate(Integer.valueOf(sDate[1]), Integer.valueOf(sDate[0]) - 1, Integer.valueOf(sDate[2]), Integer.valueOf(sTime[0]), Integer.valueOf(sTime[1]));
            Date endDate = null;
            if (isEndDatePicked)
                endDate = DateUtils.convertToDate(Integer.valueOf(eDate[1]), Integer.valueOf(eDate[0]) - 1, Integer.valueOf(eDate[2]), Integer.valueOf(eTime[0]), Integer.valueOf(eTime[1]));
            else
                endDate = DateUtils.convertToDate(Integer.valueOf(sDate[1]), Integer.valueOf(sDate[0]) - 1, Integer.valueOf(sDate[2]), Integer.valueOf(eTime[0]), Integer.valueOf(eTime[1]));
            isCorrectDates = DateUtils.checkDateCorrectness(startDate, endDate);
            if (endDate.getTime() - startDate.getTime() <= 0 || !isCorrectDates) {
                Toast.makeText(this, "Error in start and end date", Toast.LENGTH_SHORT).show();
            } else {
                newEvent.setEventName(eventName);
                newEvent.setEventDesc(eventDescription);
                newEvent.setStartDate(startDate);
                newEvent.setEndDate(endDate);
                newEvent.setLocation(this.location.getAddress());
                newEvent.setLatitude(this.location.getLatitude());
                newEvent.setLongitude(this.location.getLongitude());
                newEvent.setShortLocation(this.location.getShortAddress());
                newEvent.setFenceRadius(this.location.getRadiusFence());
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

    private void setRecurringAlarm(Context context, long time) {
        Intent downloader = new Intent(context, GalleryBroadcastReceiver.class);
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context,
                0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) this.getSystemService(
                Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                time,
                AlarmManager.INTERVAL_DAY, recurringDownload);
        Log.d("Service", "SCHEDULED");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        int year, month, day;
        boolean isStart;

        public DatePickerFragment() {
            // Default Constructor
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle b = this.getArguments();
            Long time = null;
            if (b.containsKey("time"))
                time = b.getLong("time");
            Calendar c = Calendar.getInstance();
            if(time != null) {
                c.setTimeInMillis(time);
            }
            this.isStart = b.getBoolean("isStart");
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            currentDateButton.setText(Utils.appendStrings(Constants.DATE_DELIMITER, String.valueOf(month + 1), String.valueOf(day), String.valueOf(year)));
            if (isStart)
                isStartDatePicked = true;
            else
                isEndDatePicked = true;
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        int hour, minute;
        boolean isStart;

        public TimePickerFragment() {
            // Default Constructor
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle b = this.getArguments();
            Long time = null;
            if (b.containsKey("time"))
                time = b.getLong("time");
            Calendar c = Calendar.getInstance();
            if(time != null) {
                c.setTimeInMillis(time);
            }
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            this.isStart = b.getBoolean("isStart");
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            currentTimeButton.setText(Utils.appendStrings(Constants.TIME_DELIMITER, String.valueOf(hourOfDay), String.valueOf(minute)));
            if (isStart)
                isStartTimePicked = true;
            else
                isEndTimePicked = true;
        }
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
            if (result.equals("")) {
                status = "Failure";
            } else {
                newEvent.setEventId(result);
                status = "Success";
                Intent intent = new Intent(act, InviteEventActivity.class);
                intent.putExtra("Error", Constants.CREATE_SCREEN_ERROR_MSG);
                intent.putExtra("event", newEvent);
                startActivity(intent);
                setRecurringAlarm(getApplicationContext(), newEvent.getStartDate().getTime());
            }
            Toast.makeText(act, status, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}