package com.vanberlo.blake.newname_android.Fragments;
import com.vanberlo.blake.newname_android.Models.Name;
import com.vanberlo.blake.newname_android.R;
import com.vanberlo.blake.newname_android.RealmService;
import io.realm.RealmResults;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.ML.Model;
import com.vanberlo.blake.newname_android.R;

import java.util.ArrayList;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;

    private Model model;
    private TextView newNameTextView;
    private ToggleButton genderToggleButton;

    private ArrayList<String> arrayHistory;
    private ListView listViewHistory;
    private ArrayAdapter<String> stringArrayAdapter;

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
        genderToggleButton = (ToggleButton) rootView.findViewById(R.id.toggleGender);
        listViewHistory = (ListView) rootView.findViewById(R.id.listViewHistory);

        //Something with the list
        arrayHistory = new ArrayList<String>();
        stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayHistory);
        listViewHistory.setAdapter(stringArrayAdapter);

        // Set the buttons' on click listeners to this fragment
        Button buttonFavourite = (Button) rootView.findViewById(R.id.buttonFavourite);
        Button buttonName = (Button) rootView.findViewById(R.id.buttonName);

        buttonFavourite.setOnClickListener(this);
        buttonName.setOnClickListener(this);

        listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                latestSelectedIndex = position;
                String selected =(listViewHistory.getItemAtPosition(position).toString());
                newNameTextView.setText(selected);
            }
        });

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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonName:
                onGenetateNameBtnClicked();
                break;
            case R.id.buttonFavourite:
                onFavouriteBtnClicked();
                break;

        }
    }

    public void onGenetateNameBtnClicked(){
        boolean male = (genderToggleButton).isChecked();
        String generatedNameLower = "";

        if (male){
            generatedNameLower = model.predictName(Gender.MALE);
            newNameTextView.setTextColor(Color.BLUE);
        }
        else{
            generatedNameLower = model.predictName(Gender.FEMALE);
            newNameTextView.setTextColor(Color.RED);

        }

        String generatedNameUpper = generatedNameLower.substring(0, 1).toUpperCase() + generatedNameLower.substring(1);

        arrayHistory.add(0,generatedNameUpper);
        stringArrayAdapter.notifyDataSetChanged();

        listViewHistory.setClickable(true);

        newNameTextView.setText(generatedNameUpper);
    }

    public void onFavouriteBtnClicked(){
        Context context = getApplicationContext();
        String selectedName = listViewHistory.getItemAtPosition(latestSelectedIndex).toString();
        CharSequence text = selectedName + "saved to Favourites!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        boolean male = (genderToggleButton).isChecked();

        RealmService realmService;
        realmService = new RealmService();
        if(male){
            realmService.insertName( selectedName, Gender.MALE);
        }
        else{
            realmService.insertName( selectedName, Gender.FEMALE);
        }
    }
}
