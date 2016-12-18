package com.surya.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.surya.popularmovies.Utils.Utility;
import com.surya.popularmovies.data.MoviesContract;
import com.surya.popularmovies.data.MoviesDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.surya.popularmovies.Utils.Utility.makeHttpRequest;

/**
 * Created by Surya on 16-12-2016.
 */

public class MovieTaskLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = MovieTaskLoader.class.getSimpleName();
    private String sortOrder;
    private Context mContext;

    public MovieTaskLoader(Context context,String sortOrder) {
        super(context);

        this.mContext = context;
        this.sortOrder = sortOrder;

    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    @Override
    public List<MoviesModel> loadInBackground() {


        URL url = null;

        final String API_KEY = "api_key";
        Uri builtUri = Uri.parse(Utility.TMDB_BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(API_KEY,BuildConfig.TMDB_API_KEY)
                .build();

        //create a url object
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"Error in url " + e.getMessage());
        }
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractFromJson(jsonResponse);

    }

    private List<MoviesModel> extractFromJson(String jsonResponse) {

        final String RESULTS = "results";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String ID = "id";
        final String TITLE = "title";
        final String BACKDROP_PATH = "backdrop_path";
        final String POPULARITY = "popularity";
        final String VOTE_COUNT = "vote_count";
        final String VOTE_AVERAGE = "vote_average";
        final String LANGUAGE = "original_language";
        final String GENRE = "genre_ids";

        List<MoviesModel> results = new ArrayList<>();
        ContentValues contentValues = new ContentValues();

        SQLiteDatabase db = (new MoviesDBHelper(mContext)).getWritableDatabase();

        if (jsonResponse == null)
            return null;

        try {
            JSONObject response = new JSONObject(jsonResponse);

            JSONArray resultsArray = response.getJSONArray(RESULTS);

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject resultObject = resultsArray.getJSONObject(i);

                String poster_path = resultObject.getString(POSTER_PATH);
                String overview = resultObject.getString(OVERVIEW);
                String release_date = resultObject.getString(RELEASE_DATE);
                String id = resultObject.getString(ID);
                String title = resultObject.getString(TITLE);
                String backdrop_path = resultObject.getString(BACKDROP_PATH);
                String popularity = resultObject.getString(POPULARITY);
                String vote_count = resultObject.getString(VOTE_COUNT);
                String vote_average = resultObject.getString(VOTE_AVERAGE);
                String language = resultObject.getString(LANGUAGE);
                JSONArray genreArray = resultObject.getJSONArray(GENRE);

                int genre_id[] = new int[2 > genreArray.length() ? genreArray.length() : 2];

                for (int j = 0; j < genre_id.length; j++) {
                    genre_id[j] = genreArray.getInt(j);
                }

                String formatGenre = "";
                //format  genre of only one type
                if (genre_id.length > 0) {
                    if (genre_id.length == 1) {

                        formatGenre = Utility.getGenreFromId(genre_id[0]);

                    } else {
                        //format  genre of only two type
                        formatGenre = mContext.getString(R.string.formatGenre,
                                Utility.getGenreFromId(genre_id[0]),
                                Utility.getGenreFromId(genre_id[1]));
                    }
                }

                results.add(new MoviesModel(poster_path,overview,release_date,id,title,
                        backdrop_path,popularity,vote_count,vote_average,language,genre_id));

                contentValues.put(MoviesContract.CategoryEntry.COL_CATEGORY,sortOrder);
                contentValues.put(MoviesContract.CategoryEntry.COL_MOVIE_ID,id);
                contentValues.put(MoviesContract.CategoryEntry.COL_POSTER_PATH,poster_path);
                contentValues.put(MoviesContract.CategoryEntry.COL_VOTE_AVERAGE,vote_average);
                contentValues.put(MoviesContract.CategoryEntry.COL_RELEASE_DATE,release_date);
                contentValues.put(MoviesContract.CategoryEntry.COL_POPULARITY,popularity);
                contentValues.put(MoviesContract.CategoryEntry.COL_TITLE,title);
                contentValues.put(MoviesContract.CategoryEntry.COL_BACKDROP,backdrop_path);
                contentValues.put(MoviesContract.CategoryEntry.COL_VOTE_COUNT,vote_count);
                contentValues.put(MoviesContract.CategoryEntry.COL_GENRE,formatGenre);
                contentValues.put(MoviesContract.CategoryEntry.COL_LANGUAGE,language);
                contentValues.put(MoviesContract.CategoryEntry.COL_SYNOPSIS,overview);



                long rowId = db.insert(MoviesContract.CategoryEntry.TABLE_NAME,null,contentValues);

//                Toast.makeText(mContext, rowId + "inserted", Toast.LENGTH_SHORT).show();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }
}
