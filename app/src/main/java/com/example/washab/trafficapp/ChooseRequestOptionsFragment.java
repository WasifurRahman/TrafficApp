package com.example.washab.trafficapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseRequestOptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseRequestOptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseRequestOptionsFragment extends Fragment implements  View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button addRequestButton;
    private RadioButton mostFavouriteButton,mostRecentButton;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseRequestOptionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooseRequestOptionsFragment newInstance(String param1, String param2) {
        ChooseRequestOptionsFragment fragment = new ChooseRequestOptionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChooseRequestOptionsFragment() {
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


        View view =  inflater.inflate(R.layout.fragment_choose_request_options, container, false);

        addListernerForAll(view);

        return view;
    }

    private void addListernerForAll(View view){
        addRequestButton = (Button) view.findViewById(R.id.startAddingRequestButton);
        addRequestButton.setOnClickListener(this);

        mostFavouriteButton=(RadioButton)view.findViewById(R.id.requestOptionMostFavouriteButton);
        mostFavouriteButton.setOnClickListener(this);

        mostRecentButton=(RadioButton)view.findViewById(R.id.requestOptionMostRecentButton);
        mostRecentButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.startAddingRequestButton){
            mListener.startAddRequestActivity();
        }
        else if(v.getId()==R.id.requestOptionMostFavouriteButton){

            mListener.setRequestSorting("mostPopular");
            Log.d("SortingTest", "Button Clicked");
        }
        else if(v.getId()==R.id.requestOptionMostRecentButton){
            mListener.setRequestSorting("mostRecent");

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
        public void startAddRequestActivity();
        public void setRequestSorting(String sortingCrietaria);
    }

}
