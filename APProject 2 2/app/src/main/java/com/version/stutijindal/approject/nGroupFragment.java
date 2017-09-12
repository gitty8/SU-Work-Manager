package com.version.stutijindal.approject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.version.stutijindal.approject.R.color.wallet_bright_foreground_disabled_holo_light;
import static com.version.stutijindal.approject.R.color.wallet_holo_blue_light;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link nGroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link nGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nGroupFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseUser user;
    String name, email, id;
    Uri photoUrl;
    FloatingActionButton fab;
    EditText text;
    String timestampIn, timestampOut, username, userpic, userid, newKey, date, description, subbedUser;
    TimePicker tp1, tp2;
    DatePicker dp;
    private DatabaseReference mDatabase;
    Button submit;
    Switch aSwitch;
    Intent intent;

    private OnFragmentInteractionListener mListener1;

    public nGroupFragment() {
    }


    public static nGroupFragment newInstance() {
        nGroupFragment fragment = new nGroupFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_n_group, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        dp = (DatePicker) view.findViewById(R.id.datePicker2);
        tp1 = (TimePicker) view.findViewById(R.id.timePicker);
        tp2 = (TimePicker) view.findViewById(R.id.timePicker2);
        text=(EditText) view.findViewById(R.id.editText);
        aSwitch=(Switch) view.findViewById(R.id.switch1);

        final Fragment_List.OnListItemSelectedListener mListener;
        try {
            mListener = (Fragment_List.OnListItemSelectedListener) getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }


        aSwitch.setChecked(false);
        //attach a listener to check for changes in state
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    submit.setEnabled(true);
                    submit.setBackgroundColor(getResources().getColor(wallet_holo_blue_light));

                }else{
                    submit.setEnabled(false);
                    submit.setBackgroundColor(getResources().getColor(wallet_bright_foreground_disabled_holo_light));
                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListItemSelected();

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();
            id = user.getUid();
        }
        username = name;
        if (photoUrl != null)
            userpic = photoUrl.toString();
        else
            userpic = Uri.parse("android.resource://com.example.stutijindal.approject/" + R.drawable.com_facebook_profile_picture_blank_square).toString();
        userid = id;



        submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = Integer.toString(dp.getYear());
                String month = Integer.toString(dp.getMonth() + 1);
                String day = Integer.toString(dp.getDayOfMonth());
                String hour = Integer.toString(tp1.getCurrentHour());
                String minutes = Integer.toString(tp1.getCurrentMinute());
                Log.d("tps1",hour+minutes);
                timestampIn = hour + ":" + minutes;

                hour = Integer.toString(tp2.getCurrentHour());
                 minutes = Integer.toString(tp2.getCurrentMinute());
                Log.d("tps2",hour+minutes);
                timestampOut = hour + ":" + minutes;

                date = year + "." + month + "." + day;
                description=text.getText().toString();
                subbedUser="";

                mDatabase = FirebaseDatabase.getInstance().getReference();
                newKey = mDatabase.child("Shifts").child("Subs").push().getKey();
                Groups sub = new Groups( newKey, timestampIn, timestampOut, date, description, subbedUser, username, userpic, id);

                mDatabase.child("Shifts").child("Subs").child(newKey).setValue(sub);

                mListener.onListItemSelected();
                Toast.makeText(getActivity(), "You have subbed your shift successfully!",
                        Toast.LENGTH_LONG).show();

            }
        });
        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public interface OnListItemSelectedListener{
        public void onListItemSelected(HashMap<String, ?> movie);
    }
}
