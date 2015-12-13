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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddRequest extends AppCompatActivity {

    int fromLocationIndex, toLocationIndex;
    String[] locationArray;

    String locationChoices[] = Locations.getAllLocationNames();
    private String description;
    private int fromLocationId, toLocationId;
    private boolean addRequestButtonPressed = false;
    Button addRequestButton;
    AddRequestTask addRequestTask = new AddRequestTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        initiateFromLocation();
        initiateToLocation();
    }

    public void initiateFromLocation() {
        Spinner spinner = (Spinner) findViewById(R.id.fromLocationSpinner);

        locationArray = new String[locationChoices.length + 1];
        locationArray[0] = "< Select an option >";
        System.arraycopy(locationChoices, 0, locationArray, 1, locationChoices.length);

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
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item,locationArray);
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
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, locationArray);
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
            fromLocationId=Locations.getLocationId(locationArray[fromLocationIndex]);
            toLocationId=Locations.getLocationId(locationArray[toLocationIndex]);
            description = ((EditText) findViewById(R.id.addRequestDescriptionBox)).getText().toString();

            Log.d("AddRequestEntries", fromLocationId + " " + toLocationId + " " + description);

            if(fromLocationId == -1 || toLocationId == -1) {
                Toast.makeText(this, "One or more required field(s) missing!", Toast.LENGTH_LONG).show();
            }
            else if(locationArray[fromLocationIndex].equals(locationArray[toLocationIndex])) {
                Toast.makeText(this,"Source and Destination locations must be different", Toast.LENGTH_LONG).show();
            }
            else {
                addRequestButton = (Button)v.findViewById(R.id.addRequestButton);
                addRequestButton.setClickable(false);
                addRequestTask.execute();
            }

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

            addRequestButton.setClickable(true);
            Intent intent=new Intent(AddRequest.this, Home.class);
            startActivity(intent);

        }
    }
//    }

}
