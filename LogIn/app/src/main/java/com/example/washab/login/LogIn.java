package com.example.washab.login;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogIn extends AppCompatActivity implements View.OnClickListener {


    public String userName, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            userName = ((EditText) findViewById(R.id.editText)).getText().toString();
            password = ((EditText) findViewById(R.id.editText2)).getText().toString();
            new LoginTask().execute();
        }
    }


    class LoginTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonLogin, jsonLocations;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("userName", userName));
            params.add(new Pair("password", password));
            Log.d("login",userName+" "+password);
            // getting JSON string from URL
            jsonLogin = jParser.makeHttpRequest("/login", "POST", params);

            Log.d("login", jsonLogin.toString());
            //jsonLocations = jParser.makeHttpRequest("/locations", "GET", null);

            // Check your log cat for JSON reponse
//            Log.e("All info: ", jsonLogin.toString());
            return null;

        }
    }

}
