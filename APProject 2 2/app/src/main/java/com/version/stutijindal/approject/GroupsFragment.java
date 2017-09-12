package com.version.stutijindal.approject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class GroupsFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private OnFragmentInteractionListener mListener;
    private DatabaseReference mDatabase;
    GFirebaseRecylerAdapter myFirebaseRecylerAdapter;
    GroupData groupData;
    RecyclerView mRecyclerView;
    ShiftData shiftData = new ShiftData();
    LinearLayoutManager mLayoutManager;
    FloatingActionButton button;
    Intent intent;
    DatabaseReference ref;
    String name, username;
    String timeIn, timeOut, description, date, userpic, subUsername;
    FirebaseUser user;
    String id,key,newKey, idNo;


    public GroupsFragment() {
    }


    public static GroupsFragment newInstance() {
        GroupsFragment fragment = new GroupsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_groups, container, false);

//        final Fragment_List.OnListItemSelectedListener mListener;
//        try {
//            mListener = (Fragment_List.OnListItemSelectedListener) getContext();
//        } catch (ClassCastException e) {
//            throw new ClassCastException("The hosting activity of the fragment" +
//                    "forgot to implement onFragmentInteractionListener");
//        }


        button = (FloatingActionButton)view.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                intent = new Intent(getActivity(), nGroupActivity.class);
                startActivity(intent);

//                mListener.onListItemSelected();

            }
        });
        DatabaseReference childRef =
                FirebaseDatabase.getInstance().getReference().child("Shifts").child("Subs").getRef();
        myFirebaseRecylerAdapter = new GFirebaseRecylerAdapter(com.version.stutijindal.approject.Groups.class,
                R.layout.gcardview, GFirebaseRecylerAdapter.GroupViewHolder.class,
                childRef, getContext());

        groupData = new GroupData();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.gcardList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mRecyclerView.setAdapter(myFirebaseRecylerAdapter);
        if (groupData.getSize() == 0) {
            groupData.setAdapter(myFirebaseRecylerAdapter);
            groupData.setContext(getActivity());//getApplicationContext()-activity is used
            groupData.initializeDataFromCloud();
            Log.d("Test","Initialize completed");
        }

        myFirebaseRecylerAdapter.setOnItemClickListener(new GFirebaseRecylerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, final int position) {
                HashMap<String, ?> group= (HashMap<String, ?>) groupData.getItem(position);
                Log.d("hashmap fetched","" +group);

                key=(String) group.get("key");
                timeIn = (String) group.get("timeIn");
                timeOut = (String) group.get("timeOut");
                description = (String) group.get("description");
                date = (String) group.get("date");
                username= (String)group.get("username");
                userpic= (String) group.get("userpic");
                id= (String) group.get("id");
                idNo= id;

                ref= groupData.getFireBaseRef();
                ref.child(key).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener(){

                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(Html.fromHtml("<font color='#0D1D37'>Pick Shift</font>"));
                        builder.setIcon(android.R.drawable.stat_notify_error);
                        builder.setMessage(Html.fromHtml("<font color='#0D1D37'>Are you sure, you want to pick this shift ?</font>"))
                                .setIcon(R.drawable.warn)
                                .setCancelable(false)
                                .setPositiveButton("Yes, Pick it!", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            name = user.getDisplayName();
                                        }
                                        subUsername= name;
                                        ref.child(key).removeValue();

                                        mDatabase = FirebaseDatabase.getInstance().getReference();
                                        newKey = mDatabase.child("Shifts").child("PickedSubs").push().getKey();
                                        Groups pickedsub = new Groups( newKey,  timeIn,  timeOut,  date,  description, subUsername,  username,  userpic, idNo);
                                        Log.d("New Key",""+newKey);
                                        mDatabase.child("Shifts").child("PickedSubs").child(newKey).setValue(pickedsub);

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });
                        builder.show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError){
                        Log.d("My test", "Read failed:" + databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onOverflowMenuClick(View v, final int position) {
            }
        });
        return view;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case R.id.menu_standalone_sortyear:
//                sortByYear();
//                return true;
//        }
        return false;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if (menu.findItem(R.id.menu_search) == null)
//            inflater.inflate(R.menu.menu_searcher, menu);
//
//        Log.d("Message",(String) getActivity().getTitle());
//
//        if (getActivity().getTitle().equals("Employee Manager") ) {
//            Log.d("Message",(String) getActivity().getTitle());
//            Drawable drawable = menu.findItem(R.id.menu_search).getIcon();
//            if (drawable != null) {
//                drawable.mutate();
//                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//            }
//        }

//        final SearchView search=(SearchView) menu.findItem(R.id.menu_search).getActionView();
//        Log.d("Message","OnMenuOptions");
//        search.setQueryHint("Search by Username...");
//        if (search!=null){
//            search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
//
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    return true;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String query) {
//
//                    Log.d("inside search", "");
//                    int position1 =groupData.findFirst(query);
//                    if (position1 > 0) {
//                        mRecyclerView.scrollToPosition(position1);
//                        Toast.makeText(getContext(), "Sub available by this name", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                        Toast.makeText(getContext(), "Sub is not available by this name", Toast.LENGTH_SHORT).show();
//                    return true;
//                }


//            });
//        }
        super.onCreateOptionsMenu(menu, inflater);
    }


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

}
