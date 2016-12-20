package com.surya.popularmovies.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.surya.popularmovies.data.MoviesContract;
import com.surya.popularmovies.data.MoviesDBHelper;

/**
 * Created by Surya on 17-12-2016.
 */

public class TestUtlis {


//    public static void insertToDb(Context context){
//
//        MoviesDBHelper dbHelper = new MoviesDBHelper(context);
//
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(MoviesContract.CategoryEntry.COL_CATEGORY,"a");
//        contentValues.put(MoviesContract.CategoryEntry.COL_MOVIE_ID,"ab");
//        contentValues.put(MoviesContract.CategoryEntry.COL_POSTER_PATH,"a");
//        contentValues.put(MoviesContract.CategoryEntry.COL_VOTE_AVERAGE,"a");
//        contentValues.put(MoviesContract.CategoryEntry.COL_RELEASE_DATE,"a");
//        contentValues.put(MoviesContract.CategoryEntry.COL_POPULARITY,"a");
//        contentValues.put(MoviesContract.CategoryEntry.COL_TITLE,"a");
//        contentValues.put(MoviesContract.CategoryEntry.COL_BACKDROP,"a");
//        contentValues.put(MoviesContract.CategoryEntry.COL_VOTE_COUNT,"a");
//        contentValues.put(MoviesContract.CategoryEntry.COL_GENRE,"a");
//        contentValues.put(MoviesContract.CategoryEntry.COL_LANGUAGE,"a");
//        contentValues.put(MoviesContract.CategoryEntry.COL_SYNOPSIS,"a");
//
//
//        long rowId = db.insert(MoviesContract.CategoryEntry.TABLE_NAME,null,contentValues);
//
//
//        Toast.makeText(context, rowId + "nn", Toast.LENGTH_SHORT).show();
//
//        Log.e("XXX",rowId + "rowId");
//        db.close();
//
//
//    }


}
