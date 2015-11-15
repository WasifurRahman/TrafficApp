package com.example.washab.trafficapp;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        populateAnnouncementList(jsonAnnouncements);
        populateAnnouncementListView();
        super.onActivityCreated(savedInstanceState);
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

            JSONArray allAnnouncements=jsonAnnouncements.getJSONArray("announcements");
            allAnnouncementsArrayList.clear();
            for(int i=0;i<allAnnouncements.length();i++){
                JSONObject curObj=allAnnouncements.getJSONObject(i);
                //Log.d("in populte: ",curObj.toString());
                Announcement curAnnouncment = Announcement.createAnnouncement(curObj);
                //Log.d("new update",curUpdate.toString());
               allAnnouncementsArrayList.add(curAnnouncment);
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

    private class MyListAdapter extends ArrayAdapter<Announcement>{
        public MyListAdapter(){
            super(getActivity(), R.layout.user_announcement_item, allAnnouncementsArrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView=convertView;
            if(itemView==null){
                itemView=getActivity().getLayoutInflater().inflate(R.layout.user_announcement_item,parent,false);
            }


            //find the update to work with
            Announcement currentAnnouncement= allAnnouncementsArrayList.get(position);
            //fill the view
            TextView title = (TextView)itemView.findViewById(R.id.announcementTitleTextView);
            title.setText(currentAnnouncement.getTitle());

            TextView locFrom = (TextView)itemView.findViewById(R.id.announcementLocationFromTextView);
            locFrom.setText(currentAnnouncement.getLocationIdFrom());

            TextView locTo = (TextView)itemView.findViewById(R.id.announcementLocationToTextView);
            locTo.setText(currentAnnouncement.getLocationIdTo());

            TextView timeFrom = (TextView)itemView.findViewById(R.id.announcementTimeFromTextView);
            timeFrom.setText(currentAnnouncement.getTimeFrom());

            TextView timeTo = (TextView)itemView.findViewById(R.id.announcementTimeToTextView);
            timeTo.setText(currentAnnouncement.getTimeTo());

            TextView sitDes=(TextView)itemView.findViewById(R.id.announcementExtraDes);
            sitDes.setText(currentAnnouncement.getDescription());



            TextView updatorName=(TextView) itemView.findViewById(R.id.announcementUpdatorNameTextView);
            updatorName.setText(currentAnnouncement.getPosterName());

            TextView updateTime=(TextView) itemView.findViewById(R.id.announcementUpdateTimeTextView);
            updateTime.setText(currentAnnouncement.getTimeOfUpdate());

            TextView likeCnt=(TextView) itemView.findViewById(R.id.announcementLikeCountTextView);
            likeCnt.setText("" + currentAnnouncement.getLikeCount());

            TextView dislikeCnt=(TextView) itemView.findViewById(R.id.announcementDislikeCountTextView);
            dislikeCnt.setText("" + currentAnnouncement.getDislikeCount());



            return itemView;
            // return super.getView(position, convertView, parent);



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
    }



    class FetchAnnouncementTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonUpdates;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("sortType", sortingCriteria));
            // getting JSON string from URL

            jsonAnnouncements= jParser.makeHttpRequest("/allannouncements", "POST", params);

            // Check your log cat for JSON reponse
            Log.d("All info: ", jsonUpdates.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){

            //jsonUpdatesField=jsonUpdates;
            populateAnnouncementList(jsonAnnouncements);
            populateAnnouncementListView();
        }
    }

}
