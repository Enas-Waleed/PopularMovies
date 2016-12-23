package com.surya.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.surya.popularmovies.MoviesModel;
import com.surya.popularmovies.Utils.Utility;
import com.surya.popularmovies.data.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.surya.popularmovies.Utils.Utility.makeHttpRequest;

/**
 * Created by Surya on 16-12-2016.
 */

public class ReviewTaskLoader extends AsyncTaskLoader{
    private static final String LOG_TAG = ReviewTaskLoader.class.getSimpleName();

    //    private MoviesModel
    private String id;

    public ReviewTaskLoader(Context context, String id) {
        super(context);
        this.id = id;
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public Object loadInBackground() {

        URL url = null;

        final String API_KEY = "api_key";
        String APPEND_PATH = "append_to_response";
        String TRAILER_REVIEW = "videos,reviews";
        Uri builtUri = Uri.parse(Utility.TMDB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendQueryParameter(API_KEY,BuildConfig.TMDB_API_KEY)
                .appendQueryParameter(APPEND_PATH,TRAILER_REVIEW)
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

        extractFromJson(jsonResponse);

//        Log.e("xxxx",jsonResponse);
        return null;
    }

    private void extractFromJson(String jsonResponse) {

        final String RESULTS = "results";
        final String VIDEOS = "videos";
        final String REVIEWS = "reviews";
        final String KEY = "key";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String LINK = "url";
        final String NAME = "name";

        List<MoviesModel> results = new ArrayList<>();

        if (jsonResponse == null)
            return ;

        try {
            JSONObject response = new JSONObject(jsonResponse);

            //fetch trailers
            JSONObject videosObject = response.getJSONObject(VIDEOS);

            JSONArray trailersArray = videosObject.getJSONArray(RESULTS);

            Vector<ContentValues> cVVector = new Vector<>(trailersArray.length());

            for (int p = 0; p < trailersArray.length(); p++) {

                ContentValues trailerValues = new ContentValues();


                String trailerId = trailersArray.getJSONObject(p).getString(KEY);
                String trailerName = trailersArray.getJSONObject(p).getString(NAME);
                trailerValues.put(MoviesContract.TrailerEntry.COL_MOVIE_ID,id);
                trailerValues.put(MoviesContract.TrailerEntry.COL__TRAILER_LINK,trailerId);
                trailerValues.put(MoviesContract.TrailerEntry.COL_TRAILER_NAME,trailerName);
                cVVector.add(trailerValues);
            }

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = getContext().getContentResolver().bulkInsert(MoviesContract.TrailerEntry.CONTENT_URI, cvArray);

//                Log.e("xxx","bulk insert trailers " + inserted);
            }

            //fetch reviews

            JSONObject reviewsObject = response.getJSONObject(REVIEWS);

            JSONArray reviewsArray = reviewsObject.getJSONArray(RESULTS);

            Vector<ContentValues> reviewVector = new Vector<>(reviewsArray.length());


            for (int q = 0; q < reviewsArray.length(); q++) {

                ContentValues reviewValues = new ContentValues();

                String username = reviewsArray.getJSONObject(q).getString(AUTHOR);
                String content = reviewsArray.getJSONObject(q).getString(CONTENT);
                String url_link = reviewsArray.getJSONObject(q).getString(LINK);

                reviewValues.put(MoviesContract.ReviewEntry.COL_MOVIE_ID,id);
                reviewValues.put(MoviesContract.ReviewEntry.COL_AUTHOR,username);
                reviewValues.put(MoviesContract.ReviewEntry.COL_CONTENT,content);
                reviewValues.put(MoviesContract.ReviewEntry.COL_URL,url_link);
                reviewVector.add(reviewValues);
            }
            // add to database
            if ( reviewVector.size() > 0 ) {
                inserted = 0;
                ContentValues[] cvArray = new ContentValues[reviewVector.size()];
                reviewVector.toArray(cvArray);
                inserted = getContext().getContentResolver().bulkInsert(MoviesContract.ReviewEntry.CONTENT_URI, cvArray);

//                Log.e("xxx","bulk insert reviews " + inserted);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
