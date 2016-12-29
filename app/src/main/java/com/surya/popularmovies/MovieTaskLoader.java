package com.surya.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.surya.popularmovies.Utils.Utility;
import com.surya.popularmovies.data.MoviesContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
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
    public Cursor loadInBackground() {

        Log.e(LOG_TAG,"load in background");

        URL url = null;

        final String API_KEY = "api_key";
        Uri builtUri = Uri.parse(Utility.TMDB_BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(API_KEY,BuildConfig.TMDB_API_KEY)
                .build();

        if (sortOrder.equals(mContext.getString(R.string.pref_sort_favourite))){
            return null;
        }else {
            //create a url object
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error in url " + e.getMessage());
            }
            String jsonResponse = null;

            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return extractFromJson(jsonResponse);

        }

    }

    private Cursor extractFromJson(String jsonResponse) {

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

        if (jsonResponse == null)
            return null;

        try {
            JSONObject response = new JSONObject(jsonResponse);

            JSONArray resultsArray = response.getJSONArray(RESULTS);

            Vector<ContentValues> cvVector = new Vector<>(resultsArray.length());

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

                ContentValues contentValues = new ContentValues();
                contentValues.put(MoviesContract.MoviesEntry.COL_MOVIE_ID,id);
                contentValues.put(MoviesContract.MoviesEntry.COL_POSTER_PATH,poster_path);
                contentValues.put(MoviesContract.MoviesEntry.COL_VOTE_AVERAGE,vote_average);
                contentValues.put(MoviesContract.MoviesEntry.COL_RELEASE_DATE,release_date);
                contentValues.put(MoviesContract.MoviesEntry.COL_POPULARITY,popularity);
                contentValues.put(MoviesContract.MoviesEntry.COL_TITLE,title);
                contentValues.put(MoviesContract.MoviesEntry.COL_BACKDROP,backdrop_path);
                contentValues.put(MoviesContract.MoviesEntry.COL_VOTE_COUNT,mContext.getString(R.string.formatVotes,vote_count));
                contentValues.put(MoviesContract.MoviesEntry.COL_GENRE,formatGenre);
                contentValues.put(MoviesContract.MoviesEntry.COL_LANGUAGE,language);
                contentValues.put(MoviesContract.MoviesEntry.COL_SYNOPSIS,overview);
                contentValues.put(MoviesContract.MoviesEntry.COL_SORT,sortOrder);

                cvVector.add(contentValues);

            }

            int inserted = 0;
            // add to database
            if ( cvVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cvVector.size()];
                cvVector.toArray(cvArray);
                inserted = getContext().getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, cvArray);

                Log.e(LOG_TAG,"inserted using cp" + inserted);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
