package com.sheldonklaus.androidchallenge;

import java.util.Map;

/**
 * Created by klaussx on 3/2/2018.
 */

public class User {
    private String Name;
    private Map <String, Integer> Badges;
    private String ImageUrl;

    public User(String name, Map<String, Integer> badges, String imageUrl) {
        Name = name;
        Badges = badges;
        ImageUrl = imageUrl;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setBadges(Map<String, Integer> badges) {
        Badges = badges;
    }
    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
