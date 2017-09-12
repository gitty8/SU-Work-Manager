package com.version.stutijindal.approject;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;

/**
 * Created by sahilshah619 on 01-05-2017.
 */

public class FireMsgService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Notifications notifications = new Notifications(this);
        try {
            notifications.saveNotification(remoteMessage.getNotification().getBody());
            for(int i=0; i<notifications.getNotifications().size(); i++){
                Log.e("Msg", notifications.getNotifications().get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
