package com.example.washab.trafficapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Home extends AppCompatActivity implements UserUpdates.OnFragmentInteractionListener ,AnnouncementFragment.OnFragmentInteractionListener,
ChooseUpdateOptionsFragment.OnFragmentInteractionListener,ChooseRequestOptionsFragment.OnFragmentInteractionListener,RequestFragment.OnFragmentInteractionListener,PostTypeFragment.OnFragmentInteractionListener
,ChooseAnnouncementOptionsFragment.OnFragmentInteractionListener{

    private String updatesFragmentTag="UPDATESFRAGMENT";
    private String postsFragmentTag="POSTSFRAGMENT";
    private String requestsFragmentTag="REQUESTSFRAGMENT";
    private String chooseUpdateOptionsFragmentsTag="CHOOSEUPDATEOPTIONSFRAGMENT";
    private String chooseRequestOptionsFragmentsTag="CHOOSEREQUESTOPTIONSFRAGMENT";
    private String chooseAnnouncementOptionsFragmentsTag="CHOOSEANNOUNCEMENTOPTIONSFRAGMENT";
    private String choosePostTypeFragmentsTag="CHOOSEPOSTTYPEFRAGMENT";



    private String updatesSortingCrieteria="mostRecent";
    private String requestSortingCrieteria="mostRecent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
        UserUpdates updatesFragment = new  UserUpdates();

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
        //UserUpdates updatesFragment = new  UserUpdates();
        AnnouncementFragment announcementFragment=new AnnouncementFragment();

        fragmentTransaction.add(R.id.dataFragmentContainer,announcementFragment,postsFragmentTag);
        fragmentTransaction.commit();
    }

    private void removeAddedFragment(String exception){
        Fragment tobeDeleted = getFragmentManager().findFragmentByTag(updatesFragmentTag);
        if(tobeDeleted!=null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(tobeDeleted);
            fragmentTransaction.commit();
        }

        tobeDeleted = getFragmentManager().findFragmentByTag(requestsFragmentTag);
        if(tobeDeleted!=null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(tobeDeleted);
            fragmentTransaction.commit();
        }


            tobeDeleted = getFragmentManager().findFragmentByTag(postsFragmentTag);
            if (tobeDeleted != null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(tobeDeleted);
                fragmentTransaction.commit();
            }


        tobeDeleted = getFragmentManager().findFragmentByTag(chooseUpdateOptionsFragmentsTag);
        if(tobeDeleted!=null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(tobeDeleted);
            fragmentTransaction.commit();
        }

        tobeDeleted = getFragmentManager().findFragmentByTag(chooseRequestOptionsFragmentsTag);
        if(tobeDeleted!=null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(tobeDeleted);
            fragmentTransaction.commit();
        }
        if( exception!=null && exception!=choosePostTypeFragmentsTag) {
            tobeDeleted = getFragmentManager().findFragmentByTag(choosePostTypeFragmentsTag);
            if (tobeDeleted != null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(tobeDeleted);
                fragmentTransaction.commit();
            }
        }

        tobeDeleted = getFragmentManager().findFragmentByTag(chooseAnnouncementOptionsFragmentsTag);
        if(tobeDeleted!=null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(tobeDeleted);
            fragmentTransaction.commit();
        }
    }



    public void onUpdatesButtonClick(View v){
        if(v.getId()==R.id.updatesButton){
            removeAddedFragment(null);
            addUpdatesFragment();
            addChooseUpdateOptionsFragment();
        }
    }

    public void onPostsButtonClick(View v){
        if(v.getId()==R.id.postsButton){
            removeAddedFragment(null);
            addPostTypeFragment();

        }
    }

    public void onRequestsButtonClick(View v){
        if(v.getId()==R.id.requestsButton){
            removeAddedFragment(null);
            addChooseRequestOptionsFragment();
            addRequestsFragment();
        }
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void startAddAnnouncementActivity() {

    }

    @Override
    public void setAnnouncementSorting(String sortingCrietaria) {

    }

    @Override
    public void startAnnouncementFragment() {
          removeAddedFragment(choosePostTypeFragmentsTag);
          addChooseAnnouncementOptionsFragment();
          addAnnouncementFragment();
    }

    @Override
    public void startDiscussionFragment() {

    }

    @Override
    public void startAddRequestActivity(){
        Intent intent =new Intent(this,AddRequest.class);
        startActivity(intent);
    }

    @Override
    public void setRequestSorting(String sortingCrietaria) {
        requestSortingCrieteria=sortingCrietaria;
        Log.d("Srting Crieteria: ",sortingCrietaria);
    }

    @Override
    public void onAnnouncementFragmentInteraction(Uri uri) {

    }

    @Override
    public void startAddUpdateActivity() {

        Intent intent =new Intent(this,AddUpdate.class);
        startActivity(intent);

    }

    @Override
    public void setUpdateSorting(String sortingCrietaria) {
        updatesSortingCrieteria=sortingCrietaria;
        Log.d("Srting Crieteria: ",sortingCrietaria);
    }
}
