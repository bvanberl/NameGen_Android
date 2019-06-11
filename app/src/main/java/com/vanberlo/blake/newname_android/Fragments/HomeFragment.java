package com.vanberlo.blake.newname_android.Fragments;
import com.vanberlo.blake.newname_android.Models.Name;
import com.vanberlo.blake.newname_android.R;
import com.vanberlo.blake.newname_android.Adapters.RecentsListAdapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.ML.Model;

import java.util.ArrayList;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;

    private Model model;
    private TextView newNameTextView;
    private Switch genderToggleButton;

    private ArrayList<Name> recentNames; // A record of most recently generated names
    private ListView listViewHistory;
    private RecentsListAdapter recentNamesAdapter;

    // Required empty public constructor
    public HomeFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize RNN model
        model = new Model(getApplicationContext());

    }

    /**
     * Set up references to UI elements
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //Assigning view IDs. Wait for root view to be created first.
        newNameTextView = (TextView) rootView.findViewById(R.id.textViewName);
        genderToggleButton = (Switch) rootView.findViewById(R.id.toggleGender);
        listViewHistory = (ListView) rootView.findViewById(R.id.listViewHistory);

        // Set the the recent names list and its adapter
        recentNames = new ArrayList<Name>();
        recentNamesAdapter = new RecentsListAdapter(getApplicationContext(), recentNames);
        listViewHistory.setAdapter(recentNamesAdapter);

        // Set the buttons' on click listeners to this fragment
        Button buttonName = (Button) rootView.findViewById(R.id.buttonName);
        buttonName.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Event handler to receive click events for various views within this fragment
     * @param v - the view that was clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonName:
                onGenerateNameBtnClicked(); // Generate a new name
                break;
            case R.id.listViewHistory:
                int t = (Integer)v.getId();
                Object tag = v.getTag();
                listViewHistory.performClick(); // The user tapped on the recent names ListView; pass the event on
                break;
        }
    }


    /**
     * Uses the RNN model to generate a name
     */
    public void onGenerateNameBtnClicked(){
        // Get the value of the gender toggle view
        boolean male = (genderToggleButton).isChecked();

        //Generate a male or female name based on which gender is currently selected
        String generatedNameLower = "";
        if (male){
            generatedNameLower = model.predictName(Gender.MALE);
        }
        else{
            generatedNameLower = model.predictName(Gender.FEMALE);
        }

        //Add an upper case letter as the first letter in the name
        String generatedNameUpper = generatedNameLower.substring(0, 1).toUpperCase() + generatedNameLower.substring(1);
        Name generatedName;

        //Create a Name object out of the name generated to be added to the list of recent names
        if(male) {
            generatedName = new Name(generatedNameUpper, Gender.MALE);
        }else{
            generatedName = new Name(generatedNameUpper, Gender.FEMALE);
        }

        recentNames.add(0,generatedName); // Update the recent names list
        recentNamesAdapter.notifyDataSetChanged();
        listViewHistory.setClickable(true);
        newNameTextView.setText(generatedNameUpper); // Set the text of the newNameTextView to the most recent name
    }

}
