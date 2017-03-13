package com.example.aboalarbe.movieapp.helpedClasses;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by m_abo on 2/16/2017.
 */
public class NetworkClass {
    static final String API_KEY = "api_key";
    static final String MY_API_ID = "PUT YOUR API_KEY HERE";

    /*
       *this method build the Uri that
       * i will use to get Movie data from
       * the internet (Most popular / Top_rated)
    */
    public static Uri buildMovieUri(String sortOrder) {
        final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(sortOrder)
                .appendQueryParameter(API_KEY, MY_API_ID)
                .build();
        return builtUri;
    }

    /*
       *this method build the Uri that
       * i will use to get Movie reviews from the internet
    */
    public static Uri buildReviewURi(int id) {
        final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(String.valueOf(id)).appendPath("reviews")
                .appendQueryParameter(API_KEY, MY_API_ID)
                .build();
        return builtUri;
    }

    /*
       *this method build the Uri that
       * i will use to get Movie Trailes Videos from the internet
    */
    public static Uri buildTrailerURi(int id) {
        final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(String.valueOf(id)).appendPath("videos")
                .appendQueryParameter(API_KEY, MY_API_ID)
                .build();
        return builtUri;
    }

    public static final Uri buildTrailerVideoUrl(String videoKey) {
        final String V = "v";
        final String MOVIE_BASE_URL = "https://www.youtube.com/watch?";
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendQueryParameter(V, videoKey).build();
        return builtUri;
    }

    /*
      *this method check the internet connection
      * return true if connected
      * return false if not connected
    */

    public static boolean isConnected(Activity activity) {
        ConnectivityManager conn = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting())
            return true;
        else
            return false;
    }

    /*
       * this method build the uri
       * to Load the poster Image
       * using Picassio Library
       * and dispaly it in Imageview
     */

    public static void loadMoivePoster(Activity activity, ImageView imageView, String posterPath) {
        final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
        final String POSTER_SIZE = "w185/";
        Uri builtUri = Uri.parse(POSTER_BASE_URL).buildUpon().appendEncodedPath(POSTER_SIZE)
                .appendEncodedPath(posterPath).build();
        Picasso.with(activity)
                .load(builtUri)
                .into(imageView);
    }

    /*
       *this method which convert the json
       * data which passed in this json object
       *to string data which i use it in the app
       *and pass them to the parcleable class
       * return arraylist that have all objects
    */
    public static ArrayList<MovieDataClass> parseJsonMovieDataToString(JSONObject jsonObject) throws JSONException {
        ArrayList<MovieDataClass> list = new ArrayList<>();
        JSONArray results = jsonObject.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieObject = results.getJSONObject(i);
            String posterPath = movieObject.getString("poster_path");
            String overview = movieObject.getString("overview");
            String releaseDate = movieObject.getString("release_date");
            String originalTitle = movieObject.getString("original_title");
            double voteAverage = movieObject.getDouble("vote_average");
            int moiveId = movieObject.getInt("id");
            MovieDataClass object = new MovieDataClass(voteAverage, originalTitle, releaseDate, overview, posterPath, moiveId);
            list.add(object);
        }
        return list;
    }

    /*
       *this method which convert the json
       * review data which passed in this json object to strings
       *and pass them to the parcleable class
       * return arraylist that have all objects
    */
    public static ArrayList<ReviewClass> parseJsonReviewDataToString(JSONObject jsonObject) throws JSONException {
        ArrayList<ReviewClass> list = new ArrayList<>();
        JSONArray results = jsonObject.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieReviewObject = results.getJSONObject(i);
            String author = movieReviewObject.getString("author");
            String content = movieReviewObject.getString("content");
            ReviewClass object = new ReviewClass(author, content);
            list.add(object);
        }
        return list;
    }

    /*
       *this method which get the video keys
       * from json object which passed to the method
       * add them to arraylist and return the array
    */
    public static ArrayList<String> parseJsonTrailerUrlToString(JSONObject jsonObject) throws JSONException {
        ArrayList<String> list = new ArrayList<String>();
        JSONArray results = jsonObject.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieVideoObject = results.getJSONObject(i);
            String videoKey = movieVideoObject.getString("key");
            list.add(videoKey);
        }
        return list;
    }
}
