package com.version.stutijindal.approject;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Stuti Jindal on 4/19/17.
 */

public class Groups implements Serializable{
    String timeIn;
    String timeOut;
    String subUsername;
    String username;
    String userpic;
    String id;
    String description;
    String date;
    String key;

   public Groups(){

   }

    public Groups(String key, String timeIn, String timeOut, String date, String description,String subUsername, String username, String userpic, String id){
        this.id = id;
        this.userpic= userpic;
        this.timeIn= timeIn;
        this.timeOut= timeOut;
        this.date=date;
        this.subUsername= subUsername;
        this.username= username;
        this.description= description;
        this.key= key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        Log.d("UserName",""+username);
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getSubUsername() {
        return subUsername;
    }

    public void setSubUsername(String subUsername) {
        this.subUsername = subUsername;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
