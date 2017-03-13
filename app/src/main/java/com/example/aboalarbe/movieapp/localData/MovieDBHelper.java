package com.example.aboalarbe.movieapp.localData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by m_abo on 2/24/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="moive.db";
    private static final int DATABASE_VERSION=5;
    public MovieDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       final String CREATE_TABLE_MOIVE=
                "CREATE TABLE " + MovieContract.MoiveEntry.TABLENAME + " ("+MovieContract.MoiveEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieContract.MoiveEntry.MOIVE_ID + " INTEGER UNIQUE NOT NULL, "+
                        MovieContract.MoiveEntry.TITLE + " TEXT NOT NULL, "+
                        MovieContract.MoiveEntry.OVERVIEW + " TEXT NOT NULL, "+
                        MovieContract.MoiveEntry.PSTER_PATH + " TEXT NOT NULL, "+
                        MovieContract.MoiveEntry.RELASED_DATE + " TEXT NOT NULL, "+
                        MovieContract.MoiveEntry.VOTE + " REAL NOT NULL "+");";
        sqLiteDatabase.execSQL(CREATE_TABLE_MOIVE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoiveEntry.TABLENAME);
        onCreate(sqLiteDatabase);
    }
}
