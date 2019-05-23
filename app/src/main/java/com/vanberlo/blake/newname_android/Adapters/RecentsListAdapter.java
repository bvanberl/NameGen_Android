package com.vanberlo.blake.newname_android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.Models.Name;
import com.vanberlo.blake.newname_android.R;
import com.vanberlo.blake.newname_android.RealmService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecentsListAdapter extends ArrayAdapter<Name> {

    Context context;
    List<Name> data;
    int resourceLayout;
    private static LayoutInflater inflater = null;
    RealmService realmService;

    public RecentsListAdapter(Context context, ArrayList<Name> list) {
        super(context, 0 , list);
        this.context = context;
        this.data = list;
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
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer)v.getTag();
                onFavouriteBtnClicked(pos, v);
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Integer pos = (Integer)v.getTag();
                onSendBtnClicked(pos);
            }
        });
        return convertView;
    }


    public void onFavouriteBtnClicked(int idx, View v){
        String selectedName = data.get(idx).getName();
        Gender gender = data.get(idx).getGender();
        realmService.insertName( selectedName, gender);

        ImageButton favBtn = (ImageButton)v;
        favBtn.setEnabled(false);
        favBtn.setColorFilter(0xff8D30A6, PorterDuff.Mode.SRC_ATOP);

        CharSequence text = selectedName + " saved to Favourites!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void onSendBtnClicked(int idx){
        String selectedName = data.get(idx).getName();
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String shareBody = "I just created the name "+selectedName+" using ____________";
        String shareSub = "Check out my new name!";
        myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        context.startActivity(Intent.createChooser(myIntent,"Share Using..."));
    }

}
