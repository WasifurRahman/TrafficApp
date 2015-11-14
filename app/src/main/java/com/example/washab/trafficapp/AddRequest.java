package com.example.washab.trafficapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddRequest extends AppCompatActivity {

    int fromLocationIndex, toLocationIndex;

    String locationChoices[] = Locations.getAllLocationNames();
    private String description;
    private int fromLocationId, toLocationId;
    private boolean addRequestButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        initiateFromLocation();
        initiateToLocation();
    }

    public void initiateFromLocation() {
        Spinner spinner = (Spinner) findViewById(R.id.fromLocationSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLocationIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item,locationChoices);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
    }


    public void initiateToLocation() {
        Spinner spinner = (Spinner) findViewById(R.id.toLocationSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLocationIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, locationChoices);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_request, menu);
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

    public void onAddRequestButtonClick(View v){

        if(v.getId()==R.id.addRequestButton && addRequestButtonPressed==false){
            addRequestButtonPressed = true;
            fromLocationId=Locations.getLocationId(locationChoices[fromLocationIndex]);
            toLocationId=Locations.getLocationId(locationChoices[toLocationIndex]);
            description = ((EditText) findViewById(R.id.addRequestDescriptionBox)).getText().toString();

            Log.d("AddRequestEntries", fromLocationId + " " + toLocationId + " " + description);

            AddRequestTask addRequestTask = new AddRequestTask();
            addRequestTask.execute();
        }

    }

    /**
     *  SET BACKGROUND TASK
     */

    class AddRequestTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonAddRequest;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("fromLocationId", fromLocationId));
            params.add(new Pair("toLocationId", toLocationId));
            params.add(new Pair("description", description));
            params.add(new Pair("requesterId", Utility.CurrentUser.getId()));
            // getting JSON string from URL
            jsonAddRequest = jParser.makeHttpRequest("/addrequest", "POST", params);
//            jsonLocations = jParser.makeHttpRequest("/locations", "GET", null);

            try {
//                Boolean error = jsonLogin.getBoolean("error");
//                errorMessage = jsonLogin.getString("message");
//                if(error)loginError=true;
            }catch(Exception e){
                e.printStackTrace();
            }

            // Check your log cat for JSON reponse
//            Log.e("All info: ", jsonLogin.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){

//            if(loginError){
//                errorText.setText(errorMessage);
//            }
//            else{
//                assignUser(jsonLogin);
//                assignLocations(jsonLocations);
//            String s = Utility.CurrentUser.makeString();
//                Log.d("Client User", s);


            Intent intent=new Intent(AddRequest.this, Home.class);
            startActivity(intent);

        }
    }
//    }

}
