package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.common.PreferenceConstants;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        Button save = (Button) findViewById(R.id.button4);
        Switch isAutoUpload = (Switch) findViewById(R.id.switch1);
        SharedPreferences preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
        if(!preferences.contains(PreferenceConstants.AUTO_UPLOAD_STATUS) || preferences.getBoolean(PreferenceConstants.AUTO_UPLOAD_STATUS, false)) {
            isAutoUpload.setChecked(true);
        } else {
            isAutoUpload.setChecked(false);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch isAutoUpload = (Switch) findViewById(R.id.switch1);
                SharedPreferences preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if(isAutoUpload.isChecked()) {
                    editor.putBoolean(PreferenceConstants.AUTO_UPLOAD_STATUS, true);
                    editor.commit();
                } else {
                    editor.putBoolean(PreferenceConstants.AUTO_UPLOAD_STATUS, false);
                    editor.commit();
                }
                Toast.makeText(getApplicationContext(), "Preference saved successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
