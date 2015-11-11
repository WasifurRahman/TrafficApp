package com.example.washab.trafficapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EmailVerification extends AppCompatActivity {

    private int emailVerifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emailVerifyCode=getIntent().getIntExtra("emailVerifyCode",0);
        setContentView(R.layout.activity_email_verification);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_email_verification, menu);
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

    public void onVerifyButtonClick(View v) {

        if(v.getId() == R.id.verifyEmailButton)
        {

            int givenVerifyId=Integer.parseInt(((EditText)findViewById(R.id.codeEditText)).getText().toString());
            if(givenVerifyId==emailVerifyCode){

                Intent returnIntent = getIntent();

                setResult(RESULT_OK,returnIntent);
                finish();
            }
            else{
                ((TextView)findViewById(R.id.emailVerifyErrorTextView)).setText("Please Enter the correct verification code");
            }
        }
    }
}
