package com.surya.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Surya on 19-12-2016.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tmdbmovies.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_FAVOURITES_TABLE = "CREATE TABLE "
                + MoviesContract.MoviesEntry.TABLE_NAME + " ("
                + MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MoviesContract.MoviesEntry.COL_MOVIE_ID + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_POSTER_PATH + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_VOTE_AVERAGE + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_RELEASE_DATE + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_POPULARITY + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_TITLE + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_BACKDROP + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_VOTE_COUNT + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_GENRE + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_LANGUAGE + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_SYNOPSIS + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_SORT + " TEXT NOT NULL,"
                +  "UNIQUE (" + MoviesContract.MoviesEntry.COL_MOVIE_ID + ","
                + MoviesContract.MoviesEntry.COL_SORT + ") ON CONFLICT REPLACE);";

        final String CREATE_TRAILER_TABLE = "CREATE TABLE "
                + MoviesContract.TrailerEntry.TABLE_NAME + " ("
                + MoviesContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MoviesContract.TrailerEntry.COL_MOVIE_ID + " TEXT NOT NULL,"
                + MoviesContract.TrailerEntry.COL_TRAILER_NAME + " TEXT NOT NULL,"
                + MoviesContract.TrailerEntry.COL__TRAILER_LINK + " TEXT NOT NULL,"
                + "UNIQUE ("
                + MoviesContract.TrailerEntry.COL__TRAILER_LINK + ") ON CONFLICT REPLACE);";
        final String CREATE_REVIEW_TABLE = "CREATE TABLE "
                + MoviesContract.ReviewEntry.TABLE_NAME + " ("
                + MoviesContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MoviesContract.ReviewEntry.COL_MOVIE_ID + " TEXT NOT NULL,"
                + MoviesContract.ReviewEntry.COL_AUTHOR + " TEXT NOT NULL,"
                + MoviesContract.ReviewEntry.COL_CONTENT + " TEXT NOT NULL,"
                + MoviesContract.ReviewEntry.COL_URL + " TEXT NOT NULL,"
                + "UNIQUE (" + MoviesContract.ReviewEntry.COL_AUTHOR + ","
                + MoviesContract.ReviewEntry.COL_CONTENT + ") ON CONFLICT REPLACE);";




        db.execSQL(CREATE_FAVOURITES_TABLE);
        db.execSQL(CREATE_TRAILER_TABLE);
        db.execSQL(CREATE_REVIEW_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.ReviewEntry.TABLE_NAME);
        onCreate(db);

    }
}
