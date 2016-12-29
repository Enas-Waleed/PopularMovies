package com.surya.popularmovies;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

    private static final int CURSOR_ID = 1;
    private static final int MOVIE_TASK_LOADER = 0;
    private final String LOG_TAG = "MainFragment";

    MoviesAdapter mAdapter;

    RecyclerView recyclerView;

    private int mPosition;

    private static String lastSortingOrder = "dummy";

    public interface mMovieClickListener{

        public void OnItemClick(String movie_id);

    }

    public MoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.e(LOG_TAG,"OnCreate");

//        if (savedInstanceState == null || !savedInstanceState.containsKey(Utility.MOVIE_POSITION)){
//            updateMovieList();
//        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        Log.e(LOG_TAG,"On Activity Created");

        getLoaderManager().initLoader(CURSOR_ID,null,this);

        getLoaderManager().initLoader(MOVIE_TASK_LOADER,null,this).forceLoad();

        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(Utility.MOVIE_POSITION,mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        if (savedInstanceState != null){

            mPosition = savedInstanceState.getInt(Utility.MOVIE_POSITION);

        }

        mAdapter = new MoviesAdapter(getActivity(),0,this,null);

        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    //onstart

    @Override
    public void onStart() {
        super.onStart();

        Log.e(LOG_TAG,"OnStart");

        String sortOrder = Utility.getSortOrder(getActivity());

        if (!lastSortingOrder.equals(sortOrder)){
            onSortChange();
            lastSortingOrder = sortOrder;
            mPosition = 0;
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == MOVIE_TASK_LOADER)
            return new MovieTaskLoader(getActivity(),Utility.getSortOrder(getActivity()));
        else if (id == CURSOR_ID){

            Log.e(LOG_TAG,"cursor created");

            String selection = MoviesContract.MoviesEntry.COL_SORT + " = ?" ;

            return new CursorLoader(getActivity(),
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    null,
                    selection,
                    new String[]{Utility.getSortOrder(getActivity())},
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //scroll only when sort order is same
        if (mPosition != 0) {
            recyclerView.smoothScrollToPosition(mPosition);
        }
        if (loader.getId() == CURSOR_ID)
            mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

            mAdapter.swapCursor(null);

    }

    public void setLayout(boolean mTwoPane) {
        if (mTwoPane){

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

            recyclerView.setLayoutManager(linearLayoutManager);

        }else {

            GridLayoutManager layoutManager;
            if (getResources().getConfiguration().orientation == 1) {
                layoutManager = new GridLayoutManager(getActivity(), 2);
            } else {
                layoutManager = new GridLayoutManager(getActivity(), 4);
            }
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void onListItemClick(int position) {

        Cursor cursor = mAdapter.getCursor();

        cursor.moveToPosition(position);

        mPosition = position;

        ((mMovieClickListener)getActivity()).OnItemClick(cursor.getString(1));


    }

    public void onSortChange() {

        getLoaderManager().restartLoader(MOVIE_TASK_LOADER,null,this);
        getLoaderManager().restartLoader(CURSOR_ID,null,this);

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        updateMovieList();
//    }
}
