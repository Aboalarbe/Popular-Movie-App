package com.example.aboalarbe.movieapp.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aboalarbe.movieapp.R;
import com.example.aboalarbe.movieapp.helpedClasses.ReviewClass;

import java.util.ArrayList;

/**
 * Created by m_abo on 2/22/2017.
 */

public class ReviewArrayAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView reviewAuthor, reviewContent;
    }

    Activity activity;
    ArrayList<ReviewClass> list = new ArrayList<ReviewClass>();

    public ReviewArrayAdapter(Activity activity, ArrayList<ReviewClass> list) {
        this.activity = activity;
        this.list = list;
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
            view = inflater.inflate(R.layout.review_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.reviewAuthor = (TextView) view.findViewById(R.id.author);
            viewHolder.reviewContent = (TextView) view.findViewById(R.id.content);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.reviewAuthor.setText(list.get(i).getReviewAuthor());
        viewHolder.reviewContent.setText(list.get(i).getReviewContent());
        return view;
    }
}
