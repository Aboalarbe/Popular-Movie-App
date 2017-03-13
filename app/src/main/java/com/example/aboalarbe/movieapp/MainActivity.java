package com.example.aboalarbe.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.aboalarbe.movieapp.helpedClasses.MovieDataClass;
import com.example.aboalarbe.movieapp.helpedClasses.MovieDataListener;

public class MainActivity extends AppCompatActivity implements MovieDataListener {
    boolean isTwoPane=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MovieFragment movieFragment=new MovieFragment();

        if(savedInstanceState==null)
        {
            movieFragment.setListener(this);
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, movieFragment).commit();
        }
        if(findViewById(R.id.details_container)!=null)
        {
            isTwoPane=true;
        }
    }

    @Override
    public void sendMovieData(MovieDataClass object) {
        //case 1 pane
        if(!isTwoPane) {
            Intent intent=new Intent(this,MoiveDetails.class);
            intent.putExtra(getString(R.string.movie_object_key),object);
            startActivity(intent);
        }
        else
            //case 2 pane
        {
            MoiveDetalisFragment detalisFragment=new MoiveDetalisFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable(getString(R.string.movie_object_key),object);
            detalisFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container,detalisFragment).commit();
        }
    }
}