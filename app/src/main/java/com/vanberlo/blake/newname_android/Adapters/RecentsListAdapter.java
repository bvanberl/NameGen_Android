package com.vanberlo.blake.newname_android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.Models.Name;
import com.vanberlo.blake.newname_android.R;
import com.vanberlo.blake.newname_android.RealmService;

import java.util.ArrayList;
import java.util.List;

public class RecentsListAdapter extends ArrayAdapter<Name> {

    Context context;
    List<Name> data;
    List<Integer> favouriteIndices; // Indices of names selected by the user to favourite
    int resourceLayout;
    private static LayoutInflater inflater = null;
    RealmService realmService;

    /**
     * Constructor for the adapter
     * @param context - the current app context
     * @param list - the list of database results constituting the adapter's data
     */
    public RecentsListAdapter(Context context, ArrayList<Name> list) {
        super(context, 0 , list);
        this.context = context;
        this.data = list;
        this.favouriteIndices = new ArrayList<Integer>();
        this.resourceLayout = R.layout.recents_list_item_view;
        realmService = new RealmService();
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Set up UI for an individual item in the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(resourceLayout, parent, false);
        TextView nameText = (TextView) convertView.findViewById(R.id.name_text);
        nameText.setText(data.get(position).getName());
        ImageButton favBtn = (ImageButton)convertView.findViewById(R.id.favourite_list_button);
        favBtn.setTag(position);
        ImageButton shareBtn = (ImageButton)convertView.findViewById(R.id.share_list_button);
        shareBtn.setTag(position);

        // Set up an event handler for tapping the favourite button
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer)v.getTag();
                onFavouriteBtnClicked(pos, v);
            }
        });

        // If this is a newly added item to the list, increment all indices of items selected as favourites
        if(position == 0) {
            for (int i = 0; i < favouriteIndices.size(); i++) {
                favouriteIndices.set(i, favouriteIndices.get(i) + 1);
            }
        }

        // Set the colour of the favourite button if it was previously selected as a favourite name; otherwise, unset the colour
        if(favouriteIndices.contains(position)){
            favBtn.setEnabled(false); // Disable the favourite button
            favBtn.setColorFilter(0xff8D30A6, PorterDuff.Mode.SRC_ATOP); // Change the colour of the favourite button
        }
        else{
            favBtn.setEnabled(true); // Enable the Favourite button
            favBtn.clearColorFilter(); // Change the colour of the favourite button
        }

        // Set up an event handler for the Send button
        shareBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Integer pos = (Integer)v.getTag();
                onSendBtnClicked(pos);
            }
        });
        return convertView;
    }


    /**
     * Adds the name at the list index selected by the user to the favourites database
     * @param idx - the index of the selected name in the list
     * @param v - a reference to the view that was clicked
     */
    public void onFavouriteBtnClicked(int idx, View v){
        String selectedName = data.get(idx).getName();
        Gender gender = data.get(idx).getGender();
        realmService.insertName(selectedName, gender); // Insert the name into the Realm database

        // Change the colour of the button at the selected name's index
        ImageButton favBtn = (ImageButton)v;
        favBtn.setEnabled(false); // Disable the favourite button
        favBtn.setColorFilter(0xff8D30A6, PorterDuff.Mode.SRC_ATOP); // Change the colour of the favourite button
        favouriteIndices.add(idx);

        // Display a toast to the user, confirming that the name was saved to Favourites
        CharSequence text = selectedName + " saved to Favourites!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 250);
        toast.show();
    }

    /**
     * Create a send intent for the user to send the name at the selected index via an external app
     * @param idx - the index of the selected name in the list
     */
    public void onSendBtnClicked(int idx){
        String selectedName = data.get(idx).getName();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = "I just created the name "+ selectedName +" using NameGen!";
        String shareSub = "Check out my new name!";
        intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);

        context.startActivity(Intent.createChooser(intent,"Send using ..."));
    }


}
