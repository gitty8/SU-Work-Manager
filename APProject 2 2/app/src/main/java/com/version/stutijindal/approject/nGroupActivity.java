package com.version.stutijindal.approject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class nGroupActivity extends AppCompatActivity implements Fragment_List.OnListItemSelectedListener {

    private Fragment mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_group);

        if (savedInstanceState != null) {
            if (getSupportFragmentManager().getFragment(savedInstanceState, "mContent") != null) {
                mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
                Log.d("message", "old");
            } else {
                mContent = nGroupFragment.newInstance();
                Log.d("message", "new");
            }
        }
        else {
            mContent = nGroupFragment.newInstance();
            Log.d("message", "new");
        }

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.group_container, mContent)
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onListItemSelected() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();
        b.putString("key","fragment4");
        intent.putExtras(b);
        startActivity(intent);
    }

}
