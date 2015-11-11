package com.example.washab.trafficapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);
        initiateFromLocation();
        initiateToLocation();
        initiateTimestamp();
    }

    public void initiateFromLocation() {
            Spinner spinner = (Spinner) findViewById(R.id.fromLocationSpinner);

        // Array of choices
            String colors[] = {"Red","Blue","White","Yellow","Black", "Green","Purple","Orange","Grey"};

    // Application of the Array to the Spinner
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, colors);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner.setAdapter(spinnerArrayAdapter);


    }


    public void initiateToLocation() {
        Spinner spinner = (Spinner) findViewById(R.id.toLocationSpinner);

        // Array of choices
        String colors[] = {"Red","Blue","White","Yellow","Black", "Green","Purple","Orange","Grey"};

        // Selection of the spinner


        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, colors);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);


    }

    public void initiateTimestamp() {
        Spinner spinner = (Spinner) findViewById(R.id.timestampSpinner);

        // Array of choices
        String choices[] = {"Now", "10 minutes ago", "20 minutes ago", "30 minutes ago", "45 minutes ago"};

        // Selection of the spinner


        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choices);
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

    public void onAddUpdateButtonClick(View v) {

    }
}
