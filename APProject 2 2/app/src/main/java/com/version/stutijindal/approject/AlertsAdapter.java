package com.version.stutijindal.approject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sahilshah619 on 06-05-2017.
 */

public class AlertsAdapter extends BaseAdapter {
    ArrayList<String> data;
    Context context;
    private static LayoutInflater inflater = null;

    public AlertsAdapter(MainActivity mainActivity, ArrayList<String> data) {
        context = mainActivity;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tv;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View view;
        view = inflater.inflate(R.layout.alert_text, null);

        holder.tv = (TextView) view.findViewById(R.id.tv);

        holder.tv.setText(data.get(position));

        return view;
    }
}
