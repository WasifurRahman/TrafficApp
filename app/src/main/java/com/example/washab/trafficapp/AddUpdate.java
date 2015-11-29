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

public class AddUpdate extends AppCompatActivity implements Interfaces.WhoIsCallingUpdateInterface{

    int fromLocationIndex, toLocationIndex, situationIndex, timestampIndex;

    String locationChoices[] = Locations.getAllLocationNames();
    String timestampChoices[] = {"Now", "10 minutes ago", "20 minutes ago", "30 minutes ago", "45 minutes ago"};
    int timeToDeduct[]={0,10,20,30,45};
    String situationChoices[] = {"Free", "Mild", "Moderate", "Extreme", "Gridlock"};
    private String description;
    private String situation, timestamp;
    private int fromLocationId, toLocationId, estimatedTime, requestId;
    private boolean addUpdateButtonPressed = false;
    private String fromLocationName,toLocationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        int callingFrom=getIntent().getIntExtra("calling_from",0);
        switch(callingFrom){
            case Interfaces.WhoIsCallingUpdateInterface.ADD_UPDATE_WILLINGLY:
                handleNormalUpdate();
                Log.d("update","normal");
                break;
            case Interfaces.WhoIsCallingUpdateInterface.ADD_UPDATE_TO_RESPOND_TO_REQUEST:
                handleResponseUpdate();
                Log.d("update","response");
                break;
        }

    }

    private void handleNormalUpdate(){
        initiateFromLocation();
        initiateToLocation();
        initiateTimestamp();
        initiateSituation();

    }

    private void handleResponseUpdate(){
        initiateFromLocationForResponse();
        initiateToLocationForResponse();
        initiateTimestamp();
        initiateSituation();
        requestId = getIntent().getIntExtra("request_id", 0);
    }

    public void initiateFromLocationForResponse() {
        Spinner spinner = (Spinner) findViewById(R.id.fromLocationSpinner);
        fromLocationName=Locations.getLocationName(getIntent().getIntExtra("location_from",0));



        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item,new String[]{fromLocationName});
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);


    }


    public void initiateToLocationForResponse() {
        Spinner spinner = (Spinner) findViewById(R.id.toLocationSpinner);
        toLocationName=Locations.getLocationName(getIntent().getIntExtra("location_to",0));


        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, new String[]{toLocationName});
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);


    }

    public void initiateFromLocation() {
            Spinner spinner = (Spinner) findViewById(R.id.fromLocationSpinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    fromLocationIndex = position;
                    fromLocationName=locationChoices[fromLocationIndex];
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
                toLocationName=locationChoices[toLocationIndex];
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

    public void initiateTimestamp() {
        Spinner spinner = (Spinner) findViewById(R.id.timestampSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                timestampIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timestampChoices);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void initiateSituation() {
        Spinner spinner = (Spinner) findViewById(R.id.situationSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                situationIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, situationChoices);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_update, menu);
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

    public void onAddUpdateButtonClick(View v){

        if(v.getId()==R.id.addUpdateButton && addUpdateButtonPressed==false){
            addUpdateButtonPressed = true;
            fromLocationId=Locations.getLocationId(fromLocationName);
            toLocationId=Locations.getLocationId(toLocationName);
            situation=situationChoices[situationIndex];
            int timeToSubtract=timeToDeduct[timestampIndex];

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
//            dateFormat.format(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, -1 * timeToSubtract);
            Date newDate = cal.getTime();
            Log.d("timestamp", dateFormat.format(newDate));

            timestamp = dateFormat.format(newDate);
            estimatedTime = Integer.parseInt(((EditText) findViewById(R.id.estimatedTimeEditText)).getText().toString());
            description = ((EditText) findViewById(R.id.addUpdateDescriptionBox)).getText().toString();

            Log.d("AddUpdateEntries", fromLocationId + " " + toLocationId + " " + situation + " " + timestamp + " " + estimatedTime + " " + description);

            AddUpdateTask addUpdateTask = new AddUpdateTask();
            addUpdateTask.execute();
        }

    }


    /**
     *  SET BACKGROUND TASK
     */

    class AddUpdateTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonAddUpdate;

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
            params.add(new Pair("estTimeToCross", estimatedTime));
            params.add(new Pair("situation", situation));
            params.add(new Pair("description", description));
            params.add(new Pair("timeOfSituation", timestamp));
            params.add(new Pair("updaterId", Utility.CurrentUser.getId()));
            params.add(new Pair("requestId", requestId));

            // getting JSON string from URL
            jsonAddUpdate = jParser.makeHttpRequest("/addupdate", "POST", params);
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
                String s = Utility.CurrentUser.makeString();
//                Log.d("Client User", s);


                Intent intent=new Intent(AddUpdate.this, Home.class);
                startActivity(intent);

            }
        }
//    }
}
