package com.version.stutijindal.approject;


/*
Created by Stuti Jindal on 04/10/17

Fragment Features:
-Recycler View
-CardViews
-Timer
-ClockOut Button
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ScheduleFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int total;
    RecyclerView mRecyclerView;
    ShiftData shiftData;
    LinearLayoutManager mLayoutManager;
    MenuItem sortItem1;
    MyFirebaseRecylerAdapter myFirebaseRecylerAdapter;
    Button clockOut;
    TextView timerValue;
    private Handler customHandler = new Handler();
    long updatedTime = 0L;
    private long startTime = 0L;
    Intent intent;
    String timestampOut;
    private DatabaseReference mDatabase;
    public static String key,name,pic,id, timestampIn;
    String newKey;

    private OnFragmentInteractionListener mListener;

    public ScheduleFragment() {
    }

    public static ScheduleFragment newInstance(String mParam1,String mname,String mpic, String mid, String mtimestampIn) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        key=mParam1;
        name=mname;
        pic=mpic;
        id=mid;
        timestampIn=mtimestampIn;
        args.putString(ARG_PARAM1, mParam1);
        fragment.setArguments(args);
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
        View view= inflater.inflate(R.layout.fragment_schedule, container, false);
        clockOut=(Button)view.findViewById(R.id.clockOut);
        timerValue=(TextView)view.findViewById(R.id.timerValue);

        startTime=SystemClock.uptimeMillis();

        clockOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                timestampOut= new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Delete from Current node
                mDatabase.child("Shifts").child("Current").child(key).removeValue();
                //Add to Completed Node
                newKey = mDatabase.child("Shifts").child("Completed").push().getKey();
                Shifts shift = new Shifts(newKey,timestampIn, timestampOut, name, pic, id);
                Log.d("New Key",""+newKey);
                mDatabase.child("Shifts").child("Completed").child(newKey).setValue(shift);

                intent = new Intent(getActivity(), MainActivity.class);
                Bundle b = new Bundle();
                b.putString("key","fragment3");
                intent.putExtras(b);
                startActivity(intent);

                Toast.makeText(getActivity(), "You have clocked out of your shift!",
                        Toast.LENGTH_LONG).show();
            }
        });


        //Timer
        customHandler.postDelayed(updateTimerThread, 0);

        DatabaseReference childRef =
                FirebaseDatabase.getInstance().getReference().child("Shifts").child("Current").getRef();

        myFirebaseRecylerAdapter = new MyFirebaseRecylerAdapter(com.version.stutijindal.approject.Shifts.class,
                R.layout.cardview, MyFirebaseRecylerAdapter.ShiftViewHolder.class,
                childRef, getContext());

        shiftData = new ShiftData();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardList);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mRecyclerView.setAdapter(myFirebaseRecylerAdapter);
        if (shiftData.getSize() == 0) {
            shiftData.setAdapter(myFirebaseRecylerAdapter);
            shiftData.setContext(getActivity());//getApplicationContext()-activity is used
            shiftData.initializeDataFromCloud();
            Log.d("Test","Initialize completed");
        }

        myFirebaseRecylerAdapter.setOnItemClickListener(new MyFirebaseRecylerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, final int position) {
            }

            @Override
            public void onOverflowMenuClick(View v, final int position) {
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            updatedTime = SystemClock.uptimeMillis()-startTime;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hr= mins / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + hr+ ":" + mins + ":" + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }
    };

}
