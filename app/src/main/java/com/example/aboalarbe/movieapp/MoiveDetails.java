package com.example.aboalarbe.movieapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.aboalarbe.movieapp.helpedClasses.MovieDataClass;

public class MoiveDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moive_details);
        MoiveDetalisFragment detalisFragment=new MoiveDetalisFragment();
        if(savedInstanceState==null)
        {
            Bundle bundle=new Bundle();
            Intent intent=getIntent();
            if (intent!=null) {
                 bundle = intent.getExtras();
            }
            if(bundle!=null) {
                detalisFragment.setArguments(bundle);
            }
            getSupportFragmentManager().beginTransaction().add(R.id.details_container,detalisFragment).commit();
        }
    }
}
