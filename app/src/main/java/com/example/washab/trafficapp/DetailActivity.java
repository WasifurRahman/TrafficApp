package com.example.washab.trafficapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class DetailActivity extends AppCompatActivity implements  AddCommentFragment.OnFragmentInteractionListener,ShowCommentFragment.OnFragmentInteractionListener,Interfaces.WhichFragmentIsCallingDetailedActivity,Interfaces.ToWhichActivityIsTheFragmentAttached,UpdateFragment.OnFragmentInteractionListener,DiscussionFragment.OnFragmentInteractionListener ,AnnouncementFragment.OnFragmentInteractionListener,RequestFragment.OnFragmentInteractionListener{

    private String updatesFragmentTag = "UPDATES_FRAGMENT_TAG";
    private String discussionFragmentTag="DISCUSSIONFRAGMENT";
    private String showCommentFragmentTag = "SHOW_COMMENT_FRAGMENT_TAG";
    private String announcementFragmentTag="ANNOUNCEMENTFRAGMENT";
    private String requestsFragmentTag="REQUESTSFRAGMENT";
    private Update incomingUpdateObj;
    private Discussion incomingDiscussionObj;
    private Announcement incomingAnnouncementObj;
    private Request incomingRequestObj;
    private ArrayList<Comment> allCommentsArrayList = new ArrayList<Comment>();
    private String commentFragmentTag = "COMMENT_FRAGMENT_TAG";
    int fragmentToBeLoaded;
    private Button addCommentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        loadAppropriateFragments();
        setParameterForFragments();
    }

    private void setParameterForFragments() {

    }

    private void loadAppropriateFragments() {
        fragmentToBeLoaded = getIntent().getIntExtra("fragment_to_be_loaded", 0);
        //String message = getIntent().getStringExtra("print");

        switch (fragmentToBeLoaded) {
            case Interfaces.WhichFragmentIsCallingDetailedActivity.UPDATE_FRAGMENT:
                incomingUpdateObj = (Update) getIntent().getSerializableExtra("object_sent");
                //Log.d("detailed_activity",incomingObj.toString());
                //addUpdatesFragmentAndPassParamaters();
                addUpdatesFragment();
                //addShowCommentFragment();
                loadCommentsFromServer(incomingUpdateObj.getId());//comments are loaded from server.
                //then the background thread will load the json in the arraylist
                //then the new fragment will be added

                addAddCommentFragment();


                break;


            case Interfaces.WhichFragmentIsCallingDetailedActivity.DISCUSSION_FRAGMENT:
                incomingDiscussionObj = (Discussion) getIntent().getSerializableExtra("object_sent");
                addDiscussionFragment();
                //Log.d("detailed_activity",incomingObj.toString());
                //addUpdatesFragmentAndPassParamaters();

                //addShowCommentFragment();
                addAddCommentFragment();
                loadCommentsFromServer(incomingDiscussionObj.getId());//comments are loaded from server.
                //then the background thread will load the json in the arraylist
                //then the new fragment will be added




                break;

            case Interfaces.WhichFragmentIsCallingDetailedActivity.ANNOUNCEMENT_FRAGMENT:
                incomingAnnouncementObj = (Announcement) getIntent().getSerializableExtra("object_sent");
                addAnnouncementFragment();
                //Log.d("detailed_activity",incomingObj.toString());
                //addUpdatesFragmentAndPassParamaters();

                //addShowCommentFragment();
                addAddCommentFragment();
                loadCommentsFromServer(incomingAnnouncementObj.getId());//comments are loaded from server.
                //then the background thread will load the json in the arraylist
                //then the new fragment will be added




                break;

            case Interfaces.WhichFragmentIsCallingDetailedActivity.REQUEST_FRAGMENT:
                incomingRequestObj = (Request) getIntent().getSerializableExtra("object_sent");
                addRequestsFragment();


                //Log.d("detailed_activity",incomingObj.toString());
                //addUpdatesFragmentAndPassParamaters();






                break;
        }


    }

    private void loadCommentsFromServer(int id) {
        new FetchCommentsAssociatedWithUpdate().execute(id + "");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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

    private void addAddCommentFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddCommentFragment commentFragment = new AddCommentFragment();

        fragmentTransaction.add(R.id.addCommentFragmentContainer, commentFragment, commentFragmentTag);
        fragmentTransaction.commit();
    }

    private UpdateFragment addUpdatesFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UpdateFragment updatesFragment = new UpdateFragment();

        fragmentTransaction.add(R.id.infoAndLikeFragmentContainer, updatesFragment, updatesFragmentTag);
        fragmentTransaction.commit();
        return updatesFragment;
    }

    private void addAnnouncementFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //UpdateFragment updatesFragment = new  UpdateFragment();
        AnnouncementFragment announcementFragment=new AnnouncementFragment();

        fragmentTransaction.add(R.id.infoAndLikeFragmentContainer,announcementFragment,announcementFragmentTag);
        fragmentTransaction.commit();
    }

    private void addDiscussionFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //UpdateFragment updatesFragment = new  UpdateFragment();
        DiscussionFragment discussionFragment = new DiscussionFragment();

        fragmentTransaction.add(R.id.infoAndLikeFragmentContainer,discussionFragment,discussionFragmentTag);
        fragmentTransaction.commit();
    }

    private void addRequestsFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RequestFragment reqFragment=new RequestFragment();

        fragmentTransaction.add(R.id.infoAndLikeFragmentContainer, reqFragment,requestsFragmentTag);
        fragmentTransaction.commit();
    }


    private void addShowCommentFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // UpdateFragment updatesFragment = new UpdateFragment();
        ShowCommentFragment commentFragment = new ShowCommentFragment();

        fragmentTransaction.add(R.id.showcommentsFragmentContainer, commentFragment, showCommentFragmentTag);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void handleNewCommentAddition(Comment newComment) {

        ShowCommentFragment frag = (ShowCommentFragment) (getFragmentManager().findFragmentByTag(showCommentFragmentTag));

        addCommentButton = (Button) findViewById(R.id.addCommentButton);
        addCommentButton.setClickable(false);

        frag.addNewComment(newComment);//update the underlying comments list

        //now change in the database
        if(Interfaces.WhichFragmentIsCallingDetailedActivity.UPDATE_FRAGMENT==fragmentToBeLoaded){
            //the update object is loaded currently.

            new AddCommentTask().execute(""+newComment.getCommenterId(),""+incomingUpdateObj.getId(),newComment.getCommentText());
        }
        else if(Interfaces.WhichFragmentIsCallingDetailedActivity.ANNOUNCEMENT_FRAGMENT==fragmentToBeLoaded){
            //the update object is loaded currently.

            new AddCommentTask().execute(""+newComment.getCommenterId(),""+incomingAnnouncementObj.getId(),newComment.getCommentText());
        }
        else if(Interfaces.WhichFragmentIsCallingDetailedActivity.DISCUSSION_FRAGMENT==fragmentToBeLoaded){
            //the update object is loaded currently.

            new AddCommentTask().execute(""+newComment.getCommenterId(),""+incomingDiscussionObj.getId(),newComment.getCommentText());
        }

    }

    @Override
    public ArrayList<Comment> getCommentList() {
        // public Comment(int commenterId, String commentText, String timeStamp, String commenterName, int commentId)
        //allCommentsArrayList.add(new Comment("hmm besh valo", "23 june", "wasif", 21));
        //allCommentsArrayList.add(new Comment("oidikey besh jam", "22 june", "adnan", 24));
        return allCommentsArrayList;

        //return null;
    }

    @Override
    public void onAnnouncementFragmentInteraction(Uri uri) {

    }

    @Override
    public void callAppropriateDetailedActivity(int idOfTheFragmentToBeCalled, Object object) {

    }

    @Override
    public int getTheIdOfTheActivityTHeFragmentIsAttachedTo() {
        return 0;
    }

    @Override
    public Request passRequestObject() {
        return incomingRequestObj;
    }

    @Override
    public Announcement passAnnouncementObject() {
        return incomingAnnouncementObj;
    }

    @Override
    public Discussion passDiscussionObject() {
        return incomingDiscussionObj;
    }

    @Override
    public Update passUpdateObject() {
        return incomingUpdateObj;
    }



    class AddCommentTask extends AsyncTask<String, Void, String> {

        JSONObject jsonAddComments;

        @Override
        protected void onPreExecute() {
//            progressLayout.setVisibility(View.VISIBLE);
//            customUpdateList.setVisibility(View.GONE);
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();
            int commenterId = Integer.parseInt(args[0]);
            int toBeAssociatedId=Integer.parseInt(args[1]);
            String commentText=args[2];

            params.add(new Pair("commenterId",commenterId));
            params.add(new Pair("commentText",commentText));

            switch (fragmentToBeLoaded) {
                case Interfaces.WhichFragmentIsCallingDetailedActivity.UPDATE_FRAGMENT:
                    params.add(new Pair("updateId", toBeAssociatedId));
                    jsonAddComments = jParser.makeHttpRequest("/addupdatecomment", "POST", params);
                    break;
                case Interfaces.WhichFragmentIsCallingDetailedActivity.ANNOUNCEMENT_FRAGMENT:

                    params.add(new Pair("postId", toBeAssociatedId));
                    jsonAddComments = jParser.makeHttpRequest("/addpostcomment", "POST", params);
                    break;

                case Interfaces.WhichFragmentIsCallingDetailedActivity.DISCUSSION_FRAGMENT:

                    params.add(new Pair("postId", toBeAssociatedId));
                    Log.d("adding comments in discussion"," entered");
                    jsonAddComments = jParser.makeHttpRequest("/addpostcomment", "POST", params);
                    Log.d("adding comments in discussion",jsonAddComments.toString());
                    break;
            }
            // getting JSON string from URL





            return null;

        }


        protected void onPostExecute (String a){

            addCommentButton.setClickable(true);
            Toast.makeText(getApplicationContext(), "New Comment Added", Toast.LENGTH_LONG).show();

            if(jsonAddComments == null) {
//                Utility.CurrentUser.showConnectionError(getApplicationContext());
            }
            Log.d("update comments:", jsonAddComments.toString());

        }
    }

    class FetchCommentsAssociatedWithUpdate extends AsyncTask<String, Void, String> {

        JSONObject jsonComments;

        @Override
        protected void onPreExecute() {
            //progressLayout.setVisibility(View.VISIBLE);
            //customUpdateList.setVisibility(View.GONE);
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Building Parameters
            List<Pair> params = new ArrayList<Pair>();
            int idToFetchCommentsFrom = Integer.parseInt(args[0]);

            switch (fragmentToBeLoaded) {
                case Interfaces.WhichFragmentIsCallingDetailedActivity.UPDATE_FRAGMENT:
                    params.add(new Pair("updateId", idToFetchCommentsFrom));
                    // getting JSON string from URL
                    jsonComments = jParser.makeHttpRequest("/updatecomments", "GET", params);
                    break;
                case Interfaces.WhichFragmentIsCallingDetailedActivity.ANNOUNCEMENT_FRAGMENT:
                    params.add(new Pair("postId", idToFetchCommentsFrom));
                    // getting JSON string from URL
                    jsonComments = jParser.makeHttpRequest("/postcomments", "GET", params);

                    break;

                case Interfaces.WhichFragmentIsCallingDetailedActivity.DISCUSSION_FRAGMENT:
                    params.add(new Pair("postId", idToFetchCommentsFrom));
                    // getting JSON string from URL

                    jsonComments = jParser.makeHttpRequest("/postcomments", "GET", params);

                    break;

            }


            return null;

        }

        protected void onPostExecute (String a){
            if(jsonComments== null) {
                Utility.CurrentUser.showConnectionError(getApplicationContext());
            }
            Log.d("update comments:",jsonComments.toString());
            convertTheJSONIntoArrayList(jsonComments);
        }



    }

    private void convertTheJSONIntoArrayList(JSONObject jsonComments) {
        try {
            JSONArray jsonCommentsArray=jsonComments.getJSONArray("comments");
            allCommentsArrayList.clear();
            for(int i=0;i<jsonCommentsArray.length();i++){
                JSONObject curCommentJSONObj=jsonCommentsArray.getJSONObject(i);

                String commenterName=curCommentJSONObj.getString("commenterName");
                String commentText=curCommentJSONObj.getString("commentText");
                int commentId=curCommentJSONObj.getInt("commentId");
                String timeOfComment=curCommentJSONObj.getString("timeOfComment");

                Comment curComment=new Comment(commentText,timeOfComment,commenterName,commentId);
                allCommentsArrayList.add(curComment);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //now we can add the show comment fragment safely
        addShowCommentFragment();
    }


}
