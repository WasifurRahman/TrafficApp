package com.example.washab.trafficapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFragment extends Fragment implements  Interfaces.WhoIsCallingUpdateInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ArrayList<Request> allRequestsArrayList =new ArrayList<Request>();
    private String sortingCriteria = "mostRecent";
    LinearLayout progressLayout;
    ListView customRequestListView;
    private int locationIdToSearch;
    private int requestToBeFollowed;
    private int followerId;
    private FetchRequestTask fetchRequestTask = new FetchRequestTask();
    private AddFollowerTask addFollowerTask = new AddFollowerTask();



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestFragment newInstance(String param1, String param2) {
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        progressLayout = (LinearLayout) getActivity().findViewById(R.id.progressbar_view);

        


        if(mListener.getTheIdOfTheActivityTHeFragmentIsAttachedTo()==Interfaces.ToWhichActivityIsTheFragmentAttached.HOME_ACTIVITY) {
            //Log.e("updatesfragment","called by home");

            customRequestListView=(ListView)getActivity().findViewById(R.id.userRequestListView);
            registerCallBack();
            new FetchRequestTask().execute();
        }else{
            progressLayout.setVisibility(View.GONE);


            Request currentRequest =mListener.passRequestObject();


            populateRequestList(currentRequest);
           populateRequestListView();


        }

        super.onActivityCreated(savedInstanceState);
    }


    private void registerCallBack() {

        ListView lv = (ListView) getActivity().findViewById(R.id.userRequestListView);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.callAppropriateDetailedActivity(Interfaces.WhichFragmentIsCallingDetailedActivity.REQUEST_FRAGMENT, allRequestsArrayList.get(position));

            }
        });
    }

    private void populateRequestList(Request currentRequest) {
        allRequestsArrayList.clear();
        allRequestsArrayList.add(currentRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        fetchRequestTask.cancel(true);
        addFollowerTask.cancel(true);
    }

    /**
     * The request list is prepared from database information.
     *
     * @param jsonRequests A JSON object containing info of requests from database.
     */

    private void populateRequestList(JSONObject jsonRequests){

        try {

            JSONArray allRequestsJSONArray = jsonRequests.getJSONArray("requests");
            allRequestsArrayList.clear();
            int curIndex = 0, N = allRequestsJSONArray.length();

            while (curIndex < N) {
                JSONObject curObj = allRequestsJSONArray.getJSONObject(curIndex++);

                Request curRequest = Request.createRequest(curObj);
                int followerCount = curRequest.getFollowerCount();
                for (int i = 0; i < followerCount; i++) {
//                    JSONObject likeObj = allRequestsJSONArray.getJSONObject(curIndex++);
                  JSONObject followObj = allRequestsJSONArray.getJSONObject(curIndex++);

                    Follower newFollower = new Follower(followObj.getInt("followerId"), followObj.getString("followerName"));
                    curRequest.addFollowerInitially(newFollower);
                }
                allRequestsArrayList.add(curRequest);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * ListView is prepared for displaying the list of requests.
     */

    private void populateRequestListView(){
        ArrayAdapter<Request> adapter = new MyListAdapter();
        ListView list=(ListView)getView().findViewById(R.id.userRequestListView);
        list.setAdapter(adapter);
    }

    /**
     *
     * @param locationIdToSearch A location to search requests for.
     */
    public void setRequestSearchLocation(int locationIdToSearch) {
        this.locationIdToSearch=locationIdToSearch;
        new FetchRequestTask().execute();
    }

    /**
     * Array adapter MyListAdapter accomodates requests dynamically and fills the list container with proper layout.
     */

    private class MyListAdapter extends ArrayAdapter<Request>{
        public MyListAdapter(){
            super(getActivity(), R.layout.user_request_item,allRequestsArrayList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View itemView=convertView;
            if(itemView==null){
                itemView=getActivity().getLayoutInflater().inflate(R.layout.user_request_item,parent,false);
            }


            //find the request to work with
            final Request currentRequest= allRequestsArrayList.get(position);
            //fill the view

            Follower mayBeFollower=new Follower(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());


            TextView locFrom = (TextView)itemView.findViewById(R.id.requestLocationFromTextView);
            locFrom.setText(Locations.getLocationName(currentRequest.getLocationIdFrom()));

            TextView locTo = (TextView)itemView.findViewById(R.id.requestLocationToTextView);
            locTo.setText(Locations.getLocationName(currentRequest.getLocationIdTo()));

            TextView sitDes=(TextView)itemView.findViewById(R.id.requestDescriptionBox);
            sitDes.setText(currentRequest.getDescription());

//            TextView requesterName=(TextView) itemView.findViewById(R.id.requesterNameTextView);
//            requesterName.setText(currentRequest.getRequesterName());

            TextView requesterId=(TextView) itemView.findViewById(R.id.requesterNameTextView);
            requesterId.setText("" + currentRequest.getRequesterName());

            TextView updateTime=(TextView) itemView.findViewById(R.id.requestUpdateTimeTextView);
            String timeOfRequest = Utility.CurrentUser.parsePostTime(currentRequest.getTimeOfRequest());
            updateTime.setText(timeOfRequest);

            final TextView followCnt=(TextView) itemView.findViewById(R.id.requestFollowerCountTextView);
            followCnt.setText("" + currentRequest.getFollowerCount());



            Button respondButton=(Button)itemView.findViewById(R.id.respondButton);
            respondButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.respondButton) {
                        Intent intent = new Intent(getActivity(), AddUpdate.class);
                        intent.putExtra("calling_from", Interfaces.WhoIsCallingUpdateInterface.ADD_UPDATE_TO_RESPOND_TO_REQUEST);
                        intent.putExtra("location_from", currentRequest.getLocationIdFrom())
                                .putExtra("location_to", currentRequest.getLocationIdTo())
                                .putExtra("request_id", currentRequest.getRequestId());
                        startActivity(intent);
                    }
                }
            });

            final Button followButton =(Button)itemView.findViewById(R.id.followButton);

           followButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //can not follow his own post
                    if (v.getId() == R.id.followButton && currentRequest.getRequesterId()!=Utility.CurrentUser.getId()) {
                        handleFollowButtonPress(position,followButton,followCnt);

                    }
                }
           });

            checkIfAlreadyFollowedAndChangeColorAccordingly(mayBeFollower, currentRequest, followButton);


            return itemView;
            // return super.getView(position, convertView, parent);
        }
    }

    /**
     * Modifies the follow button when the page loads, based on whether the current user is already following the request or not.
     *
     * @param curFollower The user for whom request-following is considered currently
     * @param curRequest The request currently in context
     * @param followButton The button allowing a new follower for the request
     */

    private void checkIfAlreadyFollowedAndChangeColorAccordingly(Follower curFollower, Request curRequest, Button followButton) {
        synchronized (curRequest) {
            // Log.d("UpdateId-LikerId-LikeCount", curUpdate.getId() + "-" + curLiker.getLikerId() + "-" + curUpdate.getLikeCount());
//            Log.d("");
            if (curRequest.hasTHeUserFollowedTheRequest(curFollower)) {

                Log.d("InsidefollowButton Color", "Yes");
                followButton.setText("Followed");
                followButton.setBackgroundColor(Color.CYAN);
                followButton.setWidth(50);

            }else{
                followButton.setText("Follow");
                followButton.setBackgroundColor(Color.LTGRAY);
                followButton.setWidth(20);
            }
        }
    }

    /**
     * Handles pressing of follow button during user session.
     *
     * @param pos Position in arraylist of requests
     * @param followButton The button allowing a new follower for the request
     * @param followCount The number of people following the request
     */

    private void handleFollowButtonPress(int pos, Button followButton, TextView followCount) {

        //Log.d("the pressed like button update: ",allUserUpdatesArraylist.get(pos).toString());

        //check if the user has pressed the like button already.if he had,do not do anything.
        //else increseLIkeCountBy one
        Follower curFollower=new Follower(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());
        Request curRequest=allRequestsArrayList.get(pos);
        if(!curRequest.hasTHeUserFollowedTheRequest(curFollower)){
            curRequest.addFollower(curFollower);
           // curRequest.re(curFollower);
            //Log.d("yes liked ", "for the first time");
            followButton.setText("Followed");
            followButton.setBackgroundColor(Color.CYAN);
            //now increase the likeCount by one
            int curFollowCount=curRequest.getFollowerCount();
            followCount.setText("" + curFollowCount);
            requestToBeFollowed=curRequest.getRequestId();
            followerId=Utility.CurrentUser.getId();
            addFollowerTask.execute();

            //populateUpdateListView();


        }else{
            Log.d(" Already followed", "the request");
            curRequest.removeFollower(curFollower);

            removeColorFromFollow(curRequest,followButton,followCount);
            new RemoveFollowerTask().execute("" + curRequest.getRequestId());
        }
    }


//    private void handledislikeButtonPress(int pos,Button dislikeButton) {
//
//        Log.d("pressed dislike button update: ",allUserUpdatesArraylist.get(pos).toString());
//
//        //check if the user has pressed the like button already.if he had,do not do anything.
//        //else increseLIkeCountBy one
//        Voter curDisliker=new Voter(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());
//        Update curUpdate=allUserUpdatesArraylist.get(pos);
//        if(!curUpdate.hasTheUserLikedTheUpdate(curDisliker)){
//            curUpdate.addLiker(curDisliker);
//            curUpdate.removeDisliker(curDisliker);
//
//        }
//    }

    private void removeColorFromFollow(Request curRequest,Button followButton,TextView followText) {
        followButton.setText("Follow");
        followText.setText(""+curRequest.getFollowerCount());
        followButton.setBackgroundColor(Color.LTGRAY);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

        public void callAppropriateDetailedActivity(int idOfTheFragmentToBeCalled, Object object);
        //public void callAppropriateDetailedActivity(int idOfTheFragmentToBeCalled, Object object);
        public int getTheIdOfTheActivityTHeFragmentIsAttachedTo();
        public Request passRequestObject();
    }

    /**
     * Sets the criteria for sorting of the requests in the page.
     *
     * @param sortingCriteria The criteria used to sort the requests in the page.
     */

    public void setRequestSorting (String sortingCriteria) {
        this.sortingCriteria = sortingCriteria;
        Log.d("Sorting Criteria change", "New Background Thread starts");
        fetchRequestTask.execute();
    }

    /**
     * AsyncTask FetchRequestTask runs in background to send a HTTP request and fetch all requests in JSON format from database.
     */

    class FetchRequestTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonRequests;

        @Override
        protected void onPreExecute() {
            progressLayout.setVisibility(View.VISIBLE);
            customRequestListView.setVisibility(View.GONE);
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("sortType", sortingCriteria));

            // getting JSON string from URL

            if(locationIdToSearch==0){
                jsonRequests = jParser.makeHttpRequest("/allrequests", "GET", params);
                Log.d("specific: ","all locationsearch");
            }
            else{
                params.add(new Pair("locationId", locationIdToSearch));
                jsonRequests = jParser.makeHttpRequest("/requestsfromlocation", "GET", params);
                Log.d("specific: ",Locations.getLocationName(locationIdToSearch));
            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){
            progressLayout.setVisibility(View.GONE);
            customRequestListView.setVisibility(View.VISIBLE);

            if(jsonRequests == null) {
                Utility.CurrentUser.showConnectionError(getActivity());
                return;
            }

            //jsonUpdatesField=jsonUpdates;
            populateRequestList(jsonRequests);
            populateRequestListView();
        }
    }

    /**
     * AsyncTask AddFollowerTask runs in background to send a HTTP request to add a new request follower to database.
     */

    class AddFollowerTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonAddRequestFollow;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("requestId",requestToBeFollowed));
            params.add(new Pair("followerId",followerId));

            jsonAddRequestFollow = jParser.makeHttpRequest("/addrequestfollower", "POST", params);

            // Check your log cat for JSON reponse
            // Log.d("All info: ",jsonUpdates.toString());
            return null;

        }

        /**
         * After completing background task
         **/
        protected void onPostExecute (String a){
            if(jsonAddRequestFollow == null) {
                Utility.CurrentUser.showConnectionError(getActivity());
            }
        }
    }


    class RemoveFollowerTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonRemoveFollow;
        private int requestToUpdateFollowFrom;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();
            requestToUpdateFollowFrom =Integer.parseInt(args[0]);

            params.add(new Pair("requestId", requestToUpdateFollowFrom));
            params.add(new Pair("followerId",Utility.CurrentUser.getId()));

            jsonRemoveFollow = jParser.makeHttpRequest("/removerequestfollower", "POST", params);

            // Check your log cat for JSON reponse
            // Log.d("All info: ",jsonUpdates.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){
            if(jsonRemoveFollow == null) {
                Utility.CurrentUser.showConnectionError(getActivity());
            }
        }
    }

}
