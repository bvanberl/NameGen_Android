package com.vanberlo.blake.newname_android.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.Models.Name;
import com.vanberlo.blake.newname_android.R;
import com.vanberlo.blake.newname_android.RealmService;

import java.util.ArrayList;

import io.realm.RealmResults;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavouritesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FavouritesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RealmService realmService;
    private RealmResults<Name> savedNames;

    private ArrayList<String> favNames;
    private ListView listViewFavourites;
    private ArrayAdapter<String> stringArrayAdapter;

    public FavouritesFragment() {
        // Required empty public constructor
        realmService = new RealmService();
        savedNames = realmService.getAllNames();

        // TEST INSERTING A NAME
        // realmService.insertName( "Blake", Gender.MALE); // Test name
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        listViewFavourites = (ListView) rootView.findViewById(R.id.listViewFavourites);

        // Get all the names from the Realm DB
        favNames = new ArrayList<String>();
        for(int i = 0; i < savedNames.size(); i++){
            favNames.add(savedNames.get(i).getName());
        }
        stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, favNames);
        listViewFavourites.setAdapter(stringArrayAdapter);

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
}
