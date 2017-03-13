package com.example.aboalarbe.movieapp;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aboalarbe.movieapp.Adapters.ReviewArrayAdapter;
import com.example.aboalarbe.movieapp.helpedClasses.NetworkClass;
import com.example.aboalarbe.movieapp.helpedClasses.ReviewClass;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewActivity extends AppCompatActivity {
    int moiveId;
    private final String ID_KEY="id";
    private int scrollIndex;
    ArrayList<ReviewClass>list;
    ReviewArrayAdapter adapter;
    @BindView(R.id.review_list)ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        moiveId=getIntent().getExtras().getInt(ID_KEY);
        if(savedInstanceState==null || !savedInstanceState.containsKey(getString(R.string.review_key))
                || !savedInstanceState.containsKey(getString(R.string.scroll_index_key)))
        {
            list= new ArrayList<ReviewClass>();
            scrollIndex=0;
        }
        else
        {
            list=savedInstanceState.getParcelableArrayList(getString(R.string.review_key));
            scrollIndex=savedInstanceState.getInt(getString(R.string.scroll_index_key));
        }
        ButterKnife.bind(this);
        fetchMovieReview(moiveId);
        Log.d("hgjhg",moiveId+"");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.review_key),list);
        outState.putInt(getString(R.string.scroll_index_key),listView.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }

    /*
     * take the id of the movie which user selected
     *get the movie reviews data from the internet and pass them to adapter to display them
    */
    public void fetchMovieReview(int id)
    {
        RequestQueue queue= Volley.newRequestQueue(this);
        String requestedUrl = NetworkClass.buildReviewURi(id).toString();
        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, requestedUrl, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    list=NetworkClass.parseJsonReviewDataToString(response);
                    adapter=new ReviewArrayAdapter(ReviewActivity.this,list);
                    listView.setAdapter(adapter);
                    listView.setSelection(scrollIndex);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReviewActivity.this,getString(R.string.unexcepected_error), Toast.LENGTH_SHORT).show();
            }
        });
        //add request to the queue to begin
        // execute it
        queue.add(objectRequest);
    }
}
