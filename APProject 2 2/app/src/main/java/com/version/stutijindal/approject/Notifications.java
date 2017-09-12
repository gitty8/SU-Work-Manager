package com.version.stutijindal.approject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by sahilshah619 on 01-05-2017.
 */

public class Notifications {
    Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String key = "notifications";

    public Notifications(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        if (!prefs.contains(key)) {
            editor.putString(key, "[]");
            editor.apply();
        }
    }

    public void saveNotification(String notification) throws JSONException {
        ArrayList<String> notifications = getNotifications();
        notifications.add(notification);
        editor.remove(key);
        JSONArray array = new JSONArray(notifications);
        editor.putString(key, array.toString());
        editor.apply();
    }

    public ArrayList<String> getNotifications() throws JSONException {
        ArrayList<String> notifications = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(prefs.getString(key, "[]"));
        for (int i = 0; i < jsonArray.length(); i++) {
            notifications.add(String.valueOf(jsonArray.get(i)));
        }
        return notifications;
    }
/*
    private ArrayList<String> getDefaultNotifications() {
        if (!prefs.contains(key)) {
            SortedSet<String> set = new TreeSet<>();
            editor.putStringSet(key, set);
            editor.apply();
        }
        ArrayList<String> notifications = new ArrayList<String>();
        return notifications;
    }*/
}