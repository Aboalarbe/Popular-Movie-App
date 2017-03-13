package com.example.aboalarbe.movieapp.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aboalarbe.movieapp.R;

import java.util.ArrayList;

/**
 * Created by m_abo on 2/22/2017.
 */

public class TrailerArrayAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView textView;
    }

    ArrayList<String> list = new ArrayList<String>();
    Activity activity;

    public TrailerArrayAdapter(ArrayList<String> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.trailer_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.txt);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText("Trailer " + (i + 1));
        return view;
    }
}
