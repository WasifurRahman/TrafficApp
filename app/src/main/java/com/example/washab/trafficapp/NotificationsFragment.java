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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<Notification> allNotifsArraylist = new ArrayList<Notification>();
    LinearLayout progressLayout;
    ListView customNotifsListView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private FetchNotificationsTask fetchNotificationsTask = new FetchNotificationsTask();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NotificationsFragment() {
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

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return A new view to display the notifications for the user.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    /**
     * Notifications are fetched from the database as soon as the activity is ready.
     *
     * @param savedInstanceState
     */

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        progressLayout = (LinearLayout) getActivity().findViewById(R.id.progressbar_view);
        customNotifsListView =(ListView)getActivity().findViewById(R.id.userNotifsListView);
        fetchNotificationsTask.execute();

        super.onActivityCreated(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        fetchNotificationsTask.cancel(true);
    }

    /**
     * The notification list is prepared from database information.
     *
     * @param jsonNotifs JSON object containing fetched notifications from database.
     */

    private void populateNotifList(JSONObject jsonNotifs){

        try {
            //Log.d("within updates: ",jsonUpdates.toString());
            allNotifsArraylist.clear();

            JSONArray allNotifsJSONArray=jsonNotifs.getJSONArray("notifications");
            int curIndex=0, N=allNotifsJSONArray.length();

            while(curIndex<N){
                JSONObject curObj=allNotifsJSONArray.getJSONObject(curIndex++);
                Notification curNotif=Notification.createNotification(curObj);
                allNotifsArraylist.add(curNotif);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ListView is prepared for displaying the list of notifications.
     */

    private void populateNotifListView(){
        ArrayAdapter<Notification> adapter = new MyListAdapter();
        ListView list=(ListView)getView().findViewById(R.id.userNotifsListView);
        list.setAdapter(adapter);
    }

    /**
     * Array adapter MyListAdapter accomodates notifications dynamically and fills the list container with proper layout.
     */

    private class MyListAdapter extends ArrayAdapter<Notification>{
        public MyListAdapter(){
            super(getActivity(), R.layout.user_notif_item, allNotifsArraylist);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View itemView=convertView;
            if(itemView==null){
                itemView=getActivity().getLayoutInflater().inflate(R.layout.user_notif_item,parent,false);
            }


            //find the notification to work with
            final Notification currentNotif = allNotifsArraylist.get(position);
            int currentUserId=Utility.CurrentUser.getId();

            //fill the view

            TextView notifText = (TextView)itemView.findViewById(R.id.notifText);

            String notifType = currentNotif.getNotifType();
            String notifFromUsername = currentNotif.getNotifFromUsername();
            String notifAbout = currentNotif.getNotifAbout();

            if(notifType.equals("like")) {
                notifText.setText(notifFromUsername + " liked your " + notifAbout + ".");
            }
            else if(notifType.equals("follow")) {
                notifText.setText(notifFromUsername + " followed your " + notifAbout + ".");
            }
            else if(notifType.equals("request")) {
                notifText.setText(notifFromUsername + " requested an update on a location you are following.");
            }
            else if(notifType.equals("requestResponse")) {
                notifText.setText(notifFromUsername + " responded to your " + notifAbout + ".");
            }
            else if(notifType.equals("followResponse")) {
                notifText.setText(notifFromUsername + " responded to a " + notifAbout + " you are following.");
            }

            return itemView;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * AsyncTask FetchNotificationsTask runs in background to send a HTTP request and fetch all notifications in JSON format from database.
     */

    class FetchNotificationsTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonNotifs;

        @Override
        protected void onPreExecute() {
            progressLayout.setVisibility(View.VISIBLE);
            customNotifsListView.setVisibility(View.GONE);

            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            List<Pair> params = new ArrayList<Pair>();
            params.add(new Pair("userId", Utility.CurrentUser.getId()));

            // getting JSON string from URL

            jsonNotifs = jParser.makeHttpRequest("/allnotifications", "GET", params);

            return null;

        }

        /**
         * After completing background task
         **/
        protected void onPostExecute (String a){

            if(jsonNotifs == null) {
                Utility.CurrentUser.showConnectionError(getActivity());
                return;
            }

            progressLayout.setVisibility(View.GONE);
            customNotifsListView.setVisibility(View.VISIBLE);

            populateNotifList(jsonNotifs);
            populateNotifListView();

        }
    }


}
