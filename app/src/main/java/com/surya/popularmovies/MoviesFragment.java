package com.surya.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
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

    MoviesAdapter mAdapter;

    RecyclerView recyclerView;

    ArrayList<MoviesModel> movieList;

    private static String lastSortingOrder = "dummy";

    public MoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null ||!savedInstanceState.containsKey(Utility.MOVIES_ARRAY)){
            Log.e("XXX","onCreate"  + "null");
            movieList = new ArrayList<>();
        }else {
            Log.e("XXX","onCreate"  + "not null");
              movieList = savedInstanceState.getParcelableArrayList(Utility.MOVIES_ARRAY);
      }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.e("XXX","onSavedInstance");
        outState.putParcelableArrayList(Utility.MOVIES_ARRAY,movieList);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        Log.e("XXX","onCreateView");

        GridLayoutManager layoutManager;

        if (getResources().getConfiguration().orientation == 1) {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        }else{
            layoutManager = new GridLayoutManager(getActivity(), 4);
        }
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MoviesAdapter(getActivity(), movieList,0);

        if (movieList.size() != 0)
        mAdapter.notifyDataSetChanged();

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

        Log.e("XXX","onStart");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String sortOrder = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));

        if (!lastSortingOrder.equals(sortOrder)){
            Log.e("XXX","onStart" + "sortorder not equal");
            movieList.clear();
            updateMoviesList(sortOrder);
            lastSortingOrder = sortOrder;
        }
    }

    private void updateMoviesList(String sortOrder) {

        FetchMoviesTask moviesTask = new FetchMoviesTask(getActivity(),mAdapter);

        moviesTask.execute(sortOrder);

    }


}
