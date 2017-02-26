package com.surya.popularmovies.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.surya.popularmovies.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Surya on 04-12-2016.
 */

public class Utility {

    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie";

    public static final String TMDB_POSTER_URL = "https://image.tmdb.org/t/p/w342/";
    public static final String TMDB_BACKDROP_POSTER_URL = "https://image.tmdb.org/t/p/w780/";
    public static final String MOVIE_ID = "movie_id";
    public static final String YOUTUBE_IMG_URL = "http://img.youtube.com/vi/";
    public static final String END_IMG_URL = "/0.jpg";
    public static final String YOUTUBE_URL = "http://youtube.com/watch?v=";
    public static final String MOVIE_POSITION = "position";
    public static final String TMDB_WEB_URL = "https://www.themoviedb.org/movie/";

    public static int formatPopularity(String popularity) {
        return (int) Math.round(Double.valueOf(popularity));
    }

    public static String getGenreFromId(int id){

        if (id == 28)
            return "Action";

        if (id == 12)
            return "Adventure";

        if (id == 16)
            return "Animation";

        if (id == 35)
            return "Comedy";

        if (id == 80)
            return "Crime";

        if (id == 99)
            return "Documentary";

        if (id == 18)
            return "Drama";

        if (id == 10751)
            return "Family";

        if (id == 14)
            return "Fantasy";

        if (id == 36)
            return "History";

        if (id == 27)
            return "Horror";

        if (id == 10402)
            return "Music";

        if (id == 9648)
            return "Mystery";

        if (id == 10749)
            return "Romance";

        if (id == 878)
            return "Science Fiction";

        if (id == 10770)
            return "TV Movie";

        if (id == 53)
            return "Thriller";


        if (id == 10752)
            return "War";


        if (id == 37)
            return "Western";


        return null;
    }

    //helper methods for making http connection
    public static String makeHttpRequest(URL url) throws IOException {

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

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        InputStreamReader streamReader = new InputStreamReader(inputStream);

        BufferedReader reader = new BufferedReader(streamReader);

        String line = reader.readLine();

        while (line != null){
            output.append(line).append("\n");
            line = reader.readLine();
        }

        return output.toString();
    }

    public static String getSortOrder(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_popular));

    }

    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }



}
