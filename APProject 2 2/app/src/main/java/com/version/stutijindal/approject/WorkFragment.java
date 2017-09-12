package com.version.stutijindal.approject;

/*
Created by Stuti Jindal on 04/10/17

Fragment Features:
-Barcode Scanner
-Scanner Password="password"
-Write to Firebase Database
 */


import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.version.stutijindal.approject.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = "password";
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    FirebaseUser user;
    String name, email, id;
    String timestampIn,timestampOut,username, userpic, userid, newKey;
    Uri photoUrl;
    Intent intent;
    private DatabaseReference mDatabase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WorkFragment() {
        // Required empty public constructor
    }

    public static WorkFragment newInstance() {
        WorkFragment fragment = new WorkFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_work, container, false);

        Button scanBarcodeButton = (Button) view.findViewById(R.id.scan_barcode_button);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });

        Button mapsActivity = (Button) view.findViewById(R.id.start_map_activity);
        mapsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MapsActivity.class));
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            switch(requestCode) {
                case BARCODE_READER_REQUEST_CODE:
                    if (resultCode == CommonStatusCodes.SUCCESS) {
                        if (data != null) {
                            Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                            Point[] p = barcode.cornerPoints;

                            if(barcode.displayValue.equals("password")){

                                user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    name = user.getDisplayName();
                                    email = user.getEmail();
                                    photoUrl = user.getPhotoUrl();
                                    id=user.getUid();
                                }
                                username= name;
                                if(photoUrl!= null)
                                    userpic= photoUrl.toString();
                                else
                                    userpic= Uri.parse("android.resource://com.example.stutijindal.approject/" + R.drawable.com_facebook_profile_picture_blank_square).toString();

                                userid=id;
                                timestampIn = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                                timestampOut="";

                                Log.d("New Shift",""+username+userpic+userid+timestampOut);

                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                newKey = mDatabase.child("Shifts").child("Current").push().getKey();
                                Log.d("New Key",""+newKey);
                                Shifts shift = new Shifts(newKey, timestampIn, timestampOut, username, userpic, id);

                                mDatabase.child("Shifts").child("Current").child(newKey).setValue(shift);

                                Log.d("Office",""+barcode.displayValue);
                                intent = new Intent(getActivity(), RecyclerActivity.class);
                                Bundle b = new Bundle();
                                b.putString("Key",newKey);
                                b.putString("Name",username);
                                b.putString("Pic",userpic);
                                b.putString("Id", id);
                                b.putString("TimestampIn", timestampIn);
                                intent.putExtras(b);
                                startActivity(intent);

                                Toast.makeText(getActivity(), "You have clocked in for your shift!",
                                        Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getActivity(), "This barcode is invalid! ",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                default:
                    Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                            CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }catch (Exception e){
            Log.e("Barcode", "Exception in OnActivityResult:"+e.getMessage());
        }
    }
}
