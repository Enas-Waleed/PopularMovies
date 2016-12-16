package com.surya.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.surya.popularmovies.MoviesModel;
import com.surya.popularmovies.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

        Log.e("xxxx","load in back");

        URL url = null;

        final String API_KEY = "api_key";
        String APPEND_PATH = "append_to_response";
        String TRAILER_REVIEW = "videos,reviews";
        Uri builtUri = Uri.parse(Utility.TMDB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendQueryParameter(API_KEY,BuildConfig.TMDB_API_KEY)
                .appendQueryParameter(APPEND_PATH,TRAILER_REVIEW)
                .build();

        Log.e("xxx",builtUri.toString());

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

        Log.e("xxxx",jsonResponse);
        return null;
    }

    private void extractFromJson(String jsonResponse) {

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
        final String VIDEOS = "videos";
        final String REVIEWS = "reviews";
        final String KEY = "key";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String LINK = "url";

        List<MoviesModel> results = new ArrayList<>();

        if (jsonResponse == null)
            return ;

        try {
            JSONObject response = new JSONObject(jsonResponse);

            //fetch trailers
            JSONObject videosObject = response.getJSONObject(VIDEOS);

            JSONArray trailersArray = videosObject.getJSONArray(RESULTS);

            for (int p = 0; p < trailersArray.length(); p++) {

                String trailerId = trailersArray.getJSONObject(p).getString(KEY);
                Log.e("xxxxxxx",trailerId);

            }

            //fetch reviews
            JSONObject reviewsObject = response.getJSONObject(REVIEWS);

            JSONArray reviewsArray = reviewsObject.getJSONArray(RESULTS);

            for (int q = 0; q < reviewsArray.length(); q++) {

                String username = reviewsArray.getJSONObject(q).getString(AUTHOR);
                String content = reviewsArray.getJSONObject(q).getString(CONTENT);
                String url_link = reviewsArray.getJSONObject(q).getString(LINK);
                Log.e("xxxxxxx",username);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
