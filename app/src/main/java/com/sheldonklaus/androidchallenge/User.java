package com.sheldonklaus.androidchallenge;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * Created by klaussx on 3/2/2018.
 */

public class User {
    //Simple user class
    private String Name;
    private Map <String, Integer> Badges;
    private String ImageUrl;

    public User(String name, Map<String, Integer> badges, String imageUrl) {
        Name = name;
        Badges = badges;
        ImageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public Map<String, Integer> getBadges() {
        return Badges;
    }
    public String getImageUrl() {
        return ImageUrl;
    }
}
