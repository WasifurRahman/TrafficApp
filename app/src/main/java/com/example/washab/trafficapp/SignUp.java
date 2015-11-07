package com.example.washab.trafficapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity {

    public String email, username, gender, password;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

 /*   public boolean onFemaleRBClick(View v) {

        if(onMaleRBClick(v)) {

        }

    }

    public boolean onMaleRBClick(View v) {


    }
*/



    public void onSignUpButtonClick(View v) {

        if(v.getId() == R.id.signupButton)
        {
            email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
            password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
            username = ((EditText) findViewById(R.id.usernameEditText)).getText().toString();
            genderRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            int selectedId = genderRadioGroup.getCheckedRadioButtonId();
            genderRadioButton = (RadioButton) findViewById(selectedId);
            gender = genderRadioButton.getText().toString();
           // System.out.println("email " + email);
            Log.e("EMAIL", email+"\n" +password+"\n"+username+"\n"+gender+"\n");

            SignUpTask signUpTask = new SignUpTask();
            String params[] = {email, password, username, gender};

            signUpTask.execute(params);


            Intent intent=new Intent(this,EmailVerification.class);
            startActivity(intent);
        }
    }

    class SignUpTask extends AsyncTask<String[], Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String[]... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("email",args[0]));
            params.add(new Pair("password",args[1]));
            params.add(new Pair("username",args[2]));
            params.add(new Pair("gender",args[3]));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest("", "POST", params);

            // Check your log cat for JSON reponse
            Log.e("All info: ", json.toString());
/*
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
*/
            return null;

        }

            /**
             * After completing background task Dismiss the progress dialog
             **/
            protected void onPostExecute (String file_url){
                // dismiss the dialog after getting all products
    /*        pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
    /*                ListAdapter adapter = new SimpleAdapter(
                            AllProductsActivity.this, productsList,
                            R.layout.list_item, new String[]{TAG_PID,
                            TAG_NAME},
                            new int[]{R.id.pid, R.id.name});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }
    }*/
        }
    }
}
