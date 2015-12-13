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
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddDiscussion extends AppCompatActivity {

    int locationIndex, locationId;
    String description;
    String[] locationArray;

    String locationChoices[] = Locations.getAllLocationNames();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discussion);
        initiateLocation();
    }

    public void initiateLocation() {
        Spinner spinner = (Spinner) findViewById(R.id.announcementLocationSpinner);

        locationArray = new String[locationChoices.length + 1];
        locationArray[0] = "< Select an option >";
        System.arraycopy(locationChoices, 0, locationArray, 1, locationChoices.length);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                locationIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locationArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_discussion, menu);
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

    public void onAddDiscussionButtonClick(View v) {

        if (v.getId() == R.id.addDiscussionButton) {
//            addUpdateButtonPressed = true;
            locationId = Locations.getLocationId(locationArray[locationIndex]);
            Log.d("locationId AddDisc", "" + locationId);
            if(locationId == -1) {
                locationId = 0;
            }

            description = ((EditText) findViewById(R.id.addDiscussionDescriptionBox)).getText().toString();

//            Log.d("AddUpdateEntries", fromLocationId + " " + toLocationId + " " + situation + " " + timestamp + " " + estimatedTime + " " + description);

            if(description.equals("")) {
                Toast.makeText(this, "Write something!", Toast.LENGTH_LONG).show();
            }
            else
                new AddDiscussionTask().execute();

        }
    }



    /**
     *  SET BACKGROUND TASK
     */

    class AddDiscussionTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonAddPost;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("postType", "discussion"));
            params.add(new Pair("locationId", locationId));
            params.add(new Pair("description", description));
            params.add(new Pair("posterId", Utility.CurrentUser.getId()));
            // getting JSON string from URL
            jsonAddPost = jParser.makeHttpRequest("/addpost", "POST", params);
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

            Intent intent=new Intent(AddDiscussion.this, Home.class);
            startActivity(intent);

        }
    }
//    }


}
