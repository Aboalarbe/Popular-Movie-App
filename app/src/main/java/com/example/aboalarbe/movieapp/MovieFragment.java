package com.example.aboalarbe.movieapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aboalarbe.movieapp.Adapters.MovieArrayAdapter;
import com.example.aboalarbe.movieapp.helpedClasses.MovieDataClass;
import com.example.aboalarbe.movieapp.helpedClasses.MovieDataListener;
import com.example.aboalarbe.movieapp.helpedClasses.NetworkClass;
import com.example.aboalarbe.movieapp.localData.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by m_abo on 2/18/2017.
 */

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int LOADER_ID = 10;
    private static int scrollIndex;
    @BindView(R.id.sort_spinner)
    Spinner sortSpinner;
    @BindView(R.id.movie_grid_view)
    GridView gridView;
    String requestedUrl, selectedItem;
    MovieArrayAdapter adapter;
    static ArrayList<MovieDataClass> list;
    Cursor mData;
    private MovieDataListener listener;

    public void setListener(MovieDataListener listener) {
        this.listener = listener;
    }

    public MovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.movie_key))
                && !savedInstanceState.containsKey(getString(R.string.scroll_index_key))) {
            list = new ArrayList<MovieDataClass>();
            scrollIndex = 0;
        } else {
            list = savedInstanceState.getParcelableArrayList(getString(R.string.movie_key));
            scrollIndex = savedInstanceState.getInt((getString(R.string.scroll_index_key)));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.movie_main_fragment, container, false);
        ButterKnife.bind(this, newView);
        getSelectedSpinnerItemAndFetch();
        onSelectedIGridviewItem();
        return newView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.movie_key), list);
        scrollIndex = gridView.getFirstVisiblePosition();
        outState.putInt(getString(R.string.scroll_index_key), scrollIndex);
        super.onSaveInstanceState(outState);
    }

        /*
        * this method pass the url that concerns to users pereferences
        *  the user selected from spinner and pass the url to fetch data
        *
        */

    private void getSelectedSpinnerItemAndFetch() {
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = adapterView.getSelectedItem().toString();
                if (selectedItem.equals(getString(R.string.most_popular))) {
                    if (NetworkClass.isConnected(getActivity())) {
                        // Delete all values in the arraylist to avoid duplication
                        list.clear();
                        requestedUrl = NetworkClass.buildMovieUri("popular").toString();
                        fetchMovieData(requestedUrl);
                    } else {
                        showConnectionError(getView());
                    }

                } else if (selectedItem.equals(getString(R.string.top_rated))) {
                    if (NetworkClass.isConnected(getActivity())) {
                        // Delete all values in the arraylist to avoid duplication
                        list.clear();
                        requestedUrl = NetworkClass.buildMovieUri("top_rated").toString();
                        fetchMovieData(requestedUrl);
                    } else {
                        showConnectionError(getView());
                    }
                } else {
                    // get the data from the database
                    //and display it in the gridview
                    list = getMovieDataFromDatabase(mData);
                    adapter = new MovieArrayAdapter(getActivity(), list);
                    gridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    /*
           * take the url which concerns to movie data (most popular / top rated)
           *get the movie data from the internet and pass them to adapter to display them
       */
    public void fetchMovieData(String url) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    list = NetworkClass.parseJsonMovieDataToString(response);
                    adapter = new MovieArrayAdapter(getActivity(), list);
                    gridView.setAdapter(adapter);
                    gridView.setSelection(scrollIndex);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), getString(R.string.unexcepected_error), Toast.LENGTH_SHORT).show();
            }
        });
        //add request to the queue to begin
        // execute it
        queue.add(objectRequest);
    }

    /*
        *when the user click on the movie poster
        * send all movie data to the details Activity
     */
    private void onSelectedIGridviewItem() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieDataClass object = list.get(i);
                listener.sendMovieData(object);
            }
        });
    }

    /*
           *take the cursor which has movie data
           * that selected from the table
           * and return arraylist has all objects
         */
    public ArrayList<MovieDataClass> getMovieDataFromDatabase(Cursor cursor) {
        ArrayList<MovieDataClass> list = new ArrayList<MovieDataClass>();
        if (cursor.moveToFirst()) {
            do {
                String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.MoiveEntry.PSTER_PATH));
                String overview = cursor.getString(cursor.getColumnIndex(MovieContract.MoiveEntry.OVERVIEW));
                String releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MoiveEntry.RELASED_DATE));
                String originalTitle = cursor.getString(cursor.getColumnIndex(MovieContract.MoiveEntry.TITLE));
                double voteAverage = cursor.getDouble(cursor.getColumnIndex(MovieContract.MoiveEntry.VOTE));
                int moiveId = cursor.getInt(cursor.getColumnIndex(MovieContract.MoiveEntry.MOIVE_ID));
                MovieDataClass object = new MovieDataClass(voteAverage, originalTitle, releaseDate, overview, posterPath, moiveId);
                list.add(object);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /*
        *when there is no connection this
        * message dispalyed to the user
     */
    public void showConnectionError(View view) {
        Snackbar.make(view, getString(R.string.check_connection), Snackbar.LENGTH_LONG).setAction("Check WiFi", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        }).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getContext()) {
            Cursor dataCursor = null;

            @Override
            protected void onStartLoading() {
                if (dataCursor != null) {
                    deliverResult(dataCursor);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getActivity().getContentResolver().query(
                            MovieContract.MoiveEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                dataCursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            mData = data;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}