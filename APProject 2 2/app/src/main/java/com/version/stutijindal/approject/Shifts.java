package com.version.stutijindal.approject;

import android.util.Log;
import java.io.Serializable;

/**
 * Created by Stuti Jindal on 4/19/17.
 */

public class Shifts implements Serializable{
    String timestampIn;
    String timestampOut;
    String username;
    String userpic;
    String id;
    String key;


   public Shifts(){

   }

    public Shifts(String key, String timestampIn, String timestampOut, String username, String userpic, String id){
        this.id = id;
        this.userpic= userpic;
        this.timestampIn= timestampIn;
        this.timestampOut= timestampOut;
        this.username= username;
        this.key= key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        Log.d("UserName",""+username);
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getTimestampIn() {
        return timestampIn;
    }

    public void setTimestampIn(String timestampIn) {
        this.timestampIn = timestampIn;
    }

    public String getTimestampOut() {
        return timestampOut;
    }
    public void setTimestampOut(String timestampOut) {
        this.timestampOut = timestampOut;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

}
