package com.vanberlo.blake.newname_android.Adapters;

import android.content.Context;
import android.content.Intent;
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

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmCollection;


public class FavouritesListAdapter extends RealmBaseAdapter {

    Context context;
    OrderedRealmCollection<Name> favNames;
    int resourceLayout;
    RealmService realmService;
    private static LayoutInflater inflater = null;

    public FavouritesListAdapter(Context context, OrderedRealmCollection<Name> list) {
        super(list);
        this.context = context;
        this.favNames = list;
        this.resourceLayout = R.layout.favourites_list_item_view;
        realmService = new RealmService();
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return favNames.size();
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
        nameText.setText(favNames.get(position).getName());
        ImageButton delBtn = (ImageButton)convertView.findViewById(R.id.delete_list_button);
        delBtn.setTag(position);
        ImageButton shareBtn = (ImageButton)convertView.findViewById(R.id.share_list_button);
        shareBtn.setTag(position);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer)v.getTag();
                onDeleteBtnClicked(pos);
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
    

    public void onSendBtnClicked(int idx){
        String selectedName = favNames.get(idx).getName();
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String shareBody = "I just created the name "+selectedName+" using ____________";
        String shareSub = "Check out my new name!";
        myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        context.startActivity(Intent.createChooser(myIntent,"Share Using..."));

    }

    public void onDeleteBtnClicked(int idx){
        Name selectedName = favNames.get(idx);
        String nameText = selectedName.getName();
        long id = selectedName.getId();
        realmService.deleteNameWithId(id);

        CharSequence text = nameText + " deleted from favourites.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
