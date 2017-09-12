package com.version.stutijindal.approject; //change the package name to your project's package name


/*
Activity Features:
-ClearFocus from EditText
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    EditText userNameET;
    EditText passwordET;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 123;
    private static final String USER_CREATION_SUCCESS =  "Successfully created user";
    private static final String USER_CREATION_ERROR =  "User creation error";
    private static final String EMAIL_INVALID =  "email is invalid :";

    private String key;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("click_action")) {
            Log.e("here1", "here1");
            String notification = intent.getStringExtra("click_action");
            Notifications notifications = new Notifications(LoginActivity.this);
            try {
                notifications.saveNotification(notification);
                for(int i=0; i<notifications.getNotifications().size(); i++){
                    Log.e("Msg", notifications.getNotifications().get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            key = "notification";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.e("here2", "here2");
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);  //Replace MainActivity.class with your launcher class from previous assignments
                    if (getIntent().hasExtra("click_action")) {
                        Log.e("here1", "here1");
                        String notification = getIntent().getStringExtra("click_action");
                        Notifications notifications = new Notifications(LoginActivity.this);
                        try {
                            notifications.saveNotification(notification);
                            for(int i=0; i<notifications.getNotifications().size(); i++){
                                Log.e("Msg", notifications.getNotifications().get(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myIntent.putExtra("key", "notification");
                        LoginActivity.this.startActivity(myIntent);
                    } else
                    LoginActivity.this.startActivity(myIntent);
                }else{

                }

            }
        };

        userNameET = (EditText)findViewById(R.id.edit_text_email);
        passwordET = (EditText)findViewById(R.id.edit_text_password);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                        ))
                                .build(),
                        RC_SIGN_IN);
            }
        });

        Button createButton = (Button) findViewById(R.id.button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {

                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class); //Replace MainActivity.class with your launcher class from previous assignments
                LoginActivity.this.startActivity(myIntent);

                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
//                    showSnackbar("Sign in cancelled");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar("No network connnection");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
//                    showSnackbar("Please enter valid username and password!");
                    return;
                }
            }

//            showSnackbar("Please enter valid username and password!");
        }
    }

    public void showSnackbar(String s){
        Snackbar snackbar = Snackbar.make(userNameET,s,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    // Validate email address for new accounts.
    private boolean isEmailValid(String email) {
        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            userNameET.setError(EMAIL_INVALID);
            return false;
        }
        return true;
    }

    // create a new user in Firebase
    public void createUser() {
        if(userNameET.getText() == null ||  !isEmailValid(userNameET.getText().toString())) {
            Snackbar snackbar = Snackbar.make(userNameET, "Please enter a valid username!", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }

        if(passwordET.getText() == null || passwordET.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(userNameET, "Please enter a password!", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(userNameET.getText().toString(),passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(userNameET, "Your account has been created!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else{
                    Snackbar snackbar = Snackbar.make(userNameET, "Please enter valid username and password!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });



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
}
