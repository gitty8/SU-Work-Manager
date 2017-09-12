package com.version.stutijindal.approject;

/**
 * Created by sahilshah619 on 05-04-2017.
 */
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import org.acra.*;
import org.acra.annotation.*;
@ReportsCrashes( // will not be used
        mailTo = "crash0er@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class Application extends android.app.Application {
    private static Context context;
    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        super.onCreate();
        context = this;
        if(FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }

    public static Context getInstance() {
        return context;
    }
}