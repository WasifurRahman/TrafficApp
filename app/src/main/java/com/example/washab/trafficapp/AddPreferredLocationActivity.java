package com.example.washab.trafficapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddPreferredLocationActivity extends AppCompatActivity implements  View.OnClickListener{

    private int selectedLocationIndex;
    private String[] availableLocations;
    private Context classContext;
    private ArrayList<Location>availableLocationsArrayList;
    private ArrayList<Integer>alreadySubscribedLocationsArrayList;
    LinearLayout progressLayout,listViewHolder;
    private ListView preferedLocationListView;
    private int locationIdToFollow =0;
    private int locationIdToUnfollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preferred_location);
        availableLocations=Locations.getAllLocationNames();
        classContext=this;
        progressLayout = (LinearLayout) findViewById(R.id.progressbar_view);
        listViewHolder=(LinearLayout)findViewById(R.id.preferredLocationHolderLayout);

        //populateArrayList();
        //populateListView();
        new FetchAllSubscribedLocationId().execute();
    }

//    private void populateRequestList(JSONObject jsonRequests){
//
//        try {
//
//            JSONArray allRequests=jsonRequests.getJSONArray("requests");
//            allRequestsArrayList.clear();
//            for(int i=0;i<allRequests.length();i++){
//                JSONObject curObj=allRequests.getJSONObject(i);
//                //Log.d("in populte: ",curObj.toString());
//                Request curRequest = Request.createRequest(curObj);
//                Log.d("new request", curRequest.toString());
//                allRequestsArrayList.add(curRequest);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void populateAllLocationsArrayList(){
        availableLocationsArrayList=new ArrayList<Location>();
        for(int i=0;i<availableLocations.length;i++){
            String name=availableLocations[i];
            int locationId=Locations.getLocationId(name);
            Location tempLocation;
            if(alreadySubscribedLocationsArrayList.indexOf(locationId)==-1)//the locationId was not selected before
            {
                tempLocation=new Location(locationId,name,false);
            }
            else{
                tempLocation=new Location(locationId,name,true);
            }

            availableLocationsArrayList.add(tempLocation);
        }
    }
    private void populateAllLocationsListView(){
        ArrayAdapter<Location> adapter = new MyListAdapter();
        ListView list=(ListView)findViewById(R.id.preferedLocationSelectionListview);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Location>{
        public MyListAdapter(){
            super(classContext, R.layout.user_select_preferred_location,availableLocationsArrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView=convertView;
            if(itemView==null){
                itemView=getLayoutInflater().inflate(R.layout.user_select_preferred_location,parent,false);
            }


            //find the update to work with

            final Location currentLocation=availableLocationsArrayList.get(position);

            TextView locationName=(TextView)itemView.findViewById(R.id.preferedLocationSelectionTextView);
            locationName.setText(currentLocation.getLocationName());

            CheckBox cb=(CheckBox)itemView.findViewById(R.id.preferedLocationSelectionCheckbox);

            cb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    currentLocation.setCheckedInThisSession(cb.isChecked());
                    Log.d("checked: ",currentLocation.getLocationName()+" is "+cb.isChecked());


                }
            });


            if(currentLocation.isAlreadyChecked()){
                cb.setChecked(true);
            }
            else{
                cb.setChecked(false);
            }


            return itemView;
            // return super.getView(position, convertView, parent);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_preferred_location, menu);
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
        Log.d("subscribed: ","inside");
//        if(v.getId()==R.id.addingPreferredLocationButton){
//
//            for(int i=0;i<availableLocationsArrayList.size();i++){
//                Location curLocation=availableLocationsArrayList.get(i);
//                if(curLocation.isCheckedInThisSession()){
//                    Log.d("subscribed: ",curLocation.getLocationName());
//                }
//            }
//
//        }
    }

    public void onAddingPreferredLocationButtonClick(View v) {

        if (v.getId() == R.id.addingPreferredLocationButton) {
            for (int i = 0; i < availableLocationsArrayList.size(); i++) {
                Location curLocation = availableLocationsArrayList.get(i);
                if (curLocation.isCheckedInThisSession() && curLocation.isAlreadyChecked()) {//it was checked beforehand and in this session
                    //do nothing
                    Log.d("checkedTest",curLocation.getLocationName()+" do nothing");

                }
                else if(curLocation.isCheckedInThisSession() && !curLocation.isAlreadyChecked()){
                    //new item. must be inserted in the database
                    locationIdToFollow =curLocation.getLocationId();
                    Log.d("checkedTest",curLocation.getLocationName()+" add to database");
                    new AddSubscribedLocationId().execute(locationIdToFollow+"");
                }
                else if(!curLocation.isCheckedInThisSession() && curLocation.isAlreadyChecked()){
                    //new item. must be inserted in the database
                    locationIdToUnfollow=curLocation.getLocationId();
                    Log.d("checkedTest",curLocation.getLocationName()+" remove from database");
                    new RemoveSubscribedLocationId().execute(locationIdToUnfollow+"");
                }

            }

        }

        Toast.makeText(this,"preferences updated",Toast.LENGTH_LONG).show();

        startActivity(new Intent(this,Home.class));
    }





    class FetchAllSubscribedLocationId extends AsyncTask<String, Void, String> {

        private JSONObject jsonAllSubscribedLocations;

        @Override
        protected void onPreExecute()
        {
            progressLayout.setVisibility(View.VISIBLE);
            listViewHolder.setVisibility(View.GONE);

            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("userId", Utility.CurrentUser.getId()));

             // getting JSON string from URL

            jsonAllSubscribedLocations = jParser.makeHttpRequest("/userlocations", "GET", params);
            Log.d("subscribed",jsonAllSubscribedLocations.toString());

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){


            if(jsonAllSubscribedLocations == null) {
                Utility.CurrentUser.showConnectionError(classContext);
                return;
            }

            getAllSubscribedLocations(jsonAllSubscribedLocations);
            populateAllLocationsArrayList();
            populateAllLocationsListView();

            progressLayout.setVisibility(View.GONE);
            listViewHolder.setVisibility(View.VISIBLE);


            //jsonUpdatesField=jsonAllSubscribedLocations;
            //populateDiscussionList(jsonAllSubscribedLocations);
            //populateDiscussionListView();
        }
    }


    class AddSubscribedLocationId extends AsyncTask<String, Void, String> {

        private JSONObject jsonAddSubscribedId;
        private int userId,locationId;

        @Override
        protected void onPreExecute()
        {
            locationId= locationIdToFollow;

            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("followerId", Utility.CurrentUser.getId()));
            params.add(new Pair("locationId", Integer.parseInt(args[0])));

            // getting JSON string from URL

            jsonAddSubscribedId = jParser.makeHttpRequest("/followlocation", "POST", params);
            Log.d("subscribed",jsonAddSubscribedId.toString());

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){


            if(jsonAddSubscribedId== null) {
                Utility.CurrentUser.showConnectionError(classContext);
                return;
            }
        }
    }


    class RemoveSubscribedLocationId extends AsyncTask<String, Void, String> {

        private JSONObject jsonREmoveSubscribedId;
        private int userId,locationId;

        @Override
        protected void onPreExecute()
        {
            locationId= locationIdToUnfollow;

            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("followerId", Utility.CurrentUser.getId()));
            params.add(new Pair("locationId", args[0]));

            // getting JSON string from URL

            jsonREmoveSubscribedId = jParser.makeHttpRequest("/unfollowlocation", "POST", params);
            Log.d("subscribed", jsonREmoveSubscribedId.toString());

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){


            if(jsonREmoveSubscribedId == null) {
                Utility.CurrentUser.showConnectionError(classContext);
                return;
            }
        }
    }


    private void getAllSubscribedLocations(JSONObject jsonAllSubscribedLocations) {
        alreadySubscribedLocationsArrayList=new ArrayList<Integer>();

        JSONArray allLocationsJSONArray= null;
        try {
            allLocationsJSONArray = jsonAllSubscribedLocations.getJSONArray("locations");
            for(int i=0;i<allLocationsJSONArray.length();i++){
                int curLocationId=allLocationsJSONArray.getJSONObject(i).getInt("locationId");
                alreadySubscribedLocationsArrayList.add(curLocationId);
                //Log.d("subscribed: ",curLocationId+"");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
