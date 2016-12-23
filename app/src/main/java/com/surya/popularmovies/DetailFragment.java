package com.surya.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.surya.popularmovies.Utils.Utility;
import com.surya.popularmovies.data.MoviesContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,TrailerAdapter.ListItemClickListener{


    private final String LOG_TAG = "DetailFragment";

    private int CURSOR_ID = 3;
    private int REVIEWS_TASK_ID = 4;
    private int TRAILERS_ID = 5;
    private int REVIEWS_ID = 6;

    private String movie_id;
    private ImageView backdrop_imageView,genreImage;
    private ImageView poster_imageView;
    private  TextView movie_name,movie_release,
            movie_genre_name,movie_language,
            movie_popularity,movie_votes,
            movie_rating,movie_overview;

    Button favButton;
    String poster_path,backdrop_path;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    RelativeLayout relativeLayout;
    private String mMovieString;

    public DetailFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(LOG_TAG,"onActivity created");

        if (movie_id != null) {
            getLoaderManager().initLoader(CURSOR_ID, null, this).forceLoad();
            getLoaderManager().initLoader(REVIEWS_TASK_ID, null, this);
            getLoaderManager().initLoader(TRAILERS_ID, null, this);
            getLoaderManager().initLoader(REVIEWS_ID, null, this);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.content_detail);

        Bundle arguments = getArguments();
        if(arguments != null) {
            movie_id = arguments.getString(Utility.MOVIE_ID);
        }else{
            relativeLayout.setVisibility(View.GONE);
        }

        backdrop_imageView = (ImageView)rootView.findViewById(R.id.backdrop_poster);
        poster_imageView = (ImageView)rootView.findViewById(R.id.poster);
        movie_name = (TextView)rootView.findViewById(R.id.movieName);
        movie_release = (TextView)rootView.findViewById(R.id.movie_release);
        movie_genre_name = (TextView)rootView.findViewById(R.id.movie_genre_textView);
        movie_language = (TextView)rootView.findViewById(R.id.movie_language);
        movie_votes = (TextView)rootView.findViewById(R.id.movie_rating_textView);
        movie_rating = (TextView)rootView.findViewById(R.id.movie_rating);
        movie_popularity = (TextView)rootView.findViewById(R.id.movie_popularity);
        movie_overview = (TextView)rootView.findViewById(R.id.movie_overview);
        favButton = (Button)rootView.findViewById(R.id.fav_button);

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavourites();
            }
        });


        RecyclerView review_recyclerView = (RecyclerView)rootView.findViewById(R.id.review_recyclerview);
        RecyclerView trailer_recyclerView = (RecyclerView)rootView.findViewById(R.id.trailer_recyclerview);

        LinearLayoutManager reviewManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager trailerManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        review_recyclerView.setLayoutManager(reviewManager);
        trailer_recyclerView.setLayoutManager(trailerManager);

        trailer_recyclerView.setNestedScrollingEnabled(false);
        review_recyclerView.setNestedScrollingEnabled(false);

        trailerAdapter = new TrailerAdapter(getActivity(),null,this);
        reviewAdapter = new ReviewAdapter(getActivity(),null);

        review_recyclerView.setAdapter(reviewAdapter);
        trailer_recyclerView.setAdapter(trailerAdapter);

        //background for the circles
        genreImage = (ImageView)rootView.findViewById(R.id.movie_genre_image);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail_fragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mMovieString != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
    }


    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovieString + "\n#PopularMovies\nBy Surya Teja");
        return shareIntent;
    }

    private void addToFavourites() {

        String selection = MoviesContract.MoviesEntry.COL_MOVIE_ID + " = " + movie_id;

        Cursor cursor = getActivity().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI
                ,new String[]{MoviesContract.MoviesEntry.COL_MOVIE_ID, MoviesContract.MoviesEntry.COL_SORT}
                ,selection
                ,null
                ,null
        );

        //check for the sort type for given movie id

        long rowId ;

        if (cursor!=null)
            while (cursor.moveToNext()){

                if (cursor.getString(1).equals(getString(R.string.pref_sort_favourite))) {

                    String where = MoviesContract.MoviesEntry.COL_MOVIE_ID + " =? AND " + MoviesContract.MoviesEntry.COL_SORT + " =?";

                    String[] whereArgs = new String[]{movie_id,getString(R.string.pref_sort_favourite)};

                    rowId = getActivity().getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI
                            ,where,whereArgs);

//                Log.e("xxx",rowId + "deleted");


                    cursor.close();
                    return;
                }
                cursor.moveToNext();

            }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MoviesEntry.COL_MOVIE_ID,movie_id);
        contentValues.put(MoviesContract.MoviesEntry.COL_POSTER_PATH,poster_path);
        contentValues.put(MoviesContract.MoviesEntry.COL_VOTE_AVERAGE,movie_rating.getText().toString());
        contentValues.put(MoviesContract.MoviesEntry.COL_RELEASE_DATE,movie_release.getText().toString());
        contentValues.put(MoviesContract.MoviesEntry.COL_POPULARITY,movie_popularity.getText().toString());
        contentValues.put(MoviesContract.MoviesEntry.COL_TITLE,movie_name.getText().toString());
        contentValues.put(MoviesContract.MoviesEntry.COL_BACKDROP,backdrop_path);
        contentValues.put(MoviesContract.MoviesEntry.COL_VOTE_COUNT,movie_votes.getText().toString());
        contentValues.put(MoviesContract.MoviesEntry.COL_GENRE,movie_genre_name.getText().toString());
        contentValues.put(MoviesContract.MoviesEntry.COL_LANGUAGE,movie_language.getText().toString());
        contentValues.put(MoviesContract.MoviesEntry.COL_SYNOPSIS,movie_overview.getText().toString());
        contentValues.put(MoviesContract.MoviesEntry.COL_SORT,getString(R.string.pref_sort_favourite));
        Uri uri = getActivity().getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI,contentValues);

//        Log.e("xxx",uri + "inserted");


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        Log.e(LOG_TAG,"oncreate loader");

        if (id == REVIEWS_TASK_ID)
            return new ReviewTaskLoader(getActivity(),movie_id);
        else if (id == CURSOR_ID) {

            String selection = MoviesContract.MoviesEntry.COL_MOVIE_ID + " = ?";

            return new CursorLoader(getActivity(),
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    null,
                    selection,
                    new String[]{movie_id},
                    null
            );
        }else if (id == TRAILERS_ID) {

            String selection = MoviesContract.TrailerEntry.COL_MOVIE_ID + " = ?";

            return new CursorLoader(getActivity(),
                    MoviesContract.TrailerEntry.CONTENT_URI,
                    null,
                    selection,
                    new String[]{movie_id},
                    null
            );
        }else if (id == REVIEWS_ID) {

            String selection = MoviesContract.ReviewEntry.COL_MOVIE_ID + " = ?";

            return new CursorLoader(getActivity(),
                    MoviesContract.ReviewEntry.CONTENT_URI,
                    null,
                    selection,
                    new String[]{movie_id},
                    null
            );
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (loader.getId() == CURSOR_ID){

            cursor.moveToFirst();
            final GradientDrawable ratingCircle = (GradientDrawable) movie_language.getBackground();

            poster_path = cursor.getString(2);
            backdrop_path = cursor.getString(7);

            if (cursor.getString(12).equals(getString(R.string.pref_sort_favourite)))
                favButton.setText(getString(R.string.remove_from_fav));
            else
                favButton.setText(getString(R.string.add_to_favourites));

            Picasso.with(getActivity())
                    .load(Utility.TMDB_BACKDROP_POSTER_URL + backdrop_path)
                    .placeholder(R.drawable.dummy)
                    .into(backdrop_imageView);
            Picasso.with(getActivity())
                    .load(Utility.TMDB_POSTER_URL + poster_path)
                    .placeholder(R.drawable.dummy)
                    .into(poster_imageView);

            movie_name.setText(cursor.getString(6));
            movie_overview.setText(cursor.getString(11));
            movie_release.setText(cursor.getString(4));
            movie_rating.setText(cursor.getString(3));
            movie_votes.setText(cursor.getString(8));
            movie_genre_name.setText(cursor.getString(9));
            movie_popularity.setText(String.valueOf(Utility.formatPopularity(cursor.getString(5))));
            movie_language.setText(cursor.getString(10));

            mMovieString = cursor.getString(6) + "\n" + Utility.TMDB_WEB_URL + cursor.getString(1);

            Bitmap bitmap = ((BitmapDrawable)poster_imageView.getDrawable()).getBitmap();;

            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener(){

                @Override
                public void onGenerated(Palette palette) {

                    Palette.Swatch swatch = palette.getDarkVibrantSwatch();

                    if (swatch != null){
                        ratingCircle.setColor(swatch.getRgb());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            genreImage.setBackground(ratingCircle);
                            movie_popularity.setBackground(ratingCircle);
                            movie_language.setBackground(ratingCircle);
                            movie_rating.setBackground(ratingCircle);
                        }
                    }

                }
            });
            relativeLayout.setVisibility(View.VISIBLE);
        }
        else{
            if (loader.getId() == TRAILERS_ID){
                trailerAdapter.swapCursor(cursor);
            }
            if (loader.getId() == REVIEWS_ID){
                reviewAdapter.swapCursor(cursor);
            }


        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        if (loader.getId() == TRAILERS_ID){
            trailerAdapter.swapCursor(null);
        }
        if (loader.getId() == REVIEWS_ID){
            reviewAdapter.swapCursor(null);
        }
    }

    public void onSortChange() {

        relativeLayout.setVisibility(View.GONE);

        Log.e(LOG_TAG,"sort changed");

        if (movie_id != null) {
            getLoaderManager().restartLoader(CURSOR_ID, null, this).forceLoad();
            getLoaderManager().restartLoader(REVIEWS_TASK_ID, null, this);
            getLoaderManager().restartLoader(TRAILERS_ID, null, this);
            getLoaderManager().restartLoader(REVIEWS_ID, null, this);
        }

    }

    @Override
    public void onListItemClick(int position) {


        Cursor cursor = trailerAdapter.getCursor();

        cursor.moveToPosition(position);

        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(Utility.YOUTUBE_URL + cursor.getString(3)));

        startActivity(intent);

    }

}

