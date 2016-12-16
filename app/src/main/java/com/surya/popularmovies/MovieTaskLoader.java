package com.surya.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.surya.popularmovies.Utils.Utility;

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

/**
 * Created by Surya on 16-12-2016.
 */

public class MovieTaskLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = MovieTaskLoader.class.getSimpleName();
    private String sortOrder;

    public MovieTaskLoader(Context context,String sortOrder) {
        super(context);

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


    private String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = null;

        HttpURLConnection urlConnection = null;

        InputStream inputStream = null;

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        if (urlConnection.getResponseCode() == 200){

            inputStream = urlConnection.getInputStream();

            jsonResponse = readFromStream(inputStream);
        }

        urlConnection.disconnect();
        if (inputStream != null)
            inputStream.close();

        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        InputStreamReader streamReader = new InputStreamReader(inputStream);

        BufferedReader reader = new BufferedReader(streamReader);

        String line = reader.readLine();

        while (line != null){
            output.append(line).append("\n");
            line = reader.readLine();
        }

//        Log.e(LOG_TAG,output.toString());

        return output.toString();
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

                results.add(new MoviesModel(poster_path,overview,release_date,id,title,
                        backdrop_path,popularity,vote_count,vote_average,language,genre_id));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }
}
