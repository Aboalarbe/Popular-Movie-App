package com.example.aboalarbe.movieapp.localData;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by m_abo on 2/24/2017.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY="com.example.aboalarbe.movieapp";
    public static Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final class MoiveEntry implements BaseColumns
    {
        // table name
        public static final String TABLENAME="movie";
        // table columns
        public static final String MOIVE_ID="movie_id";
        public static final String PSTER_PATH="poster";
        public static final String TITLE="title";
        public static final String OVERVIEW="overview";
        public static final String RELASED_DATE="date";
        public static final String VOTE="vote";
        public static final String FLAG="flag";
        //create content Uri
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(TABLENAME).build();
        //create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+TABLENAME;
        //create cursor of base type item for single entries
        public static final String CONT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+TABLENAME;
        //for building uris on insertion
        public static Uri buildMoivrUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }
}
