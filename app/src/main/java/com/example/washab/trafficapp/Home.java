package com.example.washab.trafficapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class Home extends AppCompatActivity implements UpdateFragment.OnFragmentInteractionListener ,AnnouncementFragment.OnFragmentInteractionListener,
ChooseUpdateOptionsFragment.OnFragmentInteractionListener,ChooseRequestOptionsFragment.OnFragmentInteractionListener,RequestFragment.OnFragmentInteractionListener,PostTypeFragment.OnFragmentInteractionListener
,ChooseAnnouncementOptionsFragment.OnFragmentInteractionListener,ChooseDiscussionOptionsFragment.OnFragmentInteractionListener,DiscussionFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener,
Interfaces.WhoIsCallingUpdateInterface{

    private String updatesFragmentTag="UPDATESFRAGMENT";
    private String postsFragmentTag="POSTSFRAGMENT";
    private String requestsFragmentTag="REQUESTSFRAGMENT";
    private String notifsFragmentTag = "NOTIFSFRAGMENT";
    private String announcementFragmentTag="ANNOUNCEMENTFRAGMENT";
    private String discussionFragmentTag="DISCUSSIONFRAGMENT";
    private String chooseUpdateOptionsFragmentsTag="CHOOSEUPDATEOPTIONSFRAGMENT";
    private String chooseRequestOptionsFragmentsTag="CHOOSEREQUESTOPTIONSFRAGMENT";
    private String chooseAnnouncementOptionsFragmentsTag="CHOOSEANNOUNCEMENTOPTIONSFRAGMENT";
    private String choosePostTypeFragmentsTag="CHOOSEPOSTTYPEFRAGMENT";
    private String chooseDiscussionOptionsFragmentsTag="CHOOSEDISCUSSIONOPTIONSFRAGMENT";
    private String[] locationChoices;
    private int locationIdToSearch;
    private int currentPostTypeToShow=DISCUSSIONS;

    private static final int UPDATES = 1;
    private static final int POSTS = 2;
    private static final int REQUESTS = 3;
    private static final int NOTIFS = 4;
    private static final int DISCUSSIONS = 5;
    private static final int ANNOUNCEMENTS= 6;
    private static final int SWIPE_DISTANCE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private Menu menu;
//    private GestureDetectorCompat gestureDetector;

//
//    private String announcementSortingCriteria ="mostRecent";
//    private String discussionSortingCriteria ="mostRecent";
//
//
//    private String updatesSortingCriteria ="mostRecent";
//    private String requestSortingCriteria ="mostRecent";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        this.gestureDetector = new GestureDetectorCompat(this, this);               //an object from this class which would allow us to detect gestures

        locationChoices=Locations.getAllLocationNamesForSearch();
        // add all locations at the start of the location names

        //locationIdToSearch=Locations.getLocationId(locationChoices[0]);

        if(Utility.CurrentUser.getDisplayPage() == 0) {
            Utility.CurrentUser.setDisplayPage(UPDATES);
        }

        switch(Utility.CurrentUser.getDisplayPage()) {
            case UPDATES:
//                Log.d("inside the switch", "testing");
                ((TextView)findViewById(R.id.updatesButton)).setTextColor(Color.parseColor("#A5DF00"));
                addUpdatesFragment();
                addChooseUpdateOptionsFragment();
                break;
            case POSTS:
                ((TextView)findViewById(R.id.postsButton)).setTextColor(Color.parseColor("#A5DF00"));
                addPostTypeFragment();
//                addChoosePostTypeOptionsFragment();
                addDiscussionFragment();
                addChooseDiscussionOptionsFragment();
                break;
            case REQUESTS:
                ((TextView)findViewById(R.id.requestsButton)).setTextColor(Color.parseColor("#A5DF00"));
                addRequestsFragment();
                addChooseRequestOptionsFragment();
                break;
            case NOTIFS:
                ((TextView)findViewById(R.id.notifsButton)).setTextColor(Color.parseColor("#A5DF00"));
                addNotifsFragment();
                break;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.definedmenu, menu);
        this.menu=menu;

        MenuItem item=menu.findItem(R.id.searchSpinner);
        Spinner searchSpinner= (Spinner) MenuItemCompat.getActionView(item);
        if(searchSpinner==null)Log.d("null error: ","searchspinner is null");
        if(searchSpinner!=null) {
            searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //the first postion is for all updates,so nothing to change here
                    if(position==0)locationIdToSearch=0;

                    else locationIdToSearch=Locations.getLocationId(locationChoices[position]);
                    String activeFragmentTag=currentLoadedFragmentTag();
                    Fragment activeFragment = getFragmentManager().findFragmentByTag(activeFragmentTag);

                    if(activeFragment.getClass()==UpdateFragment.class){
                        Log.d("fragment alive","update fragment new");
                        ((UpdateFragment)activeFragment).setUpdatesLocation(locationIdToSearch);
                    }
                    else if(activeFragment.getClass()==RequestFragment.class){
                        Log.d("fragment alive","request fragment new");
                        ((RequestFragment)activeFragment).setRequestSearchLocation(locationIdToSearch);
                    }
                    else if(activeFragment.getClass()==AnnouncementFragment.class){
                        Log.d("fragment alive","announcement fragment new");
                        ((AnnouncementFragment)activeFragment).setAnnouncementSearchLocation(locationIdToSearch);
                    }
                    else if(activeFragment.getClass()==DiscussionFragment.class){
                        Log.d("fragment alive","discussion fragment new");
                        ((DiscussionFragment)activeFragment).setDiscussionSearchLocation(locationIdToSearch);
                    }



                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // Application of the Array to the Spinner
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locationChoices);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            searchSpinner.setAdapter(spinnerArrayAdapter);
        }
        return true;
    }




    @Override
    protected void onPostResume() {
        if(Utility.CurrentUser.isTheUserValid()==false){
            Intent intent=new Intent(Home.this,MainActivity.class);
            startActivity(intent);
        }
        super.onPostResume();
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
        else if(id==R.id.logoutButton){
            Utility.CurrentUser.invalidate();
            Intent intent=new Intent(Home.this,MainActivity.class);
            startActivity(intent);

        }
        else if(id==R.id.addPreferredLocation){

            Intent intent=new Intent(Home.this,AddPreferredLocationActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    private void setTheLocationInTitlebarToAllLocations(){
        MenuItem item=menu.findItem(R.id.searchSpinner);
        Spinner searchSpinner= (Spinner) MenuItemCompat.getActionView(item);
        searchSpinner.setSelection(0);
    }

    private String currentLoadedFragmentTag(){
        Fragment updateFragment = getFragmentManager().findFragmentByTag(updatesFragmentTag);
        if(updateFragment!=null){
            //Log.d("fragment alive","update frgment");
            return updatesFragmentTag;
        }
        Fragment requestFragment = getFragmentManager().findFragmentByTag(requestsFragmentTag);
        if(requestFragment!=null){
            return requestsFragmentTag;
            //Log.d("fragment alive","request frgment");
        }
        Fragment postsFragment = getFragmentManager().findFragmentByTag(postsFragmentTag);
//        if(postsFragment!=null){
//            //Log.d("fragment alive","post frgment");
//            return postsFragmentTag;
//        }
        Fragment discussionFragment = getFragmentManager().findFragmentByTag(discussionFragmentTag);
        if(discussionFragment!=null){
            return discussionFragmentTag;
            //Log.d("fragment alive","discussion frgment");

        }
        Fragment announcementFragment = getFragmentManager().findFragmentByTag(announcementFragmentTag);
        if(announcementFragment!=null){
            return announcementFragmentTag;
            //Log.d("fragment alive","announcement frgment");
        }
        return null;

    }

    private void addChooseUpdateOptionsFragment(){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ChooseUpdateOptionsFragment choiceFragment=new ChooseUpdateOptionsFragment();

        fragmentTransaction.add(R.id.choiceFragmentContainer,choiceFragment,chooseUpdateOptionsFragmentsTag);
        fragmentTransaction.commit();
    }

    private void addChooseRequestOptionsFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ChooseRequestOptionsFragment choiceFragment=new ChooseRequestOptionsFragment();

        fragmentTransaction.add(R.id.choiceFragmentContainer,choiceFragment,chooseRequestOptionsFragmentsTag);
        fragmentTransaction.commit();

    }

    private void addChooseAnnouncementOptionsFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ChooseAnnouncementOptionsFragment choiceFragment=new ChooseAnnouncementOptionsFragment();

        fragmentTransaction.add(R.id.choiceFragmentContainer,choiceFragment,chooseAnnouncementOptionsFragmentsTag);
        fragmentTransaction.commit();

    }

    private void addChooseDiscussionOptionsFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

       ChooseDiscussionOptionsFragment choiceFragment =new ChooseDiscussionOptionsFragment();

        fragmentTransaction.add(R.id.choiceFragmentContainer,choiceFragment,chooseDiscussionOptionsFragmentsTag);
        fragmentTransaction.commit();
    }

    private void addPostTypeFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PostTypeFragment postTypeFrag= new PostTypeFragment();

        fragmentTransaction.add(R.id.choicePostTypeFragmentContainer,postTypeFrag,choosePostTypeFragmentsTag);
        fragmentTransaction.commit();

    }

    private void addUpdatesFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UpdateFragment updatesFragment = new UpdateFragment();

        fragmentTransaction.add(R.id.dataFragmentContainer, updatesFragment,updatesFragmentTag);
        fragmentTransaction.commit();
    }

    private void addRequestsFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RequestFragment reqFragment=new RequestFragment();

        fragmentTransaction.add(R.id.dataFragmentContainer, reqFragment,requestsFragmentTag);
        fragmentTransaction.commit();
    }

    private void addAnnouncementFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //UpdateFragment updatesFragment = new  UpdateFragment();
        AnnouncementFragment announcementFragment=new AnnouncementFragment();

        fragmentTransaction.add(R.id.dataFragmentContainer,announcementFragment,announcementFragmentTag);
        fragmentTransaction.commit();
    }

    private void addDiscussionFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //UpdateFragment updatesFragment = new  UpdateFragment();
        DiscussionFragment discussionFragment = new DiscussionFragment();

        fragmentTransaction.add(R.id.dataFragmentContainer,discussionFragment,discussionFragmentTag);
        fragmentTransaction.commit();
    }

    private void addNotifsFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NotificationsFragment notifFragment = new NotificationsFragment();

        fragmentTransaction.add(R.id.dataFragmentContainer, notifFragment, notifsFragmentTag);
        fragmentTransaction.commit();
    }

    private void removeAddedFragment(String exception){
        Fragment updateFragment = getFragmentManager().findFragmentByTag(updatesFragmentTag);
        Fragment requestFragment = getFragmentManager().findFragmentByTag(requestsFragmentTag);
        Fragment postsFragment = getFragmentManager().findFragmentByTag(postsFragmentTag);
        Fragment discussionFragment = getFragmentManager().findFragmentByTag(discussionFragmentTag);
        Fragment announcementFragment = getFragmentManager().findFragmentByTag(announcementFragmentTag);
        Fragment notifsFragment = getFragmentManager().findFragmentByTag(notifsFragmentTag);
        Fragment chooseUpdateOptionsFragment= getFragmentManager().findFragmentByTag(chooseUpdateOptionsFragmentsTag);
        Fragment chooseRequestOptionsFragment= getFragmentManager().findFragmentByTag(chooseRequestOptionsFragmentsTag);
        Fragment chooseAnnouncementOptionsFragment = getFragmentManager().findFragmentByTag(chooseAnnouncementOptionsFragmentsTag);
        Fragment choosePostTypeFragment = getFragmentManager().findFragmentByTag(choosePostTypeFragmentsTag);
        Fragment chooseDiscussionOptionsFragment = getFragmentManager().findFragmentByTag(chooseDiscussionOptionsFragmentsTag);

            FragmentManager fragmentManager= getFragmentManager();
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            if(updateFragment!=null)fragmentTransaction.remove(updateFragment);
            if(requestFragment!=null)fragmentTransaction.remove(requestFragment);
            if(postsFragment!=null)fragmentTransaction.remove(postsFragment);
            if(discussionFragment!=null)fragmentTransaction.remove(discussionFragment);
            if(announcementFragment!=null)fragmentTransaction.remove(announcementFragment);
            if(notifsFragment!=null)fragmentTransaction.remove(notifsFragment);
            if(chooseUpdateOptionsFragment!=null)fragmentTransaction.remove(chooseUpdateOptionsFragment);
            if(chooseRequestOptionsFragment!=null)fragmentTransaction.remove(chooseRequestOptionsFragment);
            if(exception==null && choosePostTypeFragment!=null)fragmentTransaction.remove(choosePostTypeFragment);
            if(chooseAnnouncementOptionsFragment!=null)fragmentTransaction.remove(chooseAnnouncementOptionsFragment);
            if(chooseDiscussionOptionsFragment!=null)fragmentTransaction.remove(chooseDiscussionOptionsFragment);


            fragmentTransaction.commit();
    }



    public void onUpdatesButtonClick(View v){
        if(v.getId()==R.id.updatesButton){
            setTheLocationInTitlebarToAllLocations();
            switch (Utility.CurrentUser.getDisplayPage()) {
                case UPDATES:
                    break;
                case POSTS:
                    ((TextView)findViewById(R.id.postsButton)).setTextColor(Color.BLACK);
                    break;
                case REQUESTS:
                    ((TextView)findViewById(R.id.requestsButton)).setTextColor(Color.BLACK);
                    break;
                case NOTIFS:
                    ((TextView)findViewById(R.id.notifsButton)).setTextColor(Color.BLACK);
                    break;
            }
            Utility.CurrentUser.setDisplayPage(UPDATES);
            ((TextView)findViewById(R.id.updatesButton)).setTextColor(Color.parseColor("#A5DF00"));

            removeAddedFragment(null);
            addUpdatesFragment();
            addChooseUpdateOptionsFragment();
        }
    }

    public void onPostsButtonClick(View v){
        if(v.getId()==R.id.postsButton){
            setTheLocationInTitlebarToAllLocations();
            switch (Utility.CurrentUser.getDisplayPage()) {
                case UPDATES:
                    ((TextView)findViewById(R.id.updatesButton)).setTextColor(Color.BLACK);
                    break;
                case POSTS:
                    break;
                case REQUESTS:
                    ((TextView)findViewById(R.id.requestsButton)).setTextColor(Color.BLACK);
                    break;
                case NOTIFS:
                    ((TextView)findViewById(R.id.notifsButton)).setTextColor(Color.BLACK);
                    break;
            }

            Utility.CurrentUser.setDisplayPage(POSTS);
            ((TextView)findViewById(R.id.postsButton)).setTextColor(Color.parseColor("#A5DF00"));
            removeAddedFragment(null);
            addPostTypeFragment();
            startDiscussionFragment();

        }
    }

    public void onRequestsButtonClick(View v){
        if(v.getId()==R.id.requestsButton){
            setTheLocationInTitlebarToAllLocations();
            switch (Utility.CurrentUser.getDisplayPage()) {
                case UPDATES:
                    ((TextView)findViewById(R.id.updatesButton)).setTextColor(Color.BLACK);
                    break;
                case POSTS:
                    ((TextView)findViewById(R.id.postsButton)).setTextColor(Color.BLACK);
                    break;
                case REQUESTS:
                    break;
                case NOTIFS:
                    ((TextView)findViewById(R.id.notifsButton)).setTextColor(Color.BLACK);
                    break;
            }
            Utility.CurrentUser.setDisplayPage(REQUESTS);
            ((TextView)findViewById(R.id.requestsButton)).setTextColor(Color.parseColor("#A5DF00"));
            removeAddedFragment(null);
            addChooseRequestOptionsFragment();
            addRequestsFragment();
        }
    }

    public void onNotifsButtonClick(View v){
//        Log.d("inside notifs button click", "yes");
        if(v.getId()==R.id.notifsButton){
            setTheLocationInTitlebarToAllLocations();
            switch (Utility.CurrentUser.getDisplayPage()) {
                case UPDATES:
                    ((TextView)findViewById(R.id.updatesButton)).setTextColor(Color.BLACK);
                    break;
                case POSTS:
                    ((TextView)findViewById(R.id.postsButton)).setTextColor(Color.BLACK);
                    break;
                case REQUESTS:
                    ((TextView)findViewById(R.id.requestsButton)).setTextColor(Color.BLACK);
                    break;
                case NOTIFS:
                    break;
            }
            Utility.CurrentUser.setDisplayPage(NOTIFS);
            ((TextView)findViewById(R.id.notifsButton)).setTextColor(Color.parseColor("#A5DF00"));

            removeAddedFragment(null);
            addNotifsFragment();
        }
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void startAddDiscussionActivity() {
           Intent intent =new Intent(Home.this,AddDiscussion.class);
           startActivity(intent);
    }

    @Override
    public void setDiscussionSorting(String sortingCrietaria) {

//        discussionSortingCriteria =sortingCrietaria;
        Log.d("Srting Crieteria: ",sortingCrietaria);

        DiscussionFragment discussionFragment = (DiscussionFragment) getFragmentManager().findFragmentByTag(discussionFragmentTag);
        if(discussionFragment!=null)discussionFragment.setDiscussionSorting(sortingCrietaria);


    }

    @Override
    public void startAddAnnouncementActivity() {

        Intent intent =new Intent(Home.this,AddAnnouncement.class);
        startActivity(intent);

    }

    @Override
    public void setAnnouncementSorting(String sortingCrietaria) {

//        announcementSortingCriteria =sortingCrietaria;
        Log.d("Srting Crieteria: ",sortingCrietaria);


        UpdateFragment updateFragment = (UpdateFragment) getFragmentManager().findFragmentByTag(updatesFragmentTag);

//        UpdateFragment updateFragment = (UpdateFragment) getFragmentManager().findFragmentByTag(updatesFragmentTag);

        AnnouncementFragment announcementFragment=(AnnouncementFragment)getFragmentManager().findFragmentByTag(announcementFragmentTag);
        if(announcementFragment!=null)announcementFragment.setAnnouncementSorting(sortingCrietaria);

    }

    @Override
    public void startAnnouncementFragment() {
          setTheLocationInTitlebarToAllLocations();
          removeAddedFragment(choosePostTypeFragmentsTag);
          addChooseAnnouncementOptionsFragment();
          addAnnouncementFragment();
    }

    @Override
    public void startDiscussionFragment() {
        setTheLocationInTitlebarToAllLocations();
        removeAddedFragment(choosePostTypeFragmentsTag);
        addChooseDiscussionOptionsFragment();
        addDiscussionFragment();
       // addAnnouncementFragment();
    }

    @Override
    public void setPostTypeToShow(int postTypeToShow) {
        this.currentPostTypeToShow=postTypeToShow;
        //if(currentPostTypeToShow==DISCUSSIONS)Log.d("chosen fragment","discussions");
       // if(currentPostTypeToShow==ANNOUNCEMENTS)Log.d("chosen fragment","announcement");
    }


    @Override
    public void startAddRequestActivity(){
        Intent intent =new Intent(this,AddRequest.class);
        startActivity(intent);
    }

    @Override
    public void setRequestSorting(String sortingCriteria) {
//        requestSortingCriteria =sortingCriteria;
        Log.d("Srting Crieteria: ",sortingCriteria);

        RequestFragment requestFragment = (RequestFragment) getFragmentManager().findFragmentByTag(requestsFragmentTag);
        if(requestFragment!=null)requestFragment.setRequestSorting(sortingCriteria);
    }

    @Override
    public void onAnnouncementFragmentInteraction(Uri uri) {

    }

    @Override
    public void startAddUpdateActivity() {

        //normal AddUpdate calling
        Intent intent =new Intent(this,AddUpdate.class).putExtra("calling_from", Interfaces.WhoIsCallingUpdateInterface.ADD_UPDATE_WILLINGLY);
        startActivity(intent);

    }

    @Override
    public void setUpdateSorting(String sortingCrietaria) {
//        updatesSortingCriteria =sortingCrietaria;
        Log.d("Srting Crieteria: ",sortingCrietaria);

        UpdateFragment updateFragment = (UpdateFragment) getFragmentManager().findFragmentByTag(updatesFragmentTag);
        if(updateFragment!=null)updateFragment.setUpdateSorting(sortingCrietaria);

    }
}
