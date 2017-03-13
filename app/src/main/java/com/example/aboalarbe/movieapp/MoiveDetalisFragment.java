package com.example.aboalarbe.movieapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aboalarbe.movieapp.Adapters.TrailerArrayAdapter;
import com.example.aboalarbe.movieapp.helpedClasses.MovieDataClass;
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

public class MoiveDetalisFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int STATE_FAVOURITE = 1;
    private final int STATE_UNFAVOURITE = 0;
    private int currentState = 0;
    private final int LOADER_ID = 19;
    String moiveTitle, moivePoster, moiveOvreview, moiveDate;
    double moiveVote;
    int moiveId;
    MovieDataClass moiveObject;
    ArrayList<String> list = new ArrayList<String>();
    TrailerArrayAdapter adapter;
    @BindView(R.id.favourite_icon)
    ImageView favourite;
    @BindView(R.id.trailer_list)
    ListView listView;
    @BindView(R.id.movie_title)
    TextView moive_title;
    @BindView(R.id.relased_date)
    TextView moive_date;
    @BindView(R.id.vote_average)
    TextView moive_rate;
    @BindView(R.id.movie_overview)
    TextView moive_overview;
    @BindView(R.id.details_moive_poster)
    ImageView moive_poster;
    @BindView(R.id.trailers_tx)
    TextView txView;

    public MoiveDetalisFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        moiveObject = bundle.getParcelable(getString(R.string.movie_object_key));
        getMoiveData(moiveObject);
        if (NetworkClass.isConnected(getActivity())) {
            fetchMovieVideoKey(moiveId);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.reviews_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.review_item: {
                final String MOVIE_ID_KEY = "id";
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra(MOVIE_ID_KEY, moiveId);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.movie_details_fragment, container, false);
        ButterKnife.bind(this, newView);
        moive_title.setText(moiveTitle);
        moive_date.setText(moiveDate);
        moive_rate.setText(String.valueOf(moiveVote));
        moive_overview.setText(moiveOvreview);

        if (NetworkClass.isConnected(getActivity())) {
            NetworkClass.loadMoivePoster(getActivity(), moive_poster, moivePoster);
        } else {
            moive_poster.setImageResource(R.drawable.movie_launch);
        }

        handleTrailers();
        checkConnectionForFavouriteView();

        return newView;
    }

    /*
    *this method get the data from the intent
    * which sent from the main activity
     */
    private void getMoiveData(MovieDataClass movieObject) {
        moiveTitle = movieObject.getOriginal_title();
        moivePoster = movieObject.getPoster_Path();
        moiveOvreview = movieObject.getOverview();
        moiveDate = movieObject.getRelease_date();
        moiveVote = movieObject.getVote_average();
        moiveId = movieObject.getMoiveId();
    }

    /*
          * take the id of the movie which user selected
          *get the key of the trailer videos of the selected movie
       */
    public void fetchMovieVideoKey(int id) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String requestedUrl = NetworkClass.buildTrailerURi(id).toString();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, requestedUrl, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    list = NetworkClass.parseJsonTrailerUrlToString(response);
                    adapter = new TrailerArrayAdapter(list, getActivity());
                    listView.setAdapter(adapter);
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
    *in this method i build the uri that access
    * the movies trailer in the youtube through implicit intent
     */
    public void handleTrailers() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String videoKey = list.get(i);
                startYoutube(videoKey);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String videoKey = list.get(i);
                shareTrailerVideoUrl(videoKey);
                return true;
            }
        });
    }

    /*
       *this method take the key of the video
       * and opent the youtube to view it
     */
    private void startYoutube(String videoKey) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(NetworkClass.buildTrailerVideoUrl(videoKey));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /*
       *this method take the key of the video
       * and when you long click in the item
       * you can choose the app which want to share the video
     */
    private void shareTrailerVideoUrl(String videoKey) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, moiveTitle);
        String sAux = "\n" + getString(R.string.recommendation_text) + "\n";
        sAux = sAux + NetworkClass.buildTrailerVideoUrl(videoKey) + "\n";
        intent.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(intent, getString(R.string.choose_app)));
    }

    /*
      *when the connection is gone i hide
      * the trailers listView and menu item
      * which display the review
   */
    private void checkConnectionForFavouriteView() {
        if (!NetworkClass.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.offline_mode), Toast.LENGTH_SHORT).show();
            setHasOptionsMenu(false);
            listView.setVisibility(ListView.GONE);
            txView.setVisibility(TextView.GONE);
        }
    }

    /*
    * in this method i will handle the user`s
    * favourite moives which he selected to see
    * them when he was in offline mode
     */
    private void handleFavouriteMoives() {
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonToggle();
            }
        });
    }

    /*
        *this method that insert favoutite movies
        * into the database when user marked as Favourite
     */
    private void insertMovieDataIntoDatabase() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MoiveEntry.MOIVE_ID, moiveId);
        values.put(MovieContract.MoiveEntry.TITLE, moiveTitle);
        values.put(MovieContract.MoiveEntry.OVERVIEW, moiveOvreview);
        values.put(MovieContract.MoiveEntry.PSTER_PATH, moivePoster);
        values.put(MovieContract.MoiveEntry.VOTE, moiveVote);
        values.put(MovieContract.MoiveEntry.RELASED_DATE, moiveDate);
        Uri uri = getContext().getContentResolver().insert(MovieContract.MoiveEntry.CONTENT_URI, values);
        if (uri != null) {
            favourite.setImageResource(R.drawable.fav);
            Toast.makeText(getContext(), getString(R.string.marked_Favourite), Toast.LENGTH_SHORT).show();
        }

    }

    /*
        *this method which delete the favourite movies
        * from the database when user markes as Unfavourite
     */
    private void deleteMovieDataFromDatabase() {
        String MovieIdStr = String.valueOf(moiveId);
        Uri uri = MovieContract.MoiveEntry.CONTENT_URI.buildUpon().appendPath(MovieIdStr).build();
        getActivity().getContentResolver().delete(uri, null, null);
        favourite.setImageResource(R.drawable.notfav);
        Toast.makeText(getContext(), getString(R.string.marked_unFavourite), Toast.LENGTH_SHORT).show();
    }

    private void buttonToggle() {
        switch (currentState) {
            case STATE_FAVOURITE: {
                deleteMovieDataFromDatabase();
                currentState = STATE_UNFAVOURITE;
                break;
            }
            case STATE_UNFAVOURITE: {
                insertMovieDataIntoDatabase();
                currentState = STATE_FAVOURITE;
                break;
            }
        }
    }

    private int getFavouriteMovieIDFromDatabase(Cursor cursor) {
        int moiveId = -1;
        if (cursor.moveToFirst()) {
            do {
                moiveId = cursor.getInt(cursor.getColumnIndex(MovieContract.MoiveEntry.MOIVE_ID));
            } while (cursor.moveToNext());
        }
        return moiveId;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getContext()) {
            Cursor cursorData;

            @Override
            protected void onStartLoading() {
                if (cursorData != null) {
                    deliverResult(cursorData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getActivity().getContentResolver().query(
                            MovieContract.MoiveEntry.CONTENT_URI,
                            new String[]{MovieContract.MoiveEntry.MOIVE_ID},
                            MovieContract.MoiveEntry.MOIVE_ID + "=?",
                            new String[]{String.valueOf(moiveId)},
                            null
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                cursorData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            int movieIdInDatabase = getFavouriteMovieIDFromDatabase(data);
            if (movieIdInDatabase == moiveId) {
                favourite.setImageResource(R.drawable.fav);
                currentState = STATE_FAVOURITE;
            } else {
                favourite.setImageResource(R.drawable.notfav);
                currentState = STATE_UNFAVOURITE;
            }
            handleFavouriteMoives();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}