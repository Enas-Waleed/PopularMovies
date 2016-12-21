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
import android.widget.Toast;

import com.surya.popularmovies.Utils.TestUtlis;
import com.surya.popularmovies.Utils.Utility;
import com.surya.popularmovies.data.MoviesContract;
import com.surya.popularmovies.data.MoviesDBHelper;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener{

    private String DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG";
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (findViewById(R.id.movie_detail_container) != null){


            mTwoPane = true;

            if (savedInstanceState == null){

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.movie_detail_container,new DetailFragment(),DETAIL_FRAGMENT_TAG)
                        .commit();

            }

        }else{
            mTwoPane = false;
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
    public void onMovieSelected(String movie_id) {

        Log.e("Mainactivity",movie_id);

        if (mTwoPane){

            Bundle args = new Bundle();

            args.putString(Utility.MOVIE_ID,movie_id);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAIL_FRAGMENT_TAG)
                    .commit();

        }else{

            Intent intent = new Intent(this, DetailActivity.class);

            intent.putExtra(Utility.MOVIE_ID,movie_id);

            startActivity(intent);

        }


    }
}

