package com.vanberlo.blake.newname_android.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.vanberlo.blake.newname_android.Adapters.FavouritesListAdapter;
import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.Models.Name;
import com.vanberlo.blake.newname_android.R;
import com.vanberlo.blake.newname_android.RealmService;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;


public class FavouritesFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private RealmService realmService;
    private RealmResults<Name> savedNames;

    private ListView listViewFavourites;
    private FavouritesListAdapter favouritesListAdapter;

    private TextView textViewName;
    private CheckBox maleCheckbox;
    private CheckBox femaleCheckbox;
    private EditText searchEditText;

    public FavouritesFragment() {
        // Required empty public constructor
        realmService = new RealmService();
        savedNames = realmService.getAllNames();
    }


    /**
     * Set up refenences to UI elements
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        listViewFavourites = (ListView) rootView.findViewById(R.id.listViewFavourites);
        favouritesListAdapter = new FavouritesListAdapter(getApplicationContext(), savedNames);
        listViewFavourites.setAdapter(favouritesListAdapter);

        textViewName = (TextView) rootView.findViewById(R.id.textViewName);
        maleCheckbox = (CheckBox)rootView.findViewById(R.id.maleCheckbox);
        maleCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckboxChecked(view);
            }
        });
        femaleCheckbox = (CheckBox)rootView.findViewById(R.id.femaleCheckbox);
        femaleCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckboxChecked(view);
            }
        });
        searchEditText = (EditText)rootView.findViewById(R.id.searchEditText);

        // Add an event handler for when the text in the search box is updated
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * An event handler that is triggered when a checkbox is checked. Ensures that at least 1 checkbox is checked.
     * Triggers a database query reflecting the updated UI state
     * @param v - the checkox object that was checked
     */
    public void onCheckboxChecked(View v) {
        // Ensure that at least 1 checkbox is checked
        if (!maleCheckbox.isChecked() && !femaleCheckbox.isChecked()) {
            if (v.getId() == R.id.maleCheckbox) {
                femaleCheckbox.setChecked(true);
            } else if (v.getId() == R.id.femaleCheckbox) {
                maleCheckbox.setChecked(true);
            }
        }
        applyFilters(); // Update the filtering results
    }

    /**
     * Applies the filters set by the user in the Facourites fragment and queries the database. The adapter's state is then updated.
     */
    public void applyFilters(){
        if(maleCheckbox.isChecked() && !femaleCheckbox.isChecked()){
            favouritesListAdapter.setFavNames(realmService.getNamesWithTextAndGender(searchEditText.getText().toString(), Gender.MALE.ordinal()));
            favouritesListAdapter.notifyDataSetChanged();

        }
        else if(!maleCheckbox.isChecked() && femaleCheckbox.isChecked()){
            favouritesListAdapter.setFavNames(realmService.getNamesWithTextAndGender(searchEditText.getText().toString(), Gender.FEMALE.ordinal()));
            favouritesListAdapter.notifyDataSetChanged();
        }
        else if(maleCheckbox.isChecked() && femaleCheckbox.isChecked()){
            favouritesListAdapter.setFavNames(realmService.getNamesWithText(searchEditText.getText().toString()));
            favouritesListAdapter.notifyDataSetChanged();
        }
    }


}
