package com.example.washab.trafficapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    public String email, password, errorMessage;
    TextView errorText;
    boolean loginError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorText = (TextView)findViewById(R.id.errorTextView);
        loginError = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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

    private boolean showSignUpError(){


        if(email.isEmpty() || password.isEmpty()){
            return true;
        }
        return false;
    }

    public void onSignUpButtonClick(View v) {

        if(v.getId() == R.id.signupButton)
        {
            Log.e(MainActivity.class.getSimpleName(),"signUpButtonPressed");

            Intent intent=new Intent(this,SignUp.class);
            startActivity(intent);
        }
    }

    public void onLoginButtonClick (View v) {
        if(v.getId() == R.id.loginButton) {

            Log.d(MainActivity.class.getSimpleName(),"loginButtonPressed");
//            Intent intent=new Intent(MainActivity.this,Home.class);
//            startActivity(intent);
//            Log.e(MainActivity.class.getSimpleName(),"loginButtonPressed");
//
            email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
            password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();

            if(showSignUpError()){
                errorText.setText("One or more required fields are missing!\n");
            } else if(!SignUp.validate(email)) {
                errorText.setText("Please enter a valid address.\n");
            } else {
                LoginTask loginTask = new LoginTask();
                loginTask.execute();
            }


        }
    }



    static void assignUser(JSONObject jObj){
        int userId=0;
        String userName=" ",apiKey=" ";

        try {
            userId = jObj.getInt("id");
            userName = jObj.getString("username");
            apiKey = jObj.getString("apiKey");
        }catch(JSONException e){
            e.printStackTrace();
        }

        Utility.CurrentUser.setUser(userId, userName, apiKey);
        Log.d("user assigned: ",""+ Utility.CurrentUser.makeString());

    }


    static void assignLocations(JSONObject json) {

        try {
            Boolean invalidResult=json.getBoolean("error");

            if(!invalidResult){
                JSONArray locationArray=json.getJSONArray("locations");

                for(int i=0;i<locationArray.length();i++){
                    JSONObject currentLocation=locationArray.getJSONObject(i);
                    String locationName=currentLocation.getString("locationName");
                    int locationId=currentLocation.getInt("locationId");
                    Locations.addLocation(locationName,locationId);

                }
                Locations.prepareLocationNameArray();
                Log.d("testing: " , Locations.prepString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    class LoginTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonLogin, jsonLocations;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("email",email));
            params.add(new Pair("password",password));
            // getting JSON string from URL
            jsonLogin = jParser.makeHttpRequest("/login", "POST", params);
            jsonLocations = jParser.makeHttpRequest("/locations", "GET", null);

            // Check your log cat for JSON reponse
//            Log.e("All info: ", jsonLogin.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){

            if(jsonLogin == null) {
                Utility.CurrentUser.showConnectionError(getApplicationContext());
                return;
            }
            try {
                Boolean error = jsonLogin.getBoolean("error");
                errorMessage = jsonLogin.getString("message");
//                System.out.println("Error: " + error + "\nMessage: " + errorMessage);
//                Log.d("Error", error.toString());
//                Log.d("Message", errorMessage);
                if(error)loginError=true;
//                return message;
            }catch(JSONException e){
                e.printStackTrace();
            }

            if(loginError){
                errorText.setText(errorMessage);
            }
            else{
                assignUser(jsonLogin);
                assignLocations(jsonLocations);
                String s = Utility.CurrentUser.makeString();
                Log.d("Client User", s);


                Intent intent=new Intent(MainActivity.this,Home.class);
//                    Log.d("Where now?", "Starting Home Activity");
                startActivity(intent);

            }
        }
    }
}
