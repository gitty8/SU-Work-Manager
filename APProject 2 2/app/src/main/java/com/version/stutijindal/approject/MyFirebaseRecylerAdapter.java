package com.version.stutijindal.approject; //change the package name to your project's package name

/*
Created by Stuti Jindal on 04/10/17

-Schedule Fragment
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

public class MyFirebaseRecylerAdapter extends FirebaseRecyclerAdapter<Shifts, MyFirebaseRecylerAdapter.ShiftViewHolder> {

    private Context mContext;
    static OnItemClickListener mItemClickListener;
    String shiftdescription[]={"Mainline","Speciality","Cooks Help", "Loader", "Unloader","Runner",
            "Coffee","Tea", "Salad Station","Deli"
    };
    int descriptionNo=0;

    public MyFirebaseRecylerAdapter(Class<Shifts> modelClass, int modelLayout,
                                    Class<ShiftViewHolder> holder, DatabaseReference ref, Context context) {

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
    protected void populateViewHolder(ShiftViewHolder shiftViewHolder, Shifts shift, int i) {

        //TODO: Populate viewHolder by setting the shift attributes to cardview fields
        shiftViewHolder.getAdapterPosition();
        Log.d("Data from cloud",""+shift.getUsername());
            shiftViewHolder.vTitle.setText(shift.getUsername());
//        Picasso.with(mContext).load(shift.getUserpic()).into(shiftViewHolder.vIcon);

        shiftViewHolder.vDescription.setText(shiftdescription[descriptionNo++]);
        if(descriptionNo==shiftdescription.length){
            descriptionNo=0;
        }

    }

    //TODO: Populate ViewHolder and add listeners.
    public static class ShiftViewHolder extends RecyclerView.ViewHolder {
        public TextView vTitle;
        public TextView vDescription;

        public ShiftViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.rv_title);
            vDescription = (TextView) v.findViewById(R.id.rv_description);

//            if(v != null){
//                v.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (mItemClickListener!=null){
//                            mItemClickListener.onItemClick(vIcon, getAdapterPosition());
//                        }
//                    }
//                });
//            }
        }
    }

}
