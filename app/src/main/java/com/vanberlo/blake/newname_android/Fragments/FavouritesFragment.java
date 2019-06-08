package com.vanberlo.blake.newname_android.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vanberlo.blake.newname_android.Adapters.FavouritesListAdapter;
import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.Models.Name;
import com.vanberlo.blake.newname_android.R;
import com.vanberlo.blake.newname_android.RealmService;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavouritesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FavouritesFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private RealmService realmService;
    private RealmResults<Name> savedNames;

    private ArrayList<Name> favNames;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

    public void onCheckboxChecked(View v) {
        if (!maleCheckbox.isChecked() && !femaleCheckbox.isChecked()) {
            if (v.getId() == R.id.maleCheckbox) {
                femaleCheckbox.setChecked(true);
            } else if (v.getId() == R.id.femaleCheckbox) {
                maleCheckbox.setChecked(true);
            }
        }
        applyFilters();
    }

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
