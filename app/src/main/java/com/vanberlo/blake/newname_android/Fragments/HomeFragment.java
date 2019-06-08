package com.vanberlo.blake.newname_android.Fragments;
import com.vanberlo.blake.newname_android.Models.Name;
import com.vanberlo.blake.newname_android.R;
import com.vanberlo.blake.newname_android.Adapters.RecentsListAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.opengl.GLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.ML.Model;
import com.vanberlo.blake.newname_android.RealmService;

import java.util.ArrayList;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;

    private Model model;
    private TextView newNameTextView;
    private Switch genderToggleButton;

    private ArrayList<Name> arrayHistory;
    private ListView listViewHistory;
    private RecentsListAdapter stringArrayAdapter;

    private int latestSelectedIndex;

    // Required empty public constructor
    public HomeFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize model
        model = new Model(getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //Assigning view IDs. Wait for root view to be created first.
        newNameTextView = (TextView) rootView.findViewById(R.id.textViewName);
        genderToggleButton = (Switch) rootView.findViewById(R.id.toggleGender);
        listViewHistory = (ListView) rootView.findViewById(R.id.listViewHistory);

        //Something with the list
        arrayHistory = new ArrayList<Name>();
        stringArrayAdapter = new RecentsListAdapter(getApplicationContext(), arrayHistory);
        listViewHistory.setAdapter(stringArrayAdapter);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonName:
                onGenerateNameBtnClicked();
                break;
            case R.id.listViewHistory:
                int t = (Integer)v.getId();
                Object tag = v.getTag();
                listViewHistory.performClick();
                break;
        }
    }

    public void onGenerateNameBtnClicked(){
        //Check the toggle button for the gender selected
        boolean male = (genderToggleButton).isChecked();

        //Generate a male or female name based on the previous variable
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

        //Create a name object out of the name generated to be added to the listview
        if(male) {
            generatedName = new Name(generatedNameUpper, Gender.MALE);
        }else{
            generatedName = new Name(generatedNameUpper, Gender.FEMALE);
        }

        //Update the list view, set the text of the main textview to the new name generated
        arrayHistory.add(0,generatedName);
        stringArrayAdapter.notifyDataSetChanged();
        listViewHistory.setClickable(true);
        newNameTextView.setText(generatedNameUpper);

    }



}
