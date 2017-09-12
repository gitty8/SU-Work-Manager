package com.version.stutijindal.approject;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stuti Jindal on 4/19/17.
 */

public class ShiftData {

     List<Map<String,?>> Shiftlist;
        DatabaseReference mRef;
        MyFirebaseRecylerAdapter myFirebaseRecylerAdapter;
        Context mContext;

        public void setAdapter(MyFirebaseRecylerAdapter mAdapter) {
            myFirebaseRecylerAdapter = mAdapter;
        }

        public void removeItemFromServer(Map<String,?> shift){
            if(shift!=null){
                String key= (String)shift.get("key");
                mRef.child(key).removeValue();
            }
        }

        public void addItemToServer(Map<String,?> shift){
            if(shift!=null){
                String key = (String) shift.get("key");
                mRef.child(key).setValue(shift);
            }
        }

        public DatabaseReference getFireBaseRef(){
            return mRef;
        }
        public void setContext(Context context){mContext = context;}

        public List<Map<String, ?>> getShiftList() {
            return Shiftlist;
        }

        public int getSize(){
            return Shiftlist.size();
        }

        public HashMap getItem(int i){
            if (i >=0 && i < Shiftlist.size()){
                return (HashMap) Shiftlist.get(i);
            } else return null;
        }


        public void onItemRemovedFromCloud(HashMap item){
            int position = -1;
            String key=(String)item.get("key");
            for(int i=0; i< Shiftlist.size();i++){
                HashMap shift = (HashMap)Shiftlist.get(i);
                String mid = (String)shift.get("key");
                if(mid.equals(key)){
                    position= i;
                    break;
                }
            }
            if(position != -1){
                Shiftlist.remove(position);
//                Toast.makeText(mContext, "Item Removed:" + key, Toast.LENGTH_SHORT).show();

            }
        }

        public void onItemAddedToCloud(HashMap item){
            int insertPosition = 0;
            String key=(String)item.get("key");
            for(int i=0;i<Shiftlist.size();i++){
                HashMap shift = (HashMap)Shiftlist.get(i);
                String mid = (String)shift.get("key");
                if(mid.equals(key)){
                    return;
                }
                if(mid.compareTo(key)<0){
                    insertPosition=i+1;
                }else{
                    break;
                }
            }

            Shiftlist.add(insertPosition,item);
            // Toast.makeText(mContext, "Item added:" + key, Toast.LENGTH_SHORT).show();

        }

        public void onItemUpdatedToCloud(HashMap item){
            String key=(String)item.get("key");
            for(int i=0;i<Shiftlist.size();i++){
                HashMap shift = (HashMap)Shiftlist.get(i);
                String mid = (String)shift.get("key");
                if(mid.equals(key)){
                    Shiftlist.remove(i);
                    Shiftlist.add(i,item);
                    Log.d("My Test: NotifyChanged",key);

                    break;
                }
            }

        }
        public void initializeDataFromCloud() {
            Shiftlist.clear();
            mRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
                @Override
                public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    Log.d("MyTest: OnChildAdded", dataSnapshot.toString());
                    HashMap<String,String> shift = (HashMap<String,String>)dataSnapshot.getValue();
                    Log.d("MyTest",""+shift);
                    onItemAddedToCloud(shift);
                }

                @Override
                public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    Log.d("MyTest: OnChildChanged", dataSnapshot.toString());
                    HashMap<String,String> shift = (HashMap<String,String>)dataSnapshot.getValue();
                    onItemUpdatedToCloud(shift);
                }

                @Override
                public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    Log.d("MyTest: OnChildRemoved", dataSnapshot.toString());
                    HashMap<String,String> shift = (HashMap<String,String>)dataSnapshot.getValue();
                    onItemRemovedFromCloud(shift);
                }

                @Override
                public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public ShiftData(){

            Shiftlist = new ArrayList<Map<String,?>>();
            mRef = FirebaseDatabase.getInstance().getReference().child("Shifts").child("Current").getRef();
            myFirebaseRecylerAdapter = null;
            mContext = null;

        }


//        public int findFirst(String query){
//
//            for(int i=0;i<Shiftlist.size();i++){
//                HashMap hm = (HashMap)getShiftList().get(i);
//                if(((String)hm.get("name")).toLowerCase().contains(query.toLowerCase())){
//                    return i;
//                }
//            }
//            return 0;
//
//        }
}

