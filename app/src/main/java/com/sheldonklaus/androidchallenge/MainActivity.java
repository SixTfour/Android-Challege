package com.sheldonklaus.androidchallenge;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    TextView mTxtDisplay;
    String url = "https://api.stackexchange.com/2.2/users?site=stackoverflow";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        mTxtDisplay = findViewById(R.id.text);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<User> users = getUsers(response);
                mTxtDisplay.setText(users.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
            }
        });

        requestQueue.add(jsObjRequest);
    }

    private ArrayList<User> getUsers(JSONObject jsonObject) {
        JSONArray jsonUsers;
        ArrayList<User> users = new ArrayList<User>();

        try {
            jsonUsers = jsonObject.getJSONArray("items");

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<JSONObject>>(){}.getType();

            ArrayList<JSONObject> usersToParse = gson.fromJson(jsonUsers.toString(), type);

            for(JSONObject user : usersToParse) {
                Type forBadges = new TypeToken<HashMap<String, Integer>>() {}.getType();
                Map<String, Integer> badges = new Gson().fromJson(user.getJSONObject("badge_counts").toString(), forBadges);

                User newUser = new User(user.getString("name"), badges, user.getString("imageUrl"));

                users.add(newUser);
            }
        } catch (JSONException e) {
            Log.e("JSONUSERS", "Failed to get jsonUsers", e);
        }

        return users;
    }
}
