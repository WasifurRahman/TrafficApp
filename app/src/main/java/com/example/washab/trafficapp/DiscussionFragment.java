package com.example.washab.trafficapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
    private static Context context;

    private AddLikerTask addLikerTask = new AddLikerTask();
    private RemoveLikerTask removeLikerTask = new RemoveLikerTask();
    private AddDislikerTask addDislikerTask = new AddDislikerTask();
    private RemoveDislikerTask removeDislikerTask = new RemoveDislikerTask();

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
        DiscussionFragment.context = this.getActivity();
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
        //customDiscussionListView =(ListView)getActivity().findViewById(R.id.userDiscussionListView);
        //fetchDiscussionTask.execute();

        //progressLayout = (LinearLayout) getActivity().findViewById(R.id.progressbar_view);
        //Log.e("inside updatefragment",mListener.getTheIdOfTheActivityTHeFragmentIsAttachedTo()+" "+Interfaces.ToWhichActivityIsTheFragmentAttached.HOME_ACTIVITY);
        if(mListener.getTheIdOfTheActivityTHeFragmentIsAttachedTo()==Interfaces.ToWhichActivityIsTheFragmentAttached.HOME_ACTIVITY) {
            //Log.e("updatesfragment","called by home");

            customDiscussionListView=(ListView)getActivity().findViewById(R.id.userDiscussionListView);
            registerCallBack();
            if(fetchDiscussionTask.getStatus() == AsyncTask.Status.PENDING || fetchDiscussionTask.getStatus() == AsyncTask.Status.FINISHED || fetchDiscussionTask.isCancelled()) {
                fetchDiscussionTask = new FetchDiscussionTask();
                fetchDiscussionTask.execute();
            }
        }else{
            progressLayout.setVisibility(View.GONE);

            Discussion currentDiscussion=mListener.passDiscussionObject();
            populateDiscussionList(currentDiscussion);
            populateDiscussionListView();


        }

        super.onActivityCreated(savedInstanceState);
    }

    private void populateDiscussionList(Discussion currentDiscussion) {
        allDiscussionsArrayList.clear();
        allDiscussionsArrayList.add(currentDiscussion);
    }

    /**
     * The discussion list is prepared from database information.
     *
     * @param jsonDiscussions A JSON object containing info of discussions from database.
     */

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

    /**
     * ListView is prepared for displaying the list of updates.
     */

    private void populateDiscussionListView(){
        if(allDiscussionsArrayList != null) {
            ArrayAdapter<Discussion> adapter = new MyListAdapter();

            View v;
            if((v = getView()) != null) {
                ListView list=(ListView)v.findViewById(R.id.userDiscussionListView);
                //list.setOnItemClickListener();
                list.setAdapter(adapter);
            }
        }
    }

    private void registerCallBack() {

        ListView lv = (ListView) getActivity().findViewById(R.id.userDiscussionListView);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.callAppropriateDetailedActivity(Interfaces.WhichFragmentIsCallingDetailedActivity.DISCUSSION_FRAGMENT, allDiscussionsArrayList.get(position));

            }
        });
    }

    public void setDiscussionSearchLocation(int locationIdToSearch) {
        this.locationIdToSearch=locationIdToSearch;
        if(fetchDiscussionTask.getStatus() == AsyncTask.Status.PENDING || fetchDiscussionTask.getStatus() == AsyncTask.Status.FINISHED || fetchDiscussionTask.isCancelled()) {
            fetchDiscussionTask = new FetchDiscussionTask();
            fetchDiscussionTask.execute();
        }
    }

    /**
     * Array adapter MyListAdapter accomodates discussions dynamically and fills the list container with proper layout.
     */

    private class MyListAdapter extends ArrayAdapter<Discussion>{
        public MyListAdapter(){

            super(DiscussionFragment.context, R.layout.user_discussion_item, allDiscussionsArrayList);
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
            if(currentDiscussion.getLocationId() != 0) {
                locFrom.setText("#" + Locations.getLocationName(currentDiscussion.getLocationId()));
                locFrom.setTextColor(Color.parseColor("#197502"));
            }

            TextView timeFrom = (TextView)itemView.findViewById(R.id.discussionUpdateTimeTextView);
            String timeOfPost = Utility.CurrentUser.parsePostTime(currentDiscussion.getTimeOfPost());
            timeFrom.setText(timeOfPost);

            EditText sitDes=(EditText)itemView.findViewById(R.id.discussionDescriptionBox);
            sitDes.setText(currentDiscussion.getDescription());

            final TextView posterName=(TextView) itemView.findViewById(R.id.discussionPosterNameTextView);
            posterName.setText(currentDiscussion.getPosterName());


            final TextView likeCnt=(TextView) itemView.findViewById(R.id.discussionLikeCountTextView);
            if(currentDiscussion.getLikeCount() == 1) {
                likeCnt.setText("" + currentDiscussion.getLikeCount() + " like");
            }
            else likeCnt.setText("" + currentDiscussion.getLikeCount() + " likes");

            final TextView dislikeCnt=(TextView) itemView.findViewById(R.id.discussionDislikeCountTextView);
            if(currentDiscussion.getDislikeCount() == 1)
                dislikeCnt.setText("" + currentDiscussion.getDislikeCount() + " dislike");
            else
                dislikeCnt.setText("" + currentDiscussion.getDislikeCount() + " dislikes");

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
        }
    }

    /**
     * Modifies the dislike button when the page loads, based on whether the current user has already disliked a discussion or not.
     * @param curVoter The user for whom discussion-voting is considered currently
     * @param curDiscussion The discussion currently in context
     * @param dislikeButton The button allowing a new dislike for the discussion
     */

    private void checkIfAlreadyDislikedAndChangeColorAccordingly(Voter curVoter, Discussion curDiscussion, Button dislikeButton) {
        synchronized (curDiscussion) {
            // Log.d("UpdateId-LikerId-LikeCount", curUpdate.getId() + "-" + curVoter.getLikerId() + "-" + curUpdate.getLikeCount());
//            Log.d("");
            if (curDiscussion.hasTheUserDislikedTheDiscussion(curVoter)) {

                Log.d("Inside dislikeButton Color", "Yes");
                dislikeButton.setText("Disliked");
                dislikeButton.setTextColor(Color.WHITE);
                dislikeButton.setBackgroundColor(Color.parseColor("#521006"));
                dislikeButton.setWidth(50);

            }else{
                dislikeButton.setText("Dislike");
                dislikeButton.setTextColor(Color.BLACK);
                dislikeButton.setBackgroundColor(Color.LTGRAY);
                dislikeButton.setWidth(20);
            }
        }
    }

    /**
     * Modifies the like button when the page loads, based on whether the current user has already liked a discussion or not.
     * @param curVoter The user for whom update-voting is considered currently
     * @param curDiscussion The discussion currently in context
     * @param likeButton The button allowing a new like for the discussion
     */

    private void checkIfAlreadyLikedAndChangeColorAccordingly(Voter curVoter, Discussion curDiscussion, Button likeButton) {
        synchronized (curDiscussion) {
            // Log.d("UpdateId-LikerId-LikeCount", curUpdate.getId() + "-" + curVoter.getLikerId() + "-" + curUpdate.getLikeCount());
//            Log.d("");
            if (curDiscussion.hasTheUserLikedTheDiscussion(curVoter)) {

                Log.d("Inside likeButton Color", "Yes");
                likeButton.setText("Liked");
                likeButton.setTextColor(Color.WHITE);
                likeButton.setBackgroundColor(Color.parseColor("#034513"));
                likeButton.setWidth(50);

            }else{
                likeButton.setText("Like");
                likeButton.setTextColor(Color.BLACK);
                likeButton.setBackgroundColor(Color.LTGRAY);
                likeButton.setWidth(20);
            }
        }
    }

    /**
     * Handles pressing of like button during user session. Removes 'dislike' and adds 'like' if it was 'disliked' by user before.
     *
     * @param pos Position in arraylist of updates
     * @param likeButton The button allowing a new liker for the discussions
     * @param likeCountTextView The number of people liking the discussion
     * @param dislikeButton The button allowing a new disliker for the discussions
     * @param dislikeCountTextView The number of people disliking the discussion
     */

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
                if(removeDislikerTask.getStatus() == AsyncTask.Status.FINISHED || removeDislikerTask.getStatus() == AsyncTask.Status.PENDING) {
                    removeDislikerTask = new RemoveDislikerTask();
                    removeDislikerTask.execute(""+curDiscussion.getId());
                }

            }
            curDiscussion.addLiker(curVoter);
            // curUpdate.removeDisliker(curVoter);
            //Log.d("yes liked ", "for the first time");
            likeButton.setText("Liked");
//            likeButton.setBackgroundColor(Color.CYAN);
            likeButton.setTextColor(Color.WHITE);
            likeButton.setBackgroundColor(Color.parseColor("#034513"));
            //now increase the likeCount by one
//            int curLikeCount=curDiscussion.getLikeCount();
//            likeCountTextView.setText("" + curLikeCount);
            if(curDiscussion.getLikeCount() == 1) {
                likeCountTextView.setText("" + curDiscussion.getLikeCount() + " like");
            }
            else likeCountTextView.setText("" + curDiscussion.getLikeCount() + " likes");
            //updateToBeFoll=curDiscussion.getId();
            //likerId=Utility.CurrentUser.getId();
            if(addLikerTask.getStatus() == AsyncTask.Status.FINISHED || addLikerTask.getStatus() == AsyncTask.Status.PENDING) {
                addLikerTask = new AddLikerTask();
                addLikerTask.execute(curDiscussion.getId()+"");
            }

            //populateUpdateListView();


        }else{
            curDiscussion.removeLiker(curVoter);
            removeColorFromLike(curDiscussion, likeButton, likeCountTextView);
            if(removeLikerTask.getStatus() == AsyncTask.Status.FINISHED || removeLikerTask.getStatus() == AsyncTask.Status.PENDING) {
                removeLikerTask = new RemoveLikerTask();
                removeLikerTask.execute("" + curDiscussion.getId());
            }
            //Log.d(" Already liked ", "the post");

        }
    }

    /**
     * Changes color of dislike button.
     *
     * @param curDiscussion The discussion currently in context
     * @param disLikeButton The button allowing a new disliker for the discussion
     * @param dislikeText The text showing dislike/disliked on the button
     */

    private void removeColorFromDislike(Discussion curDiscussion,Button disLikeButton,TextView dislikeText) {
        disLikeButton.setText("Dislike");
//        dislikeText.setText("" + curDiscussion.getDislikeCount());
        if(curDiscussion.getDislikeCount() == 1) {
            dislikeText.setText("" + curDiscussion.getDislikeCount() + " dislike");
        }
        else dislikeText.setText("" + curDiscussion.getDislikeCount() + " dislikes");
        disLikeButton.setTextColor(Color.BLACK);
        disLikeButton.setBackgroundColor(Color.LTGRAY);

    }

    /**
     * Changes color of like button.
     *
     * @param curDiscussion The discussion currently in context
     * @param likeButton The button allowing a new liker for the discussion
     * @param likeText The text showing like/liked on the button
     */

    private void removeColorFromLike(Discussion curDiscussion,Button likeButton,TextView likeText) {
        likeButton.setText("Like");
//        likeText.setText("" + curDiscussion.getLikeCount());
        if(curDiscussion.getLikeCount() == 1) {
            likeText.setText("" + curDiscussion.getLikeCount() + " like");
        }
        else likeText.setText("" + curDiscussion.getLikeCount() + " likes");
        likeButton.setTextColor(Color.BLACK);
        likeButton.setBackgroundColor(Color.LTGRAY);

    }

    /**
     * Handles pressing of dislike button during user session. Removes 'like' and adds 'dislike' if it was 'liked' by user before.
     *
     * @param pos Position in arraylist of updates
     * @param likeButton The button allowing a new liker for the discussion
     * @param likeCountTextView The number of people liking the discussion
     * @param dislikeButton The button allowing a new disliker for the discussion
     * @param dislikeCountTextView The number of people disliking the discussion
     */

    private void handledislikeButtonPress(int pos,Button dislikeButton,TextView dislikeCountTextView,Button likeButton,TextView likeCountTextView) {

        Voter curVoter =new Voter(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());
        Discussion curDiscussion=allDiscussionsArrayList.get(pos);
        if(!curDiscussion.hasTheUserDislikedTheDiscussion(curVoter)){

            if(curDiscussion.hasTheUserLikedTheDiscussion(curVoter)){
                curDiscussion.removeLiker(curVoter);
                removeColorFromLike(curDiscussion, likeButton, likeCountTextView);
                if(removeLikerTask.getStatus() == AsyncTask.Status.FINISHED || removeLikerTask.getStatus() == AsyncTask.Status.PENDING) {
                    removeLikerTask = new RemoveLikerTask();
                    removeLikerTask.execute("" + curDiscussion.getId());
                }

            }

            curDiscussion.addDisliker(curVoter);
            //curUpdate.removeLiker(curVoter);
            //Log.d("yes liked ", "for the first time");
            dislikeButton.setText("Disliked");
//            dislikeButton.setBackgroundColor(Color.CYAN);
            dislikeButton.setTextColor(Color.WHITE);
            dislikeButton.setBackgroundColor(Color.parseColor("#521006"));
            //now increase the likeCount by one
//            int curdisLikeCount=curDiscussion.getDislikeCount();
//            dislikeCountTextView.setText("" + curdisLikeCount);
            if(curDiscussion.getDislikeCount() == 1) {
                dislikeCountTextView.setText("" + curDiscussion.getDislikeCount() + " dislike");
            }
            else dislikeCountTextView.setText("" + curDiscussion.getDislikeCount() + " dislikes");

            if(addDislikerTask.getStatus() == AsyncTask.Status.FINISHED || addDislikerTask.getStatus() == AsyncTask.Status.PENDING) {
                addDislikerTask = new AddDislikerTask();
                addDislikerTask.execute(curDiscussion.getId() + "");
            }

            //populateUpdateListView();


        }else{

            curDiscussion.removeDisliker(curVoter);
            removeColorFromDislike(curDiscussion, dislikeButton, dislikeCountTextView);
            if(removeDislikerTask.getStatus() == AsyncTask.Status.FINISHED || removeDislikerTask.getStatus() == AsyncTask.Status.PENDING) {
                removeDislikerTask = new RemoveDislikerTask();
                removeDislikerTask.execute(""+curDiscussion.getId());
            }
        }
    }

    /**
     * Sets the criteria for sorting of the discussions in the page.
     *
     * @param sortingCriteria The criteria used to sort the discussions in the page.
     */


    public void setDiscussionSorting (String sortingCriteria) {
        this.sortingCriteria = sortingCriteria;
        if(fetchDiscussionTask.getStatus() == AsyncTask.Status.PENDING || fetchDiscussionTask.getStatus() == AsyncTask.Status.FINISHED || fetchDiscussionTask.isCancelled()) {
            fetchDiscussionTask = new FetchDiscussionTask();
            fetchDiscussionTask.execute();
        }
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
        public Discussion passDiscussionObject();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        Log.d("inside Disc pause", "yes");
        Utility.CurrentUser.insideHomePause++;
        if(Utility.CurrentUser.insideHomePause >= 3) {
            Log.d("insidePause count", Utility.CurrentUser.insideHomePause+"");
            Utility.CurrentUser.insideHomePause = 0;
            fetchDiscussionTask.cancel(true);
//            Utility.CurrentUser.fetchUpdateTaskRunning = false;
        }
    }


    /**
     * AsyncTask FetchDiscussionTask runs in background to send a HTTP request and fetch all discussions in JSON format from database.
     */

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

    /**
     * AsyncTask AddLikerTask runs in background to send a HTTP request to add a new discussion-liker to database.
     */

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

    /**
     * AsyncTask AddDislikerTask runs in background to send a HTTP request to add a new discussion-disliker to database.
     */


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

    /**
     * AsyncTask RemoveDislikerTask runs in background to send a HTTP request to remove discussion-disliker from database.
     */

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

    /**
     * AsyncTask RemovelikerTask runs in background to send a HTTP request to remove discussion-liker from database.
     */

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
