package com.example.aboalarbe.movieapp.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aboalarbe.movieapp.R;
import com.example.aboalarbe.movieapp.helpedClasses.MovieDataClass;
import com.example.aboalarbe.movieapp.helpedClasses.NetworkClass;

import java.util.ArrayList;

/**
 * Created by m_abo on 2/16/2017.
 */

public class MovieArrayAdapter extends BaseAdapter {

    static class ViewHolder {
        ImageView imageView;
    }

    Activity activity;
    ArrayList<MovieDataClass> list = new ArrayList<MovieDataClass>();

    public MovieArrayAdapter(Activity activity, ArrayList<MovieDataClass> list) {
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
            view = inflater.inflate(R.layout.moive_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.moive_poster);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // build the posters url to get them
        String poster_uri = list.get(i).getPoster_Path();
        // get the images from the web and load them in background
        // then display them in the grid view.
        if (NetworkClass.isConnected(activity)) {
            NetworkClass.loadMoivePoster(activity, viewHolder.imageView, poster_uri);
        } else {
            viewHolder.imageView.setImageResource(R.drawable.movie_launch);
        }
        return view;
    }
}
