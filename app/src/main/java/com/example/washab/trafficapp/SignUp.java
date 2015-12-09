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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    public String email, username, gender, password, errorMessage,repeatPassword;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;
    private int emailVerifyCode;
    TextView errorText;
    private boolean signUpError;
    private static int EMAIL_VERIFICATION_REQUEST=1;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        genderRadioButton=(RadioButton)findViewById(R.id.maleRadioButton);
        errorText=((TextView)findViewById(R.id.errorTextView));
        signUpError=false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.maleRadioButton:
                if (checked)
                    genderRadioButton=(RadioButton)findViewById(R.id.maleRadioButton);
                break;
            case R.id.femaleRadioButton:
                if (checked)
                    genderRadioButton=(RadioButton)findViewById(R.id.femaleRadioButton);
                break;
        }
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


        if(email.isEmpty() || password.isEmpty() || gender.isEmpty() || username.isEmpty()){

            return true;
        }
        return false;
    }

   int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
//   private void sendEmail(String email){
//       emailVerifyCode=randomWithRange(1000,9999);
//       Intent i = new Intent(Intent.ACTION_SEND);
//      // i.setType("message/rfc822");
//       i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
//       i.putExtra(Intent.EXTRA_SUBJECT, "Email Address Verification");
//       i.putExtra(Intent.EXTRA_TEXT   , "Your email verification code is: "+ emailVerifyCode);
//       try {
//           startActivity(Intent.createChooser(i, "Send mail..."));
//           Log.d("email sent", "email sent success");
//       } catch (android.content.ActivityNotFoundException ex) {
//           Toast.makeText(SignUp.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//       }
//   }


    public void onSignUpButtonClick(View v) {

        if(v.getId() == R.id.signupButton)
        {
            email = ((EditText) findViewById(R.id.emailSignUpEditText)).getText().toString();
            password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
            username = ((EditText) findViewById(R.id.usernameEditText)).getText().toString();
            repeatPassword= ((EditText) findViewById(R.id.repeatPasswordEditText)).getText().toString();
           // genderRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
           // int selectedId = genderRadioGroup.getCheckedRadioButtonId();
            //genderRadioButton = (RadioButton) findViewById(selectedId);
            gender = genderRadioButton.getText().toString();
           // System.out.println("email " + email);
            if(showSignUpError()){
                errorText.setText("One or more required fields are missing!\n");
            }else if(!validate(email)) {
                errorText.setText("Please enter a valid address.\n");
            }
            else if(passwordMissMatch()){
                errorText.setText("Password Mismatch!\n");
            }
            else
           {
                Log.e("EMAIL", email + "\n" + password + "\n" + username + "\n" + gender + "\n");


                SignUpTask signUpTask = new SignUpTask();
                signUpTask.execute();
//                if(signUpError){
//                    errorText.setText(errorMessage);
//                }
//               else{
//                    Intent intent=new Intent(this,Home.class);
//                    Log.d("Where now?", "Starting Home Activity");
//                    startActivity(intent);
//
//                }
                //sendEmail(email);
                //Intent intent = new Intent(this, EmailVerification.class);

                //intent.putExtra("emailVerifyCode",emailVerifyCode);
                //startActivityForResult(intent, EMAIL_VERIFICATION_REQUEST);


            }
        }
    }

    private boolean passwordMissMatch() {
        //Log.d("signupcheck",password +" "+repeatPassword);
        if(password.equals(repeatPassword))return false;
        return true;

    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == EMAIL_VERIFICATION_REQUEST) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//
//                SignUpTask signUpTask = new SignUpTask();
//                signUpTask.execute();
//            }
//            else{
//
//            }
//        }
//    }
    class SignUpTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonSignUp, jsonLocations;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();
            params.add(new Pair("username",username));
            params.add(new Pair("gender",gender));
            params.add(new Pair("email",email));
            params.add(new Pair("password",password));
            // getting JSON string from URL
            jsonSignUp = jParser.makeHttpRequest("/register", "POST", params);
            jsonLocations = jParser.makeHttpRequest("/locations", "GET", null);

            return null;
        }

            /**
             * After completing background task Dismiss the progress dialog
             **/
            protected void onPostExecute (String file_url){
                if(jsonSignUp == null) {
                    Utility.CurrentUser.showConnectionError(getApplicationContext());
                    return;
                }

                try {
                    Boolean error = jsonSignUp.getBoolean("error");
                    errorMessage = jsonSignUp.getString("message");
                    System.out.println("Error: " + error + "\nMessage: " + errorMessage);
                    Log.d("Error", error.toString());
                    Log.d("Message", errorMessage);
                    if(error)signUpError=true;
//                return message;
                }catch(JSONException e){
                    e.printStackTrace();
                }

                if(signUpError){
                    errorText.setText(errorMessage);
                }
                else{
                    MainActivity.assignUser(jsonSignUp);
                    MainActivity.assignLocations(jsonLocations);
                    Intent intent=new Intent(SignUp.this,Home.class);
//                    Log.d("Where now?", "Starting Home Activity");
                    startActivity(intent);

                }
        }
    }
}
