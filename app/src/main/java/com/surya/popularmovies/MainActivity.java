package com.surya.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.surya.popularmovies.Utils.Utility;
import com.surya.popularmovies.data.MoviesContract;
import com.surya.popularmovies.data.MoviesDBHelper;

public class MainActivity extends AppCompatActivity implements MoviesFragment.mMovieClickListener {

    private static final String DF_TAG = "DFTAG";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean mTwoPane;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSortOrder = Utility.getSortOrder(this);
//        Log.e(LOG_TAG,"OnCreate");

        if (findViewById(R.id.movie_detail_container) != null){

            mTwoPane = true;
//            Log.e(LOG_TAG,"two pane");

            if (savedInstanceState == null){
//                Log.e(LOG_TAG,"saved instance null");

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.movie_detail_container,new DetailFragment(),DF_TAG)
                        .commit();

            }

        }else {
            mTwoPane = false;
        }

        MoviesFragment mf = (MoviesFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_main);

        if (mf != null){

            mf.setLayout(mTwoPane);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        String sortOrder = Utility.getSortOrder(this);

        Log.e(LOG_TAG,":onresume.....");
        if (mSortOrder != null && !mSortOrder.equals(sortOrder)){


            MoviesFragment mf = (MoviesFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_main);

            if (mf != null){

                Log.e(LOG_TAG,":onresume.....++++++");

                mf.onSortChange();

            }

            DetailFragment df = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DF_TAG);

            if (df != null){

                df.onSortChange();
            }

            mSortOrder = sortOrder;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this,SettingsActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemClick(String movie_id) {

        if (mTwoPane){

            Bundle bundle = new Bundle();

            bundle.putString(Utility.MOVIE_ID,movie_id);

            DetailFragment fragment = new DetailFragment();

            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container,fragment).commit();

        }else {

            Intent intent = new Intent(this, DetailActivity.class);

            intent.putExtra(Utility.MOVIE_ID, movie_id);

            startActivity(intent);
        }

    }
}

