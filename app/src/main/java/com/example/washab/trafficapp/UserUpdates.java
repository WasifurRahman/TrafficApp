package com.example.washab.trafficapp;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserUpdates#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserUpdates extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<Update> allUserUpdates=new ArrayList<Update>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserUpdates.
     */
    // TODO: Rename and change types and number of parameters
    public static UserUpdates newInstance(String param1, String param2) {
        UserUpdates fragment = new UserUpdates();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserUpdates() {
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


    private void populateUpdateList(){

        allUserUpdates.add(new Update("Smooth as breeze", 0, 15, 0, 5, "Dhanmondi", "New-market", "Mild", "11:15 AM", "11:20 AM", "Shabab"));
        allUserUpdates.add(new Update("Getting bored", 2, 50, 1, 23, "Mohakhali", "Jahangir Gate", "Extreme", "12:12 AM", "12:30 AM", "Neamul"));

    }

    private void populateUpdateListView(){
        ArrayAdapter<Update> adapter = new MyListAdapter();
        ListView list=(ListView)getView().findViewById(R.id.userUpdatesListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Update>{
        public MyListAdapter(){
            super(getActivity(), R.layout.user_update_item, allUserUpdates);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView=convertView;
            if(itemView==null){
                itemView=getActivity().getLayoutInflater().inflate(R.layout.user_update_item,parent,false);
            }


            //find the update to work with
            Update currentUpdate=allUserUpdates.get(position);
            //fill the view

            TextView locFrom = (TextView)itemView.findViewById(R.id.locationFromTextView);
            locFrom.setText(currentUpdate.getLocationFrom());

            TextView locTo = (TextView)itemView.findViewById(R.id.locationToTextView);
            locTo.setText(currentUpdate.getLocationTo());

            TextView sitDes=(TextView)itemView.findViewById(R.id.situationDesCriptionTextView);
            sitDes.setText(currentUpdate.getSituation());

            TextView estTime=(TextView) itemView.findViewById(R.id.estTimeDescriptorTextView);
            estTime.setText(""+currentUpdate.getEstTimeToCross());

            TextView updatorName=(TextView) itemView.findViewById(R.id.updatorNameTextView);
            updatorName.setText(currentUpdate.getUpdater());

            TextView updateTime=(TextView) itemView.findViewById(R.id.updateTimeTextView);
            updateTime.setText(currentUpdate.getTimeOfUpdate());

            TextView likeCnt=(TextView) itemView.findViewById(R.id.likeCountTextView);
            likeCnt.setText(""+currentUpdate.getLikeCount());

            TextView dislikeCnt=(TextView) itemView.findViewById(R.id.dislikeCountTextView);
            dislikeCnt.setText(""+currentUpdate.getDislikeCount());



            return itemView;
           // return super.getView(position, convertView, parent);



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
        populateUpdateList();
        populateUpdateListView();
        super.onActivityCreated(savedInstanceState);
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

}
