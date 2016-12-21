package com.surya.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surya.popularmovies.Utils.Utility;
import com.surya.popularmovies.data.MoviesContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements MoviesAdapter.ListItemClickListener ,LoaderManager.LoaderCallbacks<Cursor>{

    MoviesAdapter mAdapter;

    RecyclerView recyclerView;

    private OnMovieSelectedListener mCallback;

    private static String lastSortingOrder = "dummy";


    public interface OnMovieSelectedListener{

        public void onMovieSelected(String movie_id);

    }


    public MoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null ||!savedInstanceState.containsKey(Utility.MOVIES_ARRAY)){
             updateMovieList();
             Log.e("xxx","saved instance null");
        }else {
             Log.e("xxx","saved instance not ::: null");
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMovieSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMovieSelectedListener");
        }
    }

    private void updateMovieList() {

        getLoaderManager().initLoader(0,null,this).forceLoad();

        getLoaderManager().initLoader(1,null,this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {



//        outState.putInt(Utility.MOVIE_POSITION,recyclerView.getVerticalScrollbarPosition());
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

        mAdapter = new MoviesAdapter(getActivity(),0,this,null);

        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    //onstart

    @Override
    public void onStart() {
        super.onStart();

        String sortOrder = Utility.getSortOrder(getActivity());

        if (!lastSortingOrder.equals(sortOrder)){
          lastSortingOrder = sortOrder;
            updateMovieList();
        }


    }


    @Override
    public void onListItemClick(int position) {

        Cursor cursor = mAdapter.getCursor();

        cursor.moveToPosition(position);

//        ((OnMovieSelectedListener)getActivity()).onMovieSelected(cursor.getString(1));

        Intent intent = new Intent(getActivity(), DetailActivity.class);

        intent.putExtra(Utility.MOVIE_ID,cursor.getString(1));

        startActivity(intent);



    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(1,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == 0)
            return new MovieTaskLoader(getActivity(),Utility.getSortOrder(getActivity()));
        else {

            String selection = MoviesContract.MoviesEntry.COL_SORT + " = ?" ;

            return new CursorLoader(getActivity(),
                                    MoviesContract.MoviesEntry.CONTENT_URI,
                                    null,
                                    selection,
                                    new String[]{Utility.getSortOrder(getActivity())},
                                    null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (loader.getId() == 1) {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapCursor(null);

    }

}
