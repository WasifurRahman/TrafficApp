package com.example.washab.trafficapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
public class UpdateFragment extends android.support.v4.app.Fragment implements  Interfaces.WhoIsCallingUpdateInterface,Interfaces.ToWhichActivityIsTheFragmentAttached{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<Update> allUserUpdatesArraylist =new ArrayList<Update>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //private OnFragmentInteractionListenerForDetailedActivity mListenerDetail;

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

    private enum Situations {
        Free, Mild, Moderate, Extreme, Gridlock
    }

    private AddLikerTask addLikerTask = new AddLikerTask();
    private RemoveLikerTask removeLikerTask = new RemoveLikerTask();
    private AddDislikerTask addDislikerTask = new AddDislikerTask();
    private RemoveDislikerTask removeDislikerTask = new RemoveDislikerTask();
    private FetchUpdateTask fetchUpdateTask = new FetchUpdateTask();

    private TextView updateFragmentTextView;
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

    /**
     * The update list is prepared from database information.
     *
     * @param jsonUpdates A JSON object containing info of updates from database.
     */

    private void populateUpdateList(JSONObject jsonUpdates){

        try {
            Log.d("within updates: ",jsonUpdates.toString());
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

    /**
     * ListView is prepared for displaying the list of updates.
     */

    private void populateUpdateListView(){
        if(allUserUpdatesArraylist != null) {
            ArrayAdapter<Update> adapter = new MyListAdapter();
            View v;
            if((v = getView()) != null) {
                ListView list=(ListView)v.findViewById(R.id.userUpdatesListView);
                //list.setOnItemClickListener();
                list.setAdapter(adapter);
            }

        }

    }


    private void registerCallBack(){

        ListView lv=(ListView)getActivity().findViewById(R.id.userUpdatesListView);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.callAppropriateDetailedActivity(Interfaces.WhichFragmentIsCallingDetailedActivity.UPDATE_FRAGMENT, allUserUpdatesArraylist.get(position));

            }
        });
      /*
        lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "pressed listView", Toast.LENGTH_LONG).show();
            }
        });
        */

    }




    /**
     * Array adapter MyListAdapter accomodates updates dynamically and fills the list container with proper layout.
     */


    private class MyListAdapter extends ArrayAdapter<Update>{
        public MyListAdapter(){
            super(UpdateFragment.context, R.layout.user_update_item, allUserUpdatesArraylist);
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
            locFrom.setTextColor(Color.BLACK);

            TextView locTo = (TextView)itemView.findViewById(R.id.locationToTextView);
            locTo.setText(Locations.getLocationName(currentUpdate.getLocationIdTo()));
            locTo.setTextColor(Color.BLACK);

            TextView sitDes=(TextView)itemView.findViewById(R.id.situationTextView);
            sitDes.setText(currentUpdate.getSituation());

            switch (Situations.valueOf(currentUpdate.getSituation())) {
                case Free:
                    sitDes.setTextColor(Color.parseColor("#188F00"));
                    break;
                case Mild:
                    sitDes.setTextColor(Color.parseColor("#ADCC12"));
                    break;
                case Moderate:
                    sitDes.setTextColor(Color.parseColor("#CCBD12"));
                    break;
                case Extreme:
                    sitDes.setTextColor(Color.parseColor("#CC6012"));
                    break;
                case Gridlock:
                    sitDes.setTextColor(Color.parseColor("#CC2B12"));
                    break;
            }

            TextView estTime=(TextView) itemView.findViewById(R.id.estTimeDescriptorTextView);
            estTime.setText("~ "+currentUpdate.getEstTimeToCross() + " min");

            TextView updaterName=(TextView) itemView.findViewById(R.id.updaterNameTextView);
            updaterName.setText(currentUpdate.getUpdaterName());

//            String timeOfUpdate = itemView.findViewById(R.id.updateTimeTextView).toString();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//            TextView updateTime = (TextView) itemView.findViewById(R.id.updateTimeTextView);
//            updateTime.setText(dateFormat.parse(timeOfUpdate, new ParsePosition(11)).toString());

            TextView updateTime=(TextView) itemView.findViewById(R.id.updateTimeTextView);
//            updateTime.setText(currentUpdate.getTimeOfUpdate());
            String timeOfUpdate = Utility.CurrentUser.parsePostTime(currentUpdate.getTimeOfUpdate());
            updateTime.setText(timeOfUpdate);

            EditText description = (EditText) itemView.findViewById(R.id.updateDescriptionBox);
            description.setText(currentUpdate.getDescription());
            description.setMovementMethod(ScrollingMovementMethod.getInstance());

            final TextView likeCnt=(TextView) itemView.findViewById(R.id.likeCountTextView);
            if(currentUpdate.getLikeCount() == 1) {
                likeCnt.setText("" + currentUpdate.getLikeCount() + " like");
            }
            else likeCnt.setText("" + currentUpdate.getLikeCount() + " likes");

            final TextView dislikeCnt=(TextView) itemView.findViewById(R.id.dislikeCountTextView);
            if(currentUpdate.getDislikeCount() == 1)
                dislikeCnt.setText("" + currentUpdate.getDislikeCount() + " dislike");
            else
                dislikeCnt.setText("" + currentUpdate.getDislikeCount() + " dislikes");

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

    /**
     * Modifies the dislike button when the page loads, based on whether the current user has already disliked an update or not.
     * @param curVoter The user for whom update-voting is considered currently
     * @param curUpdate The update currently in context
     * @param dislikeButton The button allowing a new dislike for the update
     */

    private void checkIfAlreadyDislikedAndChangeColorAccordingly(Voter curVoter, Update curUpdate, Button dislikeButton) {
        synchronized (curUpdate) {
            // Log.d("UpdateId-LikerId-LikeCount", curUpdate.getId() + "-" + curVoter.getLikerId() + "-" + curUpdate.getLikeCount());
//            Log.d("");
            if (curUpdate.hasTheUserDisLikedTheUpdate(curVoter)) {

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
     * Modifies the like button when the page loads, based on whether the current user has already liked an update or not.
     * @param curVoter The user for whom update-voting is considered currently
     * @param curUpdate The update currently in context
     * @param likeButton The button allowing a new like for the update
     */

    private void checkIfAlreadyLikedAndChangeColorAccordingly(Voter curVoter, Update curUpdate, Button likeButton) {
        synchronized (curUpdate) {
           // Log.d("UpdateId-LikerId-LikeCount", curUpdate.getId() + "-" + curVoter.getLikerId() + "-" + curUpdate.getLikeCount());
//            Log.d("");
            if (curUpdate.hasTheUserLikedTheUpdate(curVoter)) {

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
     * @param likeButton The button allowing a new liker for the updates
     * @param likeCountTextView The number of people liking the update
     * @param dislikeButton The button allowing a new disliker for the updates
     * @param dislikeCountTextView The number of people disliking the update
     */

    private void handleLikeButtonPress(int pos, Button likeButton, TextView likeCountTextView,Button dislikeButton,TextView dislikeCountTextView) {


//        Log.d("the pressed like button update: ",allUserUpdatesArraylist.get(pos).toString());

        //check if the user has pressed the like button already.if he had,do not do anything.
        //else increseLIkeCountBy one
        Voter curVoter =new Voter(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());
        Update curUpdate=allUserUpdatesArraylist.get(pos);

        if(!curUpdate.hasTheUserLikedTheUpdate(curVoter)){

            if(curUpdate.hasTheUserDisLikedTheUpdate(curVoter)){
                curUpdate.removeDisliker(curVoter);
                removeColorFromDislike(curUpdate, dislikeButton, dislikeCountTextView);
                if(removeDislikerTask.getStatus() == AsyncTask.Status.FINISHED || removeDislikerTask.getStatus() == AsyncTask.Status.PENDING) {
                    removeDislikerTask = new RemoveDislikerTask();
                    removeDislikerTask.execute(""+curUpdate.getId());
                }

            }
            curUpdate.addLiker(curVoter);
           // curUpdate.removeDisliker(curVoter);
            //Log.d("yes liked ", "for the first time");
            likeButton.setText("Liked");
            likeButton.setTextColor(Color.WHITE);
            likeButton.setBackgroundColor(Color.parseColor("#034513"));
            //now increase the likeCount by one
            int curLikeCount=curUpdate.getLikeCount();
            if(curLikeCount == 1) {
                likeCountTextView.setText("" + curLikeCount + " like");
            }
            else likeCountTextView.setText("" + curLikeCount + " likes");
            updateToBeLiked=curUpdate.getId();
            likerId=Utility.CurrentUser.getId();
            if(addLikerTask.getStatus() == AsyncTask.Status.FINISHED || addLikerTask.getStatus() == AsyncTask.Status.PENDING) {
                addLikerTask = new AddLikerTask();
                addLikerTask.execute();
            }

//            populateUpdateListView();


        }else{
            curUpdate.removeLiker(curVoter);
            removeColorFromLike(curUpdate, likeButton, likeCountTextView);
            if(removeLikerTask.getStatus() == AsyncTask.Status.FINISHED || removeLikerTask.getStatus() == AsyncTask.Status.PENDING) {
                removeLikerTask = new RemoveLikerTask();
                removeLikerTask.execute("" + curUpdate.getId());
            }
            //Log.d(" Already liked ", "the post");

        }
    }

    /**
     * Changes color of dislike button.
     *
     * @param curUpdate The update currently in context
     * @param disLikeButton The button allowing a new disliker for the update
     * @param dislikeText The text showing dislike/disliked on the button
     */

    private void removeColorFromDislike(Update curUpdate,Button disLikeButton,TextView dislikeText) {
        disLikeButton.setText("Dislike");
        disLikeButton.setTextColor(Color.BLACK);
        if(curUpdate.getDislikeCount() == 1) {
            dislikeText.setText("" + curUpdate.getDislikeCount() + " dislike");
        }
        else dislikeText.setText("" + curUpdate.getDislikeCount() + " dislikes");
        disLikeButton.setBackgroundColor(Color.LTGRAY);

    }
    /**
     * Changes color of like button.
     *
     * @param curUpdate The update currently in context
     * @param likeButton The button allowing a new liker for the updates
     * @param likeText The text showing like/liked on the button
     */

    private void removeColorFromLike(Update curUpdate,Button likeButton,TextView likeText) {
        likeButton.setText("Like");
        likeButton.setTextColor(Color.BLACK);
        if(curUpdate.getLikeCount() == 1) {
            likeText.setText("" + curUpdate.getLikeCount() + " like");
        }
        else likeText.setText("" + curUpdate.getLikeCount() + " likes");
        likeButton.setBackgroundColor(Color.LTGRAY);

    }

    /**
     * Handles pressing of dislike button during user session. Removes 'like' and adds 'dislike' if it was 'liked' by user before.
     *
     * @param pos Position in arraylist of updates
     * @param likeButton The button allowing a new liker for the updates
     * @param likeCountTextView The number of people liking the update
     * @param dislikeButton The button allowing a new disliker for the updates
     * @param dislikeCountTextView The number of people disliking the update
     */

    private void handledislikeButtonPress(int pos,Button dislikeButton,TextView dislikeCountTextView,Button likeButton,TextView likeCountTextView) {

        Voter curVoter =new Voter(Utility.CurrentUser.getId(),Utility.CurrentUser.getName());
        Update curUpdate=allUserUpdatesArraylist.get(pos);
        if(!curUpdate.hasTheUserDisLikedTheUpdate(curVoter)){

            if(curUpdate.hasTheUserLikedTheUpdate(curVoter)){
                curUpdate.removeLiker(curVoter);
                removeColorFromLike(curUpdate, likeButton, likeCountTextView);
                if(removeLikerTask.getStatus() == AsyncTask.Status.FINISHED || removeLikerTask.getStatus() == AsyncTask.Status.PENDING) {
                    removeLikerTask = new RemoveLikerTask();
                    removeLikerTask.execute("" + curUpdate.getId());
                }

            }

            curUpdate.addDisliker(curVoter);
            //curUpdate.removeLiker(curVoter);
            //Log.d("yes liked ", "for the first time");
            dislikeButton.setText("Disliked");
            dislikeButton.setTextColor(Color.WHITE);
            dislikeButton.setBackgroundColor(Color.parseColor("#521006"));
            //now increase the likeCount by one
            int curdisLikeCount=curUpdate.getDislikeCount();
            if(curdisLikeCount == 1) {
                dislikeCountTextView.setText("" + curdisLikeCount + " dislike");
            }
            else dislikeCountTextView.setText("" + curdisLikeCount + " dislikes");
            updateToBeDisliked= curUpdate.getId();
            dislikerId=Utility.CurrentUser.getId();
            if(addDislikerTask.getStatus() == AsyncTask.Status.FINISHED || addDislikerTask.getStatus() == AsyncTask.Status.PENDING) {
                addDislikerTask = new AddDislikerTask();
                addDislikerTask.execute();
            }

            //populateUpdateListView();


        }else{

            curUpdate.removeDisliker(curVoter);
            removeColorFromDislike(curUpdate, dislikeButton, dislikeCountTextView);
            if(removeDislikerTask.getStatus() == AsyncTask.Status.FINISHED || removeDislikerTask.getStatus() == AsyncTask.Status.PENDING) {
                removeDislikerTask = new RemoveDislikerTask();
                removeDislikerTask.execute(""+curUpdate.getId());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user_updates, container, false);
        /*
        view.setOnTouchListener(new Interfaces.OnSwipeTouchListener(UpdateFragment.context) {
            @Override
            public void onSwipeRight() {
//                Home.removeAddedFragment(null);
//                addPostTypeFragment();
//                startDiscussionFragment(    );
            }
        });*/

        if(updateFragmentTextView == null)
            updateFragmentTextView = new TextView(getActivity());
        updateFragmentTextView = (TextView) view.findViewById(R.id.updatesButton);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        progressLayout = (LinearLayout) getActivity().findViewById(R.id.progressbar_view);
        //Log.e("inside updatefragment",mListener.getTheIdOfTheActivityTHeFragmentIsAttachedTo()+" "+Interfaces.ToWhichActivityIsTheFragmentAttached.HOME_ACTIVITY);
        if(mListener.getTheIdOfTheActivityTHeFragmentIsAttachedTo()==Interfaces.ToWhichActivityIsTheFragmentAttached.HOME_ACTIVITY) {
            //Log.e("updatesfragment","called by home");

            customUpdateList=(ListView)getActivity().findViewById(R.id.userUpdatesListView);
            registerCallBack();
            if(fetchUpdateTask.getStatus() == AsyncTask.Status.PENDING || fetchUpdateTask.getStatus() == AsyncTask.Status.FINISHED || fetchUpdateTask.isCancelled()) {
                fetchUpdateTask = new FetchUpdateTask();
                fetchUpdateTask.execute();
            }
        }else{
            progressLayout.setVisibility(View.GONE);
            Update currentUpdate=mListener.passUpdateObject();
            populateUpdateList(currentUpdate);
            populateUpdateListView();


        }
        //populateUpdateList(jsonUpdatesField);
        //populateUpdateListView();
        super.onActivityCreated(savedInstanceState);
    }

    private void populateUpdateList(Update update) {
        allUserUpdatesArraylist.clear();
        allUserUpdatesArraylist.add(update);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("inside UpdateFrag pause", "hmm");

        Utility.CurrentUser.insideHomePause++;
        if(Utility.CurrentUser.insideHomePause >= 3) {
            Log.d("insidePause count", Utility.CurrentUser.insideHomePause+"");
            Utility.CurrentUser.insideHomePause = 0;
            fetchUpdateTask.cancel(true);
            Utility.CurrentUser.fetchUpdateTaskRunning = false;
        }

        //fetchDiscussionTask.cancel(true);

        //fetchDiscussionTask.cancel(true);
        //addLikerTask.cancel(true);

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
            //if this exception is called,it means that teh fragment is attached to detailed activity;
            //mListenerDetail=(OnFragmentInteractionListenerForDetailedActivity)activity;
            //so remove the exception for now

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
     * Sets the criteria for sorting of the updates in the page.
     *
     * @param sortingCriteria The criteria used to sort the updates in the page.
     */

    public void setUpdateSorting (String sortingCriteria) {
        this.sortingCriteria = sortingCriteria;

        if(fetchUpdateTask.getStatus() == AsyncTask.Status.PENDING || fetchUpdateTask.getStatus() == AsyncTask.Status.FINISHED || fetchUpdateTask.isCancelled()) {
            fetchUpdateTask = new FetchUpdateTask();
            fetchUpdateTask.execute();
        }
    }

    /**
     *
     * @param locationIdToSearch A location to search updates for.
     */

    public void setUpdatesLocation(int locationIdToSearch) {
        this.locationIdToSearch=locationIdToSearch;
        if(fetchUpdateTask.getStatus() == AsyncTask.Status.PENDING || fetchUpdateTask.getStatus() == AsyncTask.Status.FINISHED || fetchUpdateTask.isCancelled()) {
            fetchUpdateTask = new FetchUpdateTask();
            fetchUpdateTask.execute();
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
        public Update passUpdateObject();
    }

    public interface OnFragmentInteractionListenerForDetailed {
        // TODO: Update argument type and name
        public int getTheIdOfTheActivityTHeFragmentIsAttachedTo();
        public Update passUpdateObject();
    }



    /**
     * AsyncTask FetchUpdateTask runs in background to send a HTTP request and fetch all updates in JSON format from database.
     */


    class FetchUpdateTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

            if(updateFragmentTextView == null)
                updateFragmentTextView = new TextView(getActivity());
            updateFragmentTextView.setClickable(false);

            progressLayout.setVisibility(View.VISIBLE);
            customUpdateList.setVisibility(View.GONE);
            Utility.CurrentUser.fetchUpdateTaskRunning = true;
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

            updateFragmentTextView.setClickable(true);

            //jsonUpdatesField=jsonUpdates;
            progressLayout.setVisibility(View.GONE);
            customUpdateList.setVisibility(View.VISIBLE);
            Utility.CurrentUser.fetchUpdateTaskRunning = false;

            if(jsonUpdates == null) {
                Utility.CurrentUser.showConnectionError(UpdateFragment.context);
            }
            else {
                // Check log cat for JSON reponse
                Log.d("All info: ", jsonUpdates.toString());

                if(Utility.CurrentUser.getDisplayPage() == 1) {                 //1 implies UPDATES page
                    synchronized (UpdateFragment.class) {
                        populateUpdateList(jsonUpdates);
                        populateUpdateListView();
                    }
                }

            }
        }
    }

    /**
     * AsyncTask AddLikerTask runs in background to send a HTTP request to add a new update-liker to database.
     */

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
         * After completing background task
         **/
        protected void onPostExecute (String a){
            if(jsonAddUpdateLike == null) {
                Utility.CurrentUser.showConnectionError(UpdateFragment.context);
            }
        }
    }

    /**
     * AsyncTask AddDislikerTask runs in background to send a HTTP request to add a new update-disliker to database.
     */

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

    /**
     * AsyncTask RemoveDislikerTask runs in background to send a HTTP request to remove update-disliker from database.
     */

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

    /**
     * AsyncTask RemovelikerTask runs in background to send a HTTP request to remove update-liker from database.
     */

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