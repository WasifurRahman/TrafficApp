package com.example.washab.trafficapp;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
 * {@link DiscussionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscussionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscussionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ArrayList<Discussion> allDiscussionsArrayList =new ArrayList<Discussion>();
//    private JSONObject jsonDiscussionsField;
    private String sortingCriteria = "mostRecent";
    LinearLayout progressLayout;
    ListView customDiscussionListView;
    private int locationIdToSearch=0;

    FetchDiscussionTask fetchDiscussionTask = new FetchDiscussionTask();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscussionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscussionFragment newInstance(String param1, String param2) {
        DiscussionFragment fragment = new DiscussionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DiscussionFragment() {
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
        return inflater.inflate(R.layout.fragment_discussion, container, false);
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

    public void onActivityCreated(Bundle savedInstanceState) {
        progressLayout = (LinearLayout) getActivity().findViewById(R.id.progressbar_view);
        customDiscussionListView =(ListView)getActivity().findViewById(R.id.userDiscussionListView);
        fetchDiscussionTask.execute();

        super.onActivityCreated(savedInstanceState);
    }


    private void populateDiscussionList(JSONObject jsonDiscussions) {

        try {

            JSONArray allDiscussionsJSONArray = jsonDiscussions.getJSONArray("posts");
            allDiscussionsArrayList.clear();

            int curIndex = 0, N = allDiscussionsJSONArray.length();

            while (curIndex < N) {
                JSONObject curObj = allDiscussionsJSONArray.getJSONObject(curIndex++);
                Discussion curPost = Discussion.createDiscussion(curObj);
                int likeCnt = curPost.getLikeCount(),dislikeCnt=curPost.getDislikeCount();
                for (int i = 0; i < likeCnt; i++) {
                    JSONObject likeObj = allDiscussionsJSONArray.getJSONObject(curIndex++);
                    Voter newLiker = new Voter(likeObj.getInt("likerId"), likeObj.getString("likerName"));
                    curPost.addLikerInitially(newLiker);
                }

                for (int i = 0; i < dislikeCnt; i++) {
                    JSONObject dislikeObj = allDiscussionsJSONArray.getJSONObject(curIndex++);
                    Voter newdisLiker = new Voter(dislikeObj.getInt("dislikerId"), dislikeObj.getString("dislikerName"));
                    curPost.addDisLikerInitially(newdisLiker);
                }
                allDiscussionsArrayList.add(curPost);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateDiscussionListView(){
        ArrayAdapter<Discussion> adapter = new MyListAdapter();

        ListView list=(ListView)getView().findViewById(R.id.userDiscussionListView);
        list.setAdapter(adapter);
    }

    public void setDiscussionSearchLocation(int locationIdToSearch) {
        this.locationIdToSearch=locationIdToSearch;
        new FetchDiscussionTask().execute();
    }

    private class MyListAdapter extends ArrayAdapter<Discussion>{
        public MyListAdapter(){

            super(getActivity(), R.layout.user_discussion_item, allDiscussionsArrayList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View itemView=convertView;
            if(itemView==null){
                itemView=getActivity().getLayoutInflater().inflate(R.layout.user_discussion_item,parent,false);
            }


            //find the update to work with
           Discussion currentDiscussion= allDiscussionsArrayList.get(position);
            //fill the view

            TextView locFrom = (TextView)itemView.findViewById(R.id.discussionLocationTextView);
            locFrom.setText(Locations.getLocationName(currentDiscussion.getLocationId()));

            TextView timeFrom = (TextView)itemView.findViewById(R.id.discussionUpdateTimeTextView);
            timeFrom.setText(currentDiscussion.getTimeOfPost());

            EditText sitDes=(EditText)itemView.findViewById(R.id.discussionDescriptionBox);
            sitDes.setText(currentDiscussion.getDescription());

            final TextView posterName=(TextView) itemView.findViewById(R.id.discussionPosterNameTextView);
            posterName.setText(currentDiscussion.getPosterName());


            final TextView likeCnt=(TextView) itemView.findViewById(R.id.discussionLikeCountTextView);
            likeCnt.setText("" + currentDiscussion.getLikeCount());

            final TextView dislikeCnt=(TextView) itemView.findViewById(R.id.discussionDislikeCountTextView);
            dislikeCnt.setText("" + currentDiscussion.getDislikeCount());

            final Button likeButton=(Button)itemView.findViewById(R.id.discussionLikeButton);
            final Button dislikeButton= (Button)itemView.findViewById(R.id.discussionDislikeButton);
            Voter curVoter=new Voter(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());

            checkIfAlreadyLikedAndChangeColorAccordingly(curVoter,currentDiscussion,likeButton);
            checkIfAlreadyDislikedAndChangeColorAccordingly(curVoter,currentDiscussion,dislikeButton);

            likeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.discussionLikeButton) {
                        Log.d("like button: ", "update like button pressed");
                        handleLikeButtonPress(position,likeButton,likeCnt,dislikeButton,dislikeCnt);
                    }
                }
            });

            dislikeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.discussionDislikeButton) {
                        Log.d("dislike button: ", "update dislike button pressed");
                        handledislikeButtonPress(position, dislikeButton, dislikeCnt, likeButton, likeCnt);
                    }
                }
            });




            return itemView;
            // return super.getView(position, convertView, parent);



        }
    }



    private void checkIfAlreadyDislikedAndChangeColorAccordingly(Voter curVoter, Discussion curDiscussion, Button dislikeButton) {
        synchronized (curDiscussion) {
            // Log.d("UpdateId-LikerId-LikeCount", curUpdate.getId() + "-" + curVoter.getLikerId() + "-" + curUpdate.getLikeCount());
//            Log.d("");
            if (curDiscussion.hasTheUserDislikedTheDiscussion(curVoter)) {

                Log.d("Inside dislikeButton Color", "Yes");
                dislikeButton.setText("Disliked");
                dislikeButton.setBackgroundColor(Color.CYAN);
                dislikeButton.setWidth(50);

            }else{
                dislikeButton.setText("Dislike");
                dislikeButton.setBackgroundColor(Color.LTGRAY);
                dislikeButton.setWidth(20);
            }
        }
    }

    private void checkIfAlreadyLikedAndChangeColorAccordingly(Voter curVoter, Discussion curDiscussion, Button likeButton) {
        synchronized (curDiscussion) {
            // Log.d("UpdateId-LikerId-LikeCount", curUpdate.getId() + "-" + curVoter.getLikerId() + "-" + curUpdate.getLikeCount());
//            Log.d("");
            if (curDiscussion.hasTheUserLikedTheDiscussion(curVoter)) {

                Log.d("Inside likeButton Color", "Yes");
                likeButton.setText("Liked");
                likeButton.setBackgroundColor(Color.CYAN);
                likeButton.setWidth(50);

            }else{
                likeButton.setText("Like");
                likeButton.setBackgroundColor(Color.LTGRAY);
                likeButton.setWidth(20);
            }
        }
    }

    private void handleLikeButtonPress(int pos, Button likeButton, TextView likeCountTextView,Button dislikeButton,TextView dislikeCountTextView) {

        //Log.d("the pressed like button update: ",allUserUpdatesArraylist.get(pos).toString());

        //check if the user has pressed the like button already.if he had,do not do anything.
        //else increseLIkeCountBy one
        Voter curVoter =new Voter(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());
        Discussion curDiscussion=allDiscussionsArrayList.get(pos);
        if(!curDiscussion.hasTheUserLikedTheDiscussion(curVoter)){

            if(curDiscussion.hasTheUserDislikedTheDiscussion(curVoter)){
                curDiscussion.removeDisliker(curVoter);
                removeColorFromDislike(curDiscussion, dislikeButton, dislikeCountTextView);
                new RemoveDislikerTask().execute(""+curDiscussion.getId());

            }
            curDiscussion.addLiker(curVoter);
            // curUpdate.removeDisliker(curVoter);
            //Log.d("yes liked ", "for the first time");
            likeButton.setText("Liked");
            likeButton.setBackgroundColor(Color.CYAN);
            //now increase the likeCount by one
            int curLikeCount=curDiscussion.getLikeCount();
            likeCountTextView.setText("" + curLikeCount);
            //updateToBeFoll=curDiscussion.getId();
            //likerId=Utility.CurrentUser.getId();
            new AddLikerTask().execute(curDiscussion.getId()+"");

            //populateUpdateListView();


        }else{
            curDiscussion.removeLiker(curVoter);
            removeColorFromLike(curDiscussion, likeButton, likeCountTextView);
            new RemoveLikerTask().execute("" + curDiscussion.getId());
            //Log.d(" Already liked ", "the post");

        }
    }

    private void removeColorFromDislike(Discussion curDiscussion,Button disLikeButton,TextView dislikeText) {
        disLikeButton.setText("Dislike");
        dislikeText.setText("" + curDiscussion.getDislikeCount());
        disLikeButton.setBackgroundColor(Color.LTGRAY);

    }

    private void removeColorFromLike(Discussion curDiscussion,Button likeButton,TextView likeText) {
        likeButton.setText("Like");
        likeText.setText("" + curDiscussion.getLikeCount());
        likeButton.setBackgroundColor(Color.LTGRAY);

    }

    private void handledislikeButtonPress(int pos,Button dislikeButton,TextView dislikeCountTextView,Button likeButton,TextView likeCountTextView) {

        Voter curVoter =new Voter(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());
        Discussion curDiscussion=allDiscussionsArrayList.get(pos);
        if(!curDiscussion.hasTheUserDislikedTheDiscussion(curVoter)){

            if(curDiscussion.hasTheUserLikedTheDiscussion(curVoter)){
                curDiscussion.removeLiker(curVoter);
                removeColorFromLike(curDiscussion, likeButton, likeCountTextView);
                new RemoveLikerTask().execute("" + curDiscussion.getId());

            }

            curDiscussion.addDisliker(curVoter);
            //curUpdate.removeLiker(curVoter);
            //Log.d("yes liked ", "for the first time");
            dislikeButton.setText("Disliked");
            dislikeButton.setBackgroundColor(Color.CYAN);
            //now increase the likeCount by one
            int curdisLikeCount=curDiscussion.getDislikeCount();
            dislikeCountTextView.setText("" + curdisLikeCount);

            new AddDislikerTask().execute(curDiscussion.getId()+"");

            //populateUpdateListView();


        }else{

            curDiscussion.removeDisliker(curVoter);
            removeColorFromDislike(curDiscussion, dislikeButton, dislikeCountTextView);
            new RemoveDislikerTask().execute("" + curDiscussion.getId());
        }
    }


    public void setDiscussionSorting (String sortingCriteria) {
        this.sortingCriteria = sortingCriteria;
        new FetchDiscussionTask().execute();
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
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        Log.d("inside pause", "yes");
        fetchDiscussionTask.cancel(true);
    }




    class FetchDiscussionTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonDiscussions;

        @Override
        protected void onPreExecute()
        {
            progressLayout.setVisibility(View.VISIBLE);
            customDiscussionListView.setVisibility(View.GONE);

            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("sortType", sortingCriteria));
            params.add(new Pair("postType", "discussion"));
            // getting JSON string from URL
            if(locationIdToSearch==0){
                jsonDiscussions = jParser.makeHttpRequest("/allposts", "GET", params);
            }
            else {
                params.add(new Pair("locationId",locationIdToSearch));
                jsonDiscussions =jParser.makeHttpRequest("/postsfromlocation", "GET", params);
            }


            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){
            progressLayout.setVisibility(View.GONE);
            customDiscussionListView.setVisibility(View.VISIBLE);

            if(jsonDiscussions == null) {
                Utility.CurrentUser.showConnectionError(getActivity());
                return;
            }

            //jsonUpdatesField=jsonDiscussions;
            populateDiscussionList(jsonDiscussions);
            populateDiscussionListView();
        }
    }



    class AddLikerTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonAddRequestLike;
        private int discussionToBeLiked;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();
            discussionToBeLiked=Integer.parseInt(args[0]);

            params.add(new Pair("postId",discussionToBeLiked));
            params.add(new Pair("likerId",Utility.CurrentUser.getId()));

            jsonAddRequestLike = jParser.makeHttpRequest("/addpostlike", "POST", params);

            // Check your log cat for JSON reponse
            // Log.d("All info: ",jsonUpdates.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){
            if(jsonAddRequestLike == null) {
                Utility.CurrentUser.showConnectionError(getActivity());
            }
        }
    }


    class AddDislikerTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonAddDislikeDiscussion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();
            int discussionToBeLIked=Integer.parseInt(args[0]);

            params.add(new Pair("postId",discussionToBeLIked));
            params.add(new Pair("dislikerId",Utility.CurrentUser.getId()));

            jsonAddDislikeDiscussion = jParser.makeHttpRequest("/addpostdislike", "POST", params);

            // Check your log cat for JSON reponse
            // Log.d("All info: ",jsonUpdates.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){
            if(jsonAddDislikeDiscussion == null) {
                Utility.CurrentUser.showConnectionError(getActivity());
            }
        }
    }


    class RemoveDislikerTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonRemoveDisliker;
        private int discussionToRemoveDislikeFrom;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();
            discussionToRemoveDislikeFrom =Integer.parseInt(args[0]);

            params.add(new Pair("postId", discussionToRemoveDislikeFrom));
            params.add(new Pair("dislikerId",Utility.CurrentUser.getId()));

            jsonRemoveDisliker = jParser.makeHttpRequest("/removepostdislike", "POST", params);

            // Check your log cat for JSON reponse
            // Log.d("All info: ",jsonUpdates.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){
            if(jsonRemoveDisliker == null) {
                Utility.CurrentUser.showConnectionError(getActivity());
            }
        }
    }


    class RemoveLikerTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonRemoveLike;
        private int discussionToRemoveLIkeFrom;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();
            discussionToRemoveLIkeFrom =Integer.parseInt(args[0]);

            params.add(new Pair("postId", discussionToRemoveLIkeFrom));
            params.add(new Pair("likerId",Utility.CurrentUser.getId()));

            jsonRemoveLike = jParser.makeHttpRequest("/removepostlike", "POST", params);

            // Check your log cat for JSON reponse
            // Log.d("All info: ",jsonUpdates.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){
            if(jsonRemoveLike== null) {
                Utility.CurrentUser.showConnectionError(getActivity());
            }
        }
    }

}
