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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends Fragment implements  Interfaces.WhoIsCallingUpdateInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<Update> allUserUpdatesArraylist =new ArrayList<Update>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static Context context;
    private JSONObject jsonUpdates;

    private String queryAllUpdates="/allupdates";
    private String queryAddLike="/addupdatelike";

    private String sortingCriteria = "mostRecent";
    private String query="/allupdates";
    private int updateToBeLiked=-1;

    private int likerId=-1;
    private TextView changeLikeCount;
    private int locationIdToSearch=0;
    LinearLayout progressLayout;
    ListView customUpdateList;
    private int dislikerId;
    private int updateToBeDisliked;


    FetchUpdateTask fetchUpdateTask = new FetchUpdateTask();



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(String param1, String param2) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UpdateFragment() {
        // Required empty public constructor

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        UpdateFragment.context = this.getActivity();
        setHasOptionsMenu(true);
    }



    private void populateUpdateList(JSONObject jsonUpdates){

        try {
            //Log.d("within updates: ",jsonUpdates.toString());
            allUserUpdatesArraylist.clear();

            JSONArray allUpdatesJSONArray=jsonUpdates.getJSONArray("updates");
            int curIndex=0, N=allUpdatesJSONArray.length();

            while(curIndex<N){
                JSONObject curObj=allUpdatesJSONArray.getJSONObject(curIndex++);
                Update curUpdate=Update.createUpdate(curObj);


                int likeCnt=curUpdate.getLikeCount();
                int dislikeCount=curUpdate.getDislikeCount();
                for(int i=0;i<likeCnt;i++){
                    JSONObject likeObj=allUpdatesJSONArray.getJSONObject(curIndex++);
                    Voter newVoter = new Voter(likeObj.getInt("likerId"),likeObj.getString("likerName"));
                    curUpdate.addLikerInitially(newVoter);
                }
                for(int i=0;i<dislikeCount;i++){
                    JSONObject dislikeObj=allUpdatesJSONArray.getJSONObject(curIndex++);
                    Voter newVoter = new Voter(dislikeObj.getInt("dislikerId"),dislikeObj.getString("dislikerName"));
                    curUpdate.addDisLikerInitially(newVoter);
                }


                allUserUpdatesArraylist.add(curUpdate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateUpdateListView(){
        ArrayAdapter<Update> adapter = new MyListAdapter();
        ListView list=(ListView)getView().findViewById(R.id.userUpdatesListView);
        list.setAdapter(adapter);
    }



    private class MyListAdapter extends ArrayAdapter<Update>{
        public MyListAdapter(){
            super(getActivity(), R.layout.user_update_item, allUserUpdatesArraylist);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View itemView=convertView;
            if(itemView==null){
                itemView=getActivity().getLayoutInflater().inflate(R.layout.user_update_item,parent,false);
            }


            //find the update to work with
            final Update currentUpdate= allUserUpdatesArraylist.get(position);
            int currentUserId=Utility.CurrentUser.getId();
            String currentUserName=Utility.CurrentUser.getName();
            Voter mayBeVoter =new Voter(currentUserId,currentUserName);
            //fill the view
            Log.d("check update changes : ", currentUpdate.toString());

            TextView locFrom = (TextView)itemView.findViewById(R.id.locationFromTextView);
            locFrom.setText(Locations.getLocationName(currentUpdate.getLocationIdFrom()));

            TextView locTo = (TextView)itemView.findViewById(R.id.locationToTextView);
            locTo.setText(Locations.getLocationName(currentUpdate.getLocationIdTo()));

            TextView sitDes=(TextView)itemView.findViewById(R.id.situationDesCriptionTextView);
            sitDes.setText(currentUpdate.getSituation());

            TextView estTime=(TextView) itemView.findViewById(R.id.estTimeDescriptorTextView);
            estTime.setText(""+currentUpdate.getEstTimeToCross());

            TextView updatorName=(TextView) itemView.findViewById(R.id.updatorNameTextView);
            updatorName.setText(currentUpdate.getUpdaterName());

            TextView updateTime=(TextView) itemView.findViewById(R.id.updateTimeTextView);
            updateTime.setText(currentUpdate.getTimeOfUpdate());

            EditText description = (EditText) itemView.findViewById(R.id.updateDescriptionBox);
            description.setText(currentUpdate.getDescription());

            final TextView likeCnt=(TextView) itemView.findViewById(R.id.likeCountTextView);
            likeCnt.setText("" + currentUpdate.getLikeCount());

            final TextView dislikeCnt=(TextView) itemView.findViewById(R.id.dislikeCountTextView);
            dislikeCnt.setText("" + currentUpdate.getDislikeCount());

            final Button likeButton=(Button)itemView.findViewById(R.id.updateLikeButton);

            final Button dislikeButton= (Button)itemView.findViewById(R.id.updateDislikeButton);
//            ColorDrawable buttonColor = (ColorDrawable) likeButton.getBackground();
//            Log.d("Button Colour before check", "" + buttonColor.getColor());
            checkIfAlreadyLikedAndChangeColorAccordingly(mayBeVoter, currentUpdate, likeButton);
            checkIfAlreadyDislikedAndChangeColorAccordingly(mayBeVoter, currentUpdate, dislikeButton);
//            Log.d("Button Colour after check", "" + buttonColor.getColor());
            likeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.updateLikeButton) {
                        Log.d("like button: ", "update like button pressed");
                        handleLikeButtonPress(position,likeButton,likeCnt,dislikeButton,dislikeCnt);
                    }
                }
            });

            dislikeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.updateDislikeButton) {
                        Log.d("dislike button: ", "update dislike button pressed");
                        handledislikeButtonPress(position, dislikeButton, dislikeCnt,likeButton,likeCnt);
                    }
                }
            });

            return itemView;
            // return super.getView(position, convertView, parent);



        }
    }

    private void checkIfAlreadyDislikedAndChangeColorAccordingly(Voter curVoter, Update curUpdate, Button dislikeButton) {
        synchronized (curUpdate) {
            // Log.d("UpdateId-LikerId-LikeCount", curUpdate.getId() + "-" + curVoter.getLikerId() + "-" + curUpdate.getLikeCount());
//            Log.d("");
            if (curUpdate.hasTheUserDisLikedTheUpdate(curVoter)) {

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

    private void checkIfAlreadyLikedAndChangeColorAccordingly(Voter curVoter, Update curUpdate, Button likeButton) {
        synchronized (curUpdate) {
           // Log.d("UpdateId-LikerId-LikeCount", curUpdate.getId() + "-" + curVoter.getLikerId() + "-" + curUpdate.getLikeCount());
//            Log.d("");
            if (curUpdate.hasTheUserLikedTheUpdate(curVoter)) {

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
        Update curUpdate=allUserUpdatesArraylist.get(pos);
        if(!curUpdate.hasTheUserLikedTheUpdate(curVoter)){

            if(curUpdate.hasTheUserDisLikedTheUpdate(curVoter)){
                curUpdate.removeDisliker(curVoter);
                removeColorFromDislike(curUpdate, dislikeButton, dislikeCountTextView);
                new RemoveDislikerTask().execute(""+curUpdate.getId());

            }
            curUpdate.addLiker(curVoter);
           // curUpdate.removeDisliker(curVoter);
            //Log.d("yes liked ", "for the first time");
            likeButton.setText("Liked");
            likeButton.setBackgroundColor(Color.CYAN);
            //now increase the likeCount by one
            int curLikeCount=curUpdate.getLikeCount();
            likeCountTextView.setText("" + curLikeCount);
            updateToBeLiked=curUpdate.getId();
            likerId=Utility.CurrentUser.getId();
            new AddLikerTask().execute();

            //populateUpdateListView();


        }else{
            curUpdate.removeLiker(curVoter);
            removeColorFromLike(curUpdate, likeButton, likeCountTextView);
            new RemoveLikerTask().execute(""+curUpdate.getId());
            //Log.d(" Already liked ", "the post");

        }
    }

    private void removeColorFromDislike(Update curUpdate,Button disLikeButton,TextView dislikeText) {
        disLikeButton.setText("Dislike");
        dislikeText.setText(""+curUpdate.getDislikeCount());
        disLikeButton.setBackgroundColor(Color.LTGRAY);

    }

    private void removeColorFromLike(Update curUpdate,Button likeButton,TextView likeText) {
        likeButton.setText("Like");
        likeText.setText(""+curUpdate.getLikeCount());
        likeButton.setBackgroundColor(Color.LTGRAY);

    }

    private void handledislikeButtonPress(int pos,Button dislikeButton,TextView dislikeCountTextView,Button likeButton,TextView likeCountTextView) {

        Voter curVoter =new Voter(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());
        Update curUpdate=allUserUpdatesArraylist.get(pos);
        if(!curUpdate.hasTheUserDisLikedTheUpdate(curVoter)){

            if(curUpdate.hasTheUserLikedTheUpdate(curVoter)){
                curUpdate.removeLiker(curVoter);
                removeColorFromLike(curUpdate, likeButton, likeCountTextView);
                new RemoveLikerTask().execute(""+curUpdate.getId());

            }

            curUpdate.addDisliker(curVoter);
            //curUpdate.removeLiker(curVoter);
            //Log.d("yes liked ", "for the first time");
            dislikeButton.setText("Disliked");
            dislikeButton.setBackgroundColor(Color.CYAN);
            //now increase the likeCount by one
            int curdisLikeCount=curUpdate.getDislikeCount();
            dislikeCountTextView.setText("" + curdisLikeCount);
            updateToBeDisliked= curUpdate.getId();
            dislikerId=Utility.CurrentUser.getId();
            new AddDislikerTask().execute();

            //populateUpdateListView();


        }else{

            curUpdate.removeDisliker(curVoter);
            removeColorFromDislike(curUpdate, dislikeButton, dislikeCountTextView);
            new RemoveDislikerTask().execute(""+curUpdate.getId());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_user_updates, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        progressLayout = (LinearLayout) getActivity().findViewById(R.id.progressbar_view);
        customUpdateList=(ListView)getActivity().findViewById(R.id.userUpdatesListView);

        fetchUpdateTask.execute();
        //populateUpdateList(jsonUpdatesField);
        //populateUpdateListView();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();

        fetchUpdateTask.cancel(true);
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

    public void setUpdateSorting (String sortingCriteria) {
        this.sortingCriteria = sortingCriteria;

        new FetchUpdateTask().execute();
    }

    public void setUpdatesLocation(int locationIdToSearch) {
        this.locationIdToSearch=locationIdToSearch;
        new FetchUpdateTask().execute();
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




    class FetchUpdateTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            progressLayout.setVisibility(View.VISIBLE);
            customUpdateList.setVisibility(View.GONE);
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();


            params.add(new Pair("sortType", sortingCriteria));
            // getting JSON string from URL

            if(locationIdToSearch==0){
                jsonUpdates = jParser.makeHttpRequest("/allupdates", "GET", params);
                Log.d("specific: ","all locationsearch");
            }
            else{
                params.add(new Pair("locationId", locationIdToSearch));
                jsonUpdates = jParser.makeHttpRequest("/updatesfromlocation", "GET", params);
                Log.d("specific: ",Locations.getLocationName(locationIdToSearch));
            }
            //Log.d("specific: ",jsonUpdates.toString());

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){

            //jsonUpdatesField=jsonUpdates;
            progressLayout.setVisibility(View.GONE);
            customUpdateList.setVisibility(View.VISIBLE);

            if(jsonUpdates == null) {
                Utility.CurrentUser.showConnectionError(UpdateFragment.context);
            }
            else {
                // Check log cat for JSON reponse
                Log.d("All info: ",jsonUpdates.toString());

                populateUpdateList(jsonUpdates);
                populateUpdateListView();
            }
        }
    }


    class AddLikerTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonAddUpdateLike;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("updateId",updateToBeLiked));
            params.add(new Pair("likerId",likerId));

            jsonAddUpdateLike = jParser.makeHttpRequest("/addupdatelike", "POST", params);

            // Check your log cat for JSON reponse
            // Log.d("All info: ",jsonUpdates.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){
            if(jsonAddUpdateLike == null) {
                Utility.CurrentUser.showConnectionError(UpdateFragment.context);
            }
        }
    }


    class AddDislikerTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonAddUpdateDislike;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();

            params.add(new Pair("updateId",updateToBeDisliked));
            params.add(new Pair("dislikerId",dislikerId));

            jsonAddUpdateDislike = jParser.makeHttpRequest("/addupdatedislike", "POST", params);

            // Check your log cat for JSON reponse
            // Log.d("All info: ",jsonUpdates.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){
            if(jsonAddUpdateDislike == null) {
                Utility.CurrentUser.showConnectionError(UpdateFragment.context);
            }
        }
    }


    class RemoveDislikerTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonRemoveDisliker;
        private int updateToRemoveDislikeFrom;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();
            updateToRemoveDislikeFrom=Integer.parseInt(args[0]);

            params.add(new Pair("updateId",updateToRemoveDislikeFrom));
            params.add(new Pair("dislikerId",Utility.CurrentUser.getId()));

            jsonRemoveDisliker = jParser.makeHttpRequest("/removeupdatedislike", "POST", params);

            // Check your log cat for JSON reponse
            // Log.d("All info: ",jsonUpdates.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){
            if(jsonRemoveDisliker == null) {
                Utility.CurrentUser.showConnectionError(UpdateFragment.context);
            }
        }
    }


    class RemoveLikerTask extends AsyncTask<String, Void, String> {

        private JSONObject jsonRemoveLike;
        private int updateToREmoveLikeFrom;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();
            updateToREmoveLikeFrom =Integer.parseInt(args[0]);

            params.add(new Pair("updateId", updateToREmoveLikeFrom));
            params.add(new Pair("likerId",Utility.CurrentUser.getId()));

            jsonRemoveLike = jParser.makeHttpRequest("/removeupdatelike", "POST", params);

            // Check your log cat for JSON reponse
            // Log.d("All info: ",jsonUpdates.toString());
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute (String a){
            if(jsonRemoveLike== null) {
                Utility.CurrentUser.showConnectionError(UpdateFragment.context);
            }
        }
    }


}