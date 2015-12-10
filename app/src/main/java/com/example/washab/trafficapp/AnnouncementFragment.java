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
 * {@link AnnouncementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnnouncementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnnouncementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ArrayList<Announcement> allAnnouncementsArrayList =new ArrayList<Announcement>();
    private String sortingCriteria = "mostRecent";
    JSONObject jsonAnnouncements;
    LinearLayout progressLayout;
    ListView customAnnouncementListView;
    private int locationIdToSearch=0;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnnouncementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnnouncementFragment newInstance(String param1, String param2) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AnnouncementFragment() {
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
        return inflater.inflate(R.layout.fragment_announcement, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAnnouncementFragmentInteraction(uri);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        progressLayout = (LinearLayout) getActivity().findViewById(R.id.progressbar_view);



        if(mListener.getTheIdOfTheActivityTHeFragmentIsAttachedTo()==Interfaces.ToWhichActivityIsTheFragmentAttached.HOME_ACTIVITY) {
            //Log.e("updatesfragment","called by home");

            customAnnouncementListView=(ListView)getActivity().findViewById(R.id.userAnnouncementListView);
            registerCallBack();
            new FetchAnnouncementTask().execute();
        }else{
            progressLayout.setVisibility(View.GONE);


            Announcement currentAnouncement =mListener.passAnnouncementObject();

            populateAnnouncementList(currentAnouncement);
            populateAnnouncementListView();


        }
        super.onActivityCreated(savedInstanceState);
    }

    private void populateAnnouncementList(Announcement currentAnouncement) {
        allAnnouncementsArrayList.clear();
        allAnnouncementsArrayList.add(currentAnouncement);
    }


    private void registerCallBack() {

        ListView lv = (ListView) getActivity().findViewById(R.id.userAnnouncementListView);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.callAppropriateDetailedActivity(Interfaces.WhichFragmentIsCallingDetailedActivity.ANNOUNCEMENT_FRAGMENT, allAnnouncementsArrayList.get(position));

            }
        });
    }

//  private void populateAnnouncementList(){
//
//
//        //String description, int dislikeCount, String timeFrom,String timeTo,int id, int likeCount, String locationFrom, String locationTo, String title,  String timeOfUpdate, String updater)
//        allAnnouncementsArrayList.add(new Announcement("good luck", 2, "12-30 AM", "1:15 PM", 5, 25, "Dhanmondi", "New-market", "Serious blockade", "11:15 AM", "Shabab"));
//        allAnnouncementsArrayList.add(new Announcement("good luck", 3, "12-40 AM", "1:55 PM", 53, 25, "science-lab", "New-market", "PM going", "11:35 AM", "khan"));
//
//
//    }

    private void populateAnnouncementList(JSONObject jsonAnnouncements){




        try {

            JSONArray allAnnouncementsJSONarray =jsonAnnouncements.getJSONArray("posts");
            allAnnouncementsArrayList.clear();

            int curIndex = 0, N = allAnnouncementsJSONarray.length();

            while (curIndex < N) {
                JSONObject curObj = allAnnouncementsJSONarray.getJSONObject(curIndex++);
                Announcement curPost = Announcement.createAnnouncement(curObj);
                int likeCnt = curPost.getLikeCount(),dislikeCnt=curPost.getDislikeCount();
                for (int i = 0; i < likeCnt; i++) {
                    JSONObject likeObj = allAnnouncementsJSONarray.getJSONObject(curIndex++);
                    Voter newLiker = new Voter(likeObj.getInt("likerId"), likeObj.getString("likerName"));
                    curPost.addLikerInitially(newLiker);
                }

                for (int i = 0; i < dislikeCnt; i++) {
                    JSONObject dislikeObj = allAnnouncementsJSONarray.getJSONObject(curIndex++);
                    Voter newLiker = new Voter(dislikeObj.getInt("dislikerId"), dislikeObj.getString("dislikerName"));
                    curPost.addLikerInitially(newLiker);
                }
                allAnnouncementsArrayList.add(curPost);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Discussion(String description, int dislikeCount, int id, int likeCount, String location, String timeOfUpdate, String title, String updater)
        //String description, int dislikeCount, String timeFrom,String timeTo,int id, int likeCount, String locationFrom, String locationTo, String title,  String timeOfUpdate, String updater)
        //allDiscussionsArrayList.add(new Discussion("this road needs to be fixed quickly", 5,2, 25, "Dhanmondi", "11:15 AM","road is broken", "Shabab"));


    }


    private void populateAnnouncementListView(){
        ArrayAdapter<Announcement> adapter = new MyListAdapter();
        ListView list=(ListView)getView().findViewById(R.id.userAnnouncementListView);
        list.setAdapter(adapter);
    }

    public void setAnnouncementSearchLocation(int locationIdToSearch) {
        this.locationIdToSearch=locationIdToSearch;
        new FetchAnnouncementTask().execute();

    }

    private class MyListAdapter extends ArrayAdapter<Announcement>{
        public MyListAdapter(){
            super(getActivity(), R.layout.user_announcement_item, allAnnouncementsArrayList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View itemView=convertView;
            if(itemView==null){
                itemView=getActivity().getLayoutInflater().inflate(R.layout.user_announcement_item,parent,false);
            }


            //find the update to work with
            Announcement currentAnnouncement= allAnnouncementsArrayList.get(position);
            //fill the view
            TextView title = (TextView)itemView.findViewById(R.id.announcementTitleTextView);
            title.setText(currentAnnouncement.getTitle());

            TextView location = (TextView)itemView.findViewById(R.id.announcementLocationTextView);
            location.setText(Locations.getLocationName(currentAnnouncement.getLocationId()));



            TextView source=(TextView)itemView.findViewById(R.id.announcementSourcetextView);
            source.setText(currentAnnouncement.getSource());



            TextView sitDes=(TextView)itemView.findViewById(R.id.announcementExtraDes);
            sitDes.setText(currentAnnouncement.getDescription());



            TextView updatorName=(TextView) itemView.findViewById(R.id.announcementUpdatorNameTextView);
            updatorName.setText(currentAnnouncement.getPosterName());

            TextView updateTime=(TextView) itemView.findViewById(R.id.announcementUpdateTimeTextView);
            String timeOfAnnouncement = Utility.CurrentUser.parsePostTime(currentAnnouncement.getTimeOfUpdate());
            updateTime.setText(timeOfAnnouncement);

            final TextView likeCntTV=(TextView) itemView.findViewById(R.id.announcementLikeCountTextView);
            likeCntTV.setText("" + currentAnnouncement.getLikeCount());

            final TextView dislikeCntTV=(TextView) itemView.findViewById(R.id.announcementDislikeCountTextView);
            dislikeCntTV.setText("" + currentAnnouncement.getDislikeCount());



            final Button likeButton=(Button)itemView.findViewById(R.id.announcementLikeButton);
            final Button dislikeButton= (Button)itemView.findViewById(R.id.announcementDislikeButton);
            Voter curVoter=new Voter(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());


            checkIfAlreadyLikedAndChangeColorAccordingly(curVoter,currentAnnouncement,likeButton);
            checkIfAlreadyDislikedAndChangeColorAccordingly(curVoter, currentAnnouncement, dislikeButton);


            likeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.announcementLikeButton) {
                        Log.d("like button: ", "update like button pressed");
                        handleLikeButtonPress(position, likeButton, likeCntTV, dislikeButton, dislikeCntTV);
                    }
                }
            });

            dislikeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.announcementDislikeButton) {
                        Log.d("dislike button: ", "update dislike button pressed");
                        handledislikeButtonPress(position, dislikeButton, dislikeCntTV, likeButton, likeCntTV);
                    }
                }
            });


            return itemView;
            // return super.getView(position, convertView, parent);



        }
    }

    private void checkIfAlreadyDislikedAndChangeColorAccordingly(Voter curVoter,Announcement curAnnouncement, Button dislikeButton) {
        synchronized (curAnnouncement) {
            // Log.d("UpdateId-LikerId-LikeCount", curUpdate.getId() + "-" + curVoter.getLikerId() + "-" + curUpdate.getLikeCount());
//            Log.d("");
            if (curAnnouncement.hasTheUserDislikedTheDiscussion(curVoter)) {

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

    private void checkIfAlreadyLikedAndChangeColorAccordingly(Voter curVoter, Announcement curAnnouncement, Button likeButton) {
        synchronized (curAnnouncement) {
            // Log.d("UpdateId-LikerId-LikeCount", curUpdate.getId() + "-" + curVoter.getLikerId() + "-" + curUpdate.getLikeCount());
//            Log.d("");
            if (curAnnouncement.hasTheUserLikedTheDiscussion(curVoter)) {

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

        Announcement curAnnouncement=allAnnouncementsArrayList.get(pos);
        if(!curAnnouncement.hasTheUserLikedTheDiscussion(curVoter)){

            if(curAnnouncement.hasTheUserDislikedTheDiscussion(curVoter)){
                curAnnouncement.removeDisliker(curVoter);
                removeColorFromDislike(curAnnouncement, dislikeButton, dislikeCountTextView);
                new RemoveDislikerTask().execute("" + curAnnouncement.getId());

            }
            curAnnouncement.addLiker(curVoter);
            // curUpdate.removeDisliker(curVoter);
            //Log.d("yes liked ", "for the first time");
            likeButton.setText("Liked");
            likeButton.setBackgroundColor(Color.CYAN);
            //now increase the likeCount by one
            int curLikeCount=curAnnouncement.getLikeCount();
            likeCountTextView.setText("" + curLikeCount);
            //updateToBeFoll=curDiscussion.getId();
            //likerId=Utility.CurrentUser.getId();
            new AddLikerTask().execute(curAnnouncement.getId()+"");

            //populateUpdateListView();


        }else{
            curAnnouncement.removeLiker(curVoter);
            removeColorFromLike(curAnnouncement, likeButton, likeCountTextView);
            new RemoveLikerTask().execute("" + curAnnouncement.getId());
            //Log.d(" Already liked ", "the post");

        }
    }

    private void removeColorFromDislike(Announcement curAnnouncement,Button disLikeButton,TextView dislikeText) {
        disLikeButton.setText("Dislike");
        dislikeText.setText("" + curAnnouncement.getDislikeCount());
        disLikeButton.setBackgroundColor(Color.LTGRAY);

    }

    private void removeColorFromLike(Announcement curAnnouncement,Button likeButton,TextView likeText) {
        likeButton.setText("Like");
        likeText.setText("" + curAnnouncement.getLikeCount());
        likeButton.setBackgroundColor(Color.LTGRAY);

    }

    private void handledislikeButtonPress(int pos,Button dislikeButton,TextView dislikeCountTextView,Button likeButton,TextView likeCountTextView) {

        Voter curVoter =new Voter(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());

        Announcement curAnnouncement=allAnnouncementsArrayList.get(pos);
        if(!curAnnouncement.hasTheUserDislikedTheDiscussion(curVoter)){

            if(curAnnouncement.hasTheUserLikedTheDiscussion(curVoter)){
                curAnnouncement.removeLiker(curVoter);
                removeColorFromLike(curAnnouncement, likeButton, likeCountTextView);
                new RemoveLikerTask().execute("" + curAnnouncement.getId());

            }

            curAnnouncement.addDisliker(curVoter);
            //curUpdate.removeLiker(curVoter);
            //Log.d("yes liked ", "for the first time");
            dislikeButton.setText("Disliked");
            dislikeButton.setBackgroundColor(Color.CYAN);
            //now increase the likeCount by one
            int curdisLikeCount=curAnnouncement.getDislikeCount();
            dislikeCountTextView.setText("" + curdisLikeCount);

            new AddDislikerTask().execute(curAnnouncement.getId()+"");

            //populateUpdateListView();


        }else{

            curAnnouncement.removeDisliker(curVoter);
            removeColorFromDislike(curAnnouncement, dislikeButton, dislikeCountTextView);
            new RemoveDislikerTask().execute("" + curAnnouncement.getId());
        }
    }





    public void setAnnouncementSorting (String sortingCriteria) {
        this.sortingCriteria = sortingCriteria;
        new FetchAnnouncementTask().execute();
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
        public void onAnnouncementFragmentInteraction(Uri uri);

        public void callAppropriateDetailedActivity(int idOfTheFragmentToBeCalled, Object object);
        //public void callAppropriateDetailedActivity(int idOfTheFragmentToBeCalled, Object object);
        public int getTheIdOfTheActivityTHeFragmentIsAttachedTo();
        public Announcement passAnnouncementObject();
    }



    class FetchAnnouncementTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonUpdates;

        @Override
        protected void onPreExecute() {
            progressLayout.setVisibility(View.VISIBLE);
            customAnnouncementListView.setVisibility(View.GONE);

            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("sortType", sortingCriteria));
            params.add(new Pair("postType", "announcement"));
            // getting JSON string from URL

            if(locationIdToSearch==0){
                jsonAnnouncements =jParser.makeHttpRequest("/allposts", "GET", params);
            }
            else {
                params.add(new Pair("locationId",locationIdToSearch));
                jsonAnnouncements =jParser.makeHttpRequest("/postsfromlocation", "GET", params);
            }



            // Check your log cat for JSON reponse
            Log.d("All info: ", jsonAnnouncements.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){

            //jsonUpdatesField=jsonUpdates;
            progressLayout.setVisibility(View.GONE);
            customAnnouncementListView.setVisibility(View.VISIBLE);

            populateAnnouncementList(jsonAnnouncements);
            populateAnnouncementListView();
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
