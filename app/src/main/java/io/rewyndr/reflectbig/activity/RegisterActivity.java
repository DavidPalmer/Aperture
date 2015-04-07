package io.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rewyndr.reflectbig.R;
import io.rewyndr.reflectbig.interfaces.LoginService;
import io.rewyndr.reflectbig.service.ServiceFactory;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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

    public void signUp(View view) {
        LoginService loginService = ServiceFactory.getLoginServiceInstance(this);
        TextView emailText = (TextView) findViewById(R.id.user_email);
        TextView userNameText = (TextView) findViewById(R.id.username);
        TextView passwordText = (TextView) findViewById(R.id.user_password);
        try {
            loginService.signUp(userNameText.getText().toString(), emailText.getText().toString(), passwordText.getText().toString());
            Toast.makeText(getApplicationContext(), "Registration Success Full !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, LoginActivity.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
