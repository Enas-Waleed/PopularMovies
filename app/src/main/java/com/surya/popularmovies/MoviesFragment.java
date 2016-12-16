package com.surya.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.surya.popularmovies.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements MoviesAdapter.ListItemClickListener ,LoaderManager.LoaderCallbacks<List<MoviesModel>>{

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
             movieList = new ArrayList<>();
             updateMovieList();
             Log.e("xxx","saved instance null");
        }else {
             movieList = savedInstanceState.getParcelableArrayList(Utility.MOVIES_ARRAY);
             Log.e("xxx","saved instance not ::: null");
        }

    }

    private void updateMovieList() {

        getLoaderManager().initLoader(0,null,this).forceLoad();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(Utility.MOVIES_ARRAY,movieList);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

//        Log.e("XXX","onCreateView");

        GridLayoutManager layoutManager;

        if (getResources().getConfiguration().orientation == 1) {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        }else{
            layoutManager = new GridLayoutManager(getActivity(), 4);
        }
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MoviesAdapter(getActivity(), movieList,0,this);

        recyclerView.setAdapter(mAdapter);



        return rootView;
    }

    //onstart

    @Override
    public void onStart() {
        super.onStart();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String sortOrder = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));

        if (!lastSortingOrder.equals(sortOrder)){
            movieList.clear();
            lastSortingOrder = sortOrder;
            updateMovieList();
        }


    }


    @Override
    public void onListItemClick(int position) {

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Utility.MOVIES_OBJECT, movieList.get(position));
        startActivity(intent);

    }

    @Override
    public Loader<List<MoviesModel>> onCreateLoader(int id, Bundle args) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String sortOrder = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));

        return new MovieTaskLoader(getActivity(),sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<List<MoviesModel>> loader, List<MoviesModel> data) {

        if (data != null) {

            List<MoviesModel> movieList = mAdapter.getMoviesModel();
            movieList.clear();
            for (int i = 0; i < data.size(); i++) {
                movieList.add(data.get(i));
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MoviesModel>> loader) {

        movieList.clear();

    }

}
