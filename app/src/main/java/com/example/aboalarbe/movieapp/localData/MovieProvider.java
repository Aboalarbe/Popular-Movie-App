package com.example.aboalarbe.movieapp.localData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by m_abo on 2/24/2017.
 */

public class MovieProvider extends ContentProvider {
    private static final UriMatcher URI_MATCHER=buildUriMatcher();
    private MovieDBHelper dbHelper;

    // codes for the uri matcher
    private static final int MOVIE=100;
    private static final int MOVIE_WITH_ID=200;

    private static UriMatcher buildUriMatcher()
    {
        UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        String authority=MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,MovieContract.MoiveEntry.TABLENAME,MOVIE);
        matcher.addURI(authority,MovieContract.MoiveEntry.TABLENAME + "/#",MOVIE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper=new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (URI_MATCHER.match(uri))
        {
            //when All movies selected
            case MOVIE:
            {
                retCursor=dbHelper.getReadableDatabase().query(
                        MovieContract.MoiveEntry.TABLENAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return retCursor;
            }
            default:
            {
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown Uri : "+uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match=URI_MATCHER.match(uri);
        switch (match) {
            case MOVIE:
                return MovieContract.MoiveEntry.CONTENT_DIR_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MoiveEntry.CONT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI : " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db=dbHelper.getWritableDatabase();
        Uri retUri;
        switch (URI_MATCHER.match(uri))
        {
            case MOVIE:
            {
                long id=db.insert(MovieContract.MoiveEntry.TABLENAME,null,contentValues);
                // insert unless it is already contained in the database
                if(id>0)
                {
                    retUri=MovieContract.MoiveEntry.buildMoivrUri(id);
                }
                else
                {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db=dbHelper.getWritableDatabase();
        int numDeleted;
        switch (URI_MATCHER.match(uri))
        {
            case MOVIE:
            {
                numDeleted=db.delete(MovieContract.MoiveEntry.TABLENAME,selection,selectionArgs);
                //reset ID sequence
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MoiveEntry.TABLENAME + "'");
                break;
            }
            case MOVIE_WITH_ID:
            {
                String id=uri.getPathSegments().get(1);
                numDeleted=db.delete(MovieContract.MoiveEntry.TABLENAME,"movie_id=?",new String[]{id});
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MoiveEntry.TABLENAME + "'");
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return numDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
       return 0;
    }
}