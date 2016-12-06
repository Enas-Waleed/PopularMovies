package com.surya.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    RecyclerView.Adapter mAdapter;

    List<MoviesModel> movieList;

    public MoviesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MoviesAdapter(getActivity(), movieList);

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra(Utility.MOVIES_OBJECT, movieList.get(position));
                        startActivity(intent);
                    }
                })
        );

        return rootView;
    }


    //onstart

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String sortOrder = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));

        updateMoviesList(sortOrder);

    }

    private void updateMoviesList(String sortOrder) {

        FetchMoviesTask moviesTask = new FetchMoviesTask();

        moviesTask.execute(sortOrder);

    }
    public class FetchMoviesTask extends AsyncTask<String,Void,List<MoviesModel>> {

        private static final String LOG_TAG = "FetchMoviesTask";

        @Override
        protected List<MoviesModel> doInBackground(String... params) {

            if (params.length == 0)
                return null;

            URL url = null;

            final String API_KEY = "api_key";
            Uri builtUri = Uri.parse(Utility.TMDB_BASE_URL).buildUpon()
                    .appendPath(params[0])
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

                    results.add(new MoviesModel(poster_path,overview,release_date,id,title,
                            backdrop_path,popularity,vote_count,vote_average,language));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return results;
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

            Log.e(LOG_TAG,output.toString());

            return output.toString();
        }

        @Override
        protected void onPostExecute(List<MoviesModel> result) {

            if (result != null)
                for (int i = 0; i < result.size(); i++) {
                    //

                    movieList.add(result.get(i));

                    mAdapter.notifyDataSetChanged();

                }

        }
    }

}
