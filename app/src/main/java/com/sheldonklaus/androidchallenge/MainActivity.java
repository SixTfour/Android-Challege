package com.sheldonklaus.androidchallenge;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
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

        //Make API call to URL and set users & ListView
        requestQueue.add(createUsersRequest(url));
    }

    //Create JsonObjectRequest to get users
    private JsonObjectRequest createUsersRequest(String url) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override

            public void onResponse(JSONObject response) {
                users = createUsers(response);
                UserListAdapter userListAdapter = new UserListAdapter(mContext, users);
                listView.setAdapter(userListAdapter);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Connection Error", error.getMessage());
            }
        });

        return jsObjRequest;
    }

    private ArrayList<User> createUsers(JSONObject jsonObject) {
        JSONArray jsonUsers;
        ArrayList<User> users = new ArrayList<User>();

        //Get users from JSONObject jsonUsers and add them to users ArrayList<User> users
        try {
            jsonUsers = jsonObject.getJSONArray("items");
            ArrayList<JSONObject> usersToParse = new ArrayList(jsonUsers.length());

            for(int i=0;i < jsonUsers.length();i++){
                usersToParse.add(jsonUsers.getJSONObject(i));
            }

            for(JSONObject user : usersToParse) {
                Type badgeType = new TypeToken<HashMap<String, Integer>>() {}.getType();
                Map<String, Integer> badges = new Gson().fromJson(user.getJSONObject("badge_counts").toString(), badgeType);

                User newUser = new User(user.getString("display_name"), badges, user.getString("profile_image"));

                users.add(newUser);
            }
        } catch (JSONException e) {
            Log.e("JSONParseError", "Failed to get users from jsonObject", e);
        }

        return users;
    }
}
