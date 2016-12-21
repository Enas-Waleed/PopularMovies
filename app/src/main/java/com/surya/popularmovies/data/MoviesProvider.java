package com.surya.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Surya on 20-12-2016.
 */

public class MoviesProvider extends ContentProvider {


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    private static final int MOVIES = 100;
    private static final int TRAILERS = 200;
    private static final int REVIEWS = 300;

    static {

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,MoviesContract.PATH_MOVIES,MOVIES);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,MoviesContract.PATH_TRAILERS,TRAILERS);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,MoviesContract.PATH_REVIEWS,REVIEWS);


    }

    private MoviesDBHelper dbHelper;

    @Override
    public boolean onCreate() {

         dbHelper = new MoviesDBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();


        Cursor retCursor = null;

        switch (uriMatcher.match(uri)){


            case MOVIES:
                        retCursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME,
                                        projection,
                                        selection,
                                        selectionArgs,
                                        null,
                                        null,
                                        null);

                Log.e("xxx","querying movies  " + retCursor.getCount());
                        break;

            case TRAILERS:
                        retCursor = db.query(MoviesContract.TrailerEntry.TABLE_NAME
                                        ,projection,
                                        selection,
                                        selectionArgs,
                                        null,
                                        null,
                                        null);


                Log.e("xxx","querying trailers  " + retCursor.getCount());
                        break;

            case REVIEWS:
                        retCursor = db.query(MoviesContract.ReviewEntry.TABLE_NAME
                                        ,projection,
                                        selection,
                                        selectionArgs,
                                        null,
                                        null,
                                        null);


                Log.e("xxx","querying reviews  " + retCursor.getCount());
                        break;
            default:
                Log.e("xxx","Unsupported uri " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MOVIES:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;

            case TRAILERS:
                return MoviesContract.TrailerEntry.CONTENT_TYPE;
            case REVIEWS:
                return MoviesContract.TrailerEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Uri contentUri = null;
        long rowId = -1;


        switch (uriMatcher.match(uri)){


            case MOVIES:
                rowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME,null,values);

                if (rowId > 0){

                    contentUri = MoviesContract.MoviesEntry.buildMovies(rowId);

                }
                break;

            case TRAILERS:

                rowId = db.insert(MoviesContract.TrailerEntry.TABLE_NAME,null,values);

                if (rowId > 0){

                    contentUri = MoviesContract.TrailerEntry.buildTrailers(rowId);

                }

                break;

            case REVIEWS:

                if (rowId > 0){

                    contentUri = MoviesContract.TrailerEntry.buildTrailers(rowId);

                }

                break;
            default:
                rowId = -1;
                Log.e("xxx","Unsupported uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return contentUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (uriMatcher.match(uri) == MOVIES){

            int rowId = db.delete(MoviesContract.MoviesEntry.TABLE_NAME,selection,selectionArgs);

            Log.e("XXX"," deleted in cp " + rowId);

            getContext().getContentResolver().notifyChange(uri,null);
            return rowId;

        }

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        long rowId = -1;


        switch (uriMatcher.match(uri)){


            case MOVIES:

                for (ContentValues movieValue : values) {
                    rowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, movieValue);

                    if (rowId > 0) {

                        count++;

                    }

                }

                break;

            case TRAILERS:

                count=0;
                long x = 0 ;
                for (ContentValues trailerValue : values) {
                    x = db.insert(MoviesContract.TrailerEntry.TABLE_NAME, null, trailerValue);

                    if (x > 0) {

                        count++;

                    }

                }
                break;

            case REVIEWS:

                count=0;

                long y = 0;
                for (ContentValues reviewValue : values) {
                    y = db.insert(MoviesContract.ReviewEntry.TABLE_NAME, null, reviewValue);

                    if (y > 0) {

                        count++;

                    }

                }
                break;
            default:
                rowId = -1;
                Log.e("xxx","Unsupported uri " + uri);
                return super.bulkInsert(uri, values);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;


    }
    @Override
    @TargetApi(11)
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }
}
