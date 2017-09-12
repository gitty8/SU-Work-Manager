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

public class GroupData {

     List<Map<String,?>> grouplist;
        DatabaseReference mRef;
        GFirebaseRecylerAdapter gFirebaseRecylerAdapter;
        Context mContext;

        public void setAdapter(GFirebaseRecylerAdapter mAdapter) {
            gFirebaseRecylerAdapter = mAdapter;
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

        public List<Map<String, ?>> getGrouplist() {
            return grouplist;
        }

        public int getSize(){
            return grouplist.size();
        }

        public HashMap getItem(int i){
            Log.d("grouplist",""+ grouplist);
            if (i >=0 && i < grouplist.size()){
                return (HashMap) grouplist.get(i);
            } else return null;
        }


        public void onItemRemovedFromCloud(HashMap item){
            int position = -1;
            String key=(String)item.get("key");
            for(int i=0; i< grouplist.size();i++){
                HashMap shift = (HashMap)grouplist.get(i);
                String mid = (String)shift.get("key");
                if(mid.equals(key)){
                    position= i;
                    break;
                }
            }
            if(position != -1){
                grouplist.remove(position);
//                Toast.makeText(mContext, "Item Removed:" + key, Toast.LENGTH_SHORT).show();

            }
        }

        public void onItemAddedToCloud(HashMap item){

            int insertPosition = 0;
            String key=(String)item.get("key");
            Log.d("onItemAddedToCloud key", key);
            for(int i=0;i<grouplist.size();i++){
                HashMap shift = (HashMap)grouplist.get(i);
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

            grouplist.add(insertPosition,item);


        }

        public void onItemUpdatedToCloud(HashMap item){
            String key=(String)item.get("key");
            for(int i=0;i<grouplist.size();i++){
                HashMap shift = (HashMap)grouplist.get(i);
                String mid = (String)shift.get("key");
                if(mid.equals(key)){
                    grouplist.remove(i);
                    grouplist.add(i,item);
                    Log.d("My Test: NotifyChanged",key);

                    break;
                }
            }

        }
        public void initializeDataFromCloud() {
            grouplist.clear();
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

        public GroupData(){

            grouplist = new ArrayList<Map<String,?>>();
            mRef = FirebaseDatabase.getInstance().getReference().child("Shifts").child("Subs").getRef();
            gFirebaseRecylerAdapter = null;
            mContext = null;

        }


//        public int findFirst(String query){
//
//            for(int i=0;i<grouplist.size();i++){
//                HashMap hash = (HashMap)getGrouplist().get(i);
//                String logs=((String)hash.get("username")).toLowerCase();
//                Log.d("findfirst",logs);
//                if(((String)hash.get("username")).toLowerCase().contains(query.toLowerCase())){
//
//                    return i;
//                }
//            }
//            return 0;
//        }
}

