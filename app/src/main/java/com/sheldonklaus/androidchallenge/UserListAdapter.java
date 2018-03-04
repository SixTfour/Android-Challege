package com.sheldonklaus.androidchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sheld on 3/3/2018.
 */

//Custom BaseAdapter class to be attached to the ListView
public class UserListAdapter extends ArrayAdapter {

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<User> users;

    public UserListAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.user_layout, users);

        this.context = context;
        this.users = users;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.user_layout, null);

        //Initialize text and image views
        ImageView imageView = view.findViewById(R.id.profile_image);
        TextView textView_name = view.findViewById(R.id.name);
        TextView textView_bronze = view.findViewById(R.id.bronze);
        TextView textView_silver = view.findViewById(R.id.silver);
        TextView textView_gold = view.findViewById(R.id.gold);

        //Setting values
        textView_name.setText(users.get(i).getName());
        textView_bronze.setText(users.get(i).getBadges().get("bronze").toString());
        textView_silver.setText(users.get(i).getBadges().get("silver").toString());
        textView_gold.setText(users.get(i).getBadges().get("gold").toString());

        //Setting and caching image with Glide
        GlideApp.with(context)
                .load((users.get(i).getImageUrl()))
                .placeholder(R.drawable.preloader)
                .into(imageView);

        return view;
    }
}