package com.version.stutijindal.approject; //change the package name to your project's package name

/*
Created by Stuti Jindal on 04/10/17

-Groups Fragment
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class GFirebaseRecylerAdapter extends FirebaseRecyclerAdapter<Groups, GFirebaseRecylerAdapter.GroupViewHolder> {

    private Context mContext;
    static OnItemClickListener mItemClickListener;


    public GFirebaseRecylerAdapter(Class<Groups> modelClass, int modelLayout,
                                   Class<GroupViewHolder> holder, DatabaseReference ref, Context context) {

        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
    }


    public interface OnItemClickListener {
        public void onOverflowMenuClick(View v, final int position);
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener= mItemClickListener;
    }

    @Override
    protected void populateViewHolder(GroupViewHolder groupViewHolder, Groups group, int i) {

        //TODO: Populate viewHolder by setting the shift attributes to cardview fields
        groupViewHolder.getAdapterPosition();
        Log.d("Data from cloud",""+group.getUsername());
        groupViewHolder.vName.setText(group.getUsername());
        groupViewHolder.vDate.setText(group.getDate());
        groupViewHolder.vTimestamp.setText(group.getTimeIn()+ "\tTo\t"+group.getTimeOut());
        Picasso.with(mContext).load(group.getUserpic()).into(groupViewHolder.vPic);
        groupViewHolder.vDescription.setText(group.getDescription());
    }

    //TODO: Populate ViewHolder and add listeners.
    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        public TextView vName;
        public ImageView vPic;
        public TextView vDate;
        public TextView vTimestamp;
        public TextView vDescription;

        public GroupViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.groupName);
            vDescription = (TextView) v.findViewById(R.id.groupDetail);
            vPic= (ImageView) v.findViewById(R.id.groupImage);
            vDate=(TextView)v.findViewById(R.id.groupDate);
            vTimestamp= (TextView)v.findViewById(R.id.groupTimestamp);

            if(v != null){
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener!=null){
                            mItemClickListener.onItemClick(vPic, getAdapterPosition());
                        }
                    }
                });
            }
        }
    }

}
