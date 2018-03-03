package com.sheldonklaus.androidchallenge;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<User> listAdapter;
    protected String url = "https://api.stackexchange.com/2.2/users?site=stackoverflow";
    private Context mContext;
    public ArrayList<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        listView = findViewById(R.id.list);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                users = getUsers(response);
                CustomAdapter customAdapter = new CustomAdapter();
                listView.setAdapter(customAdapter);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
            }
        });

        requestQueue.add(jsObjRequest);
    }

    private void setUserPhoto(String url, ImageView imageView) {
        new DownloadImageTask(imageView).execute(url);
    }

    private ArrayList<User> getUsers(JSONObject jsonObject) {
        JSONArray jsonUsers;
        ArrayList<User> users = new ArrayList<User>();

        try {
            jsonUsers = jsonObject.getJSONArray("items");

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<JSONObject>>(){}.getType();

            ArrayList<JSONObject> usersToParse = new ArrayList(jsonUsers.length());

            for(int i=0;i < jsonUsers.length();i++){
                usersToParse.add(jsonUsers.getJSONObject(i));
            }

            for(JSONObject user : usersToParse) {
                Type forBadges = new TypeToken<HashMap<String, Integer>>() {}.getType();
                Map<String, Integer> badges = new Gson().fromJson(user.getJSONObject("badge_counts").toString(), forBadges);

                User newUser = new User(user.getString("display_name"), badges, user.getString("profile_image"));

                users.add(newUser);
            }
        } catch (JSONException e) {
            Log.e("JSONUSERS", "Failed to get jsonUsers", e);
        }

        return users;
    }
    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.user_layout, null);

            ImageView imageView = view.findViewById(R.id.profile_image);
            TextView textView_name = view.findViewById(R.id.name);
            TextView textView_bronze = view.findViewById(R.id.bronze);
            TextView textView_silver = view.findViewById(R.id.silver);
            TextView textView_gold = view.findViewById(R.id.gold);

            //Setting values
            new DownloadImageTask(imageView).execute(users.get(i).getImageUrl());
            textView_name.setText(users.get(i).getName());
            textView_bronze.setText(users.get(i).getBadges().get("bronze").toString());
            textView_silver.setText(users.get(i).getBadges().get("silver").toString());
            textView_gold.setText(users.get(i).getBadges().get("gold").toString());

            return view;
        }
    }
}
