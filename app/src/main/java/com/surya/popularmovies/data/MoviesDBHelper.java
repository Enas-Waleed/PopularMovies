package com.surya.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Surya on 17-12-2016.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tmdbmovies.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CATEGORY_CREATE_TABLE = "CREATE TABLE "
                                    + MoviesContract.FavouriteEntry.TABLE_NAME + " ("
                                    + MoviesContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                    + MoviesContract.FavouriteEntry.COL_MOVIE_ID + " TEXT NOT NULL,"
                                    + MoviesContract.FavouriteEntry.COL_POSTER_PATH + " TEXT NOT NULL,"
                                    + MoviesContract.FavouriteEntry.COL_VOTE_AVERAGE + " TEXT NOT NULL,"
                                    + MoviesContract.FavouriteEntry.COL_RELEASE_DATE + " TEXT NOT NULL,"
                                    + MoviesContract.FavouriteEntry.COL_POPULARITY + " TEXT NOT NULL,"
                                    + MoviesContract.FavouriteEntry.COL_TITLE + " TEXT NOT NULL,"
                                    + MoviesContract.FavouriteEntry.COL_BACKDROP + " TEXT NOT NULL,"
                                    + MoviesContract.FavouriteEntry.COL_VOTE_COUNT + " TEXT NOT NULL,"
                                    + MoviesContract.FavouriteEntry.COL_GENRE + " TEXT NOT NULL,"
                                    + MoviesContract.FavouriteEntry.COL_LANGUAGE + " TEXT NOT NULL,"
                                    + MoviesContract.FavouriteEntry.COL_SYNOPSIS + " TEXT NOT NULL"
                                    + " );";

        db.execSQL(CATEGORY_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavouriteEntry.TABLE_NAME);

        onCreate(db);

    }
}
