package com.vanberlo.blake.newname_android.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class FavouritesFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private RealmService realmService;
    private RealmResults<Name> savedNames;

    private ArrayList<Name> favNames;
    private ListView listViewFavourites;
    private FavouritesListAdapter favouritesListAdapter;

    private TextView textViewName;

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

    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
