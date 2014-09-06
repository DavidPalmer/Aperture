package com.rewyndr.reflectbig.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.common.PreferenceConstants;
import com.rewyndr.reflectbig.interfaces.LoginService;
import com.rewyndr.reflectbig.service.ServiceFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


public class LoginActivity extends Activity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.txt_autoCompleteLogin);
        populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.txt_password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailId = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                loginIntoReflectBig(emailId, password);
            }
        });
    }

    private void loginIntoReflectBig(String emailId, String password) {
        LoginService service = ServiceFactory.getLoginServiceInstance(context);
        try {
            service.logIn(emailId, password);
            SharedPreferences preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PreferenceConstants.LOGIN_EMAIL_ID, emailId);
            editor.putString(PreferenceConstants.LOGIN_PASSWORD, password);
            editor.commit();
            moveToEventsPage();
        } catch (Exception e) {
            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToEventsPage() {
        Intent intent = new Intent(this, EventsListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
        String emailId = preferences.getString(PreferenceConstants.LOGIN_EMAIL_ID, "");
        String password = preferences.getString(PreferenceConstants.LOGIN_PASSWORD, "");
        if(emailId.equals("") || emailId.equals("")) {
            // Do nothing. Just load the page
        } else {
            loginIntoReflectBig(emailId, password);
        }
    }

    private void populateAutoComplete() {
        Set<String> emails = new HashSet<String>();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                emails.add(account.name);
            }
        }

        List<String> emailList = new ArrayList<String>();
        emailList.addAll(emails);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailList);
        mEmailView.setAdapter(adapter);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
    }*/
}
