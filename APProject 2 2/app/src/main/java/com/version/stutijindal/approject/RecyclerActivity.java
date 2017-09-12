package com.version.stutijindal.approject;

/*
Created by Stuti Jindal on 04/10/17

Activity Features:
 -Send Key Value to Schedule Fragment
 -BackButton disabled
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class RecyclerActivity extends AppCompatActivity{

    private Fragment mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        Bundle bundle = getIntent().getExtras();
        String key= bundle.getString("Key");
        String name= bundle.getString("Name");
        String pic= bundle.getString("Pic");
        String id= bundle.getString("Id");
        String timestampIn= bundle.getString("TimestampIn");

        if (savedInstanceState != null) {
            if (getSupportFragmentManager().getFragment(savedInstanceState, "mContent") != null) {
                mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
                Log.d("message", "old");
            } else {
                mContent = ScheduleFragment.newInstance(key,name,pic,id, timestampIn);
                Log.d("message", "new");
            }
        }
        else {
            mContent = ScheduleFragment.newInstance(key,name,pic,id, timestampIn);
            Log.d("message", "new");
        }

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recycler_container, mContent)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mContent.isAdded())
            getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    @Override
    public void onBackPressed() {
    }


}
