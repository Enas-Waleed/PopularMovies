package com.surya.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.surya.popularmovies.Utils.Utility;
import com.surya.popularmovies.data.MoviesContract;
import com.surya.popularmovies.data.MoviesDBHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks{

    private int CURSOR_ID = 3;
    private int REVIEWS_TASK_ID = 4;
    private MoviesModel moviesModel;

    public DetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("xxxx","oncreate");
        moviesModel = getActivity().getIntent().getParcelableExtra(Utility.MOVIES_OBJECT);
        getLoaderManager().initLoader(1,null,this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView backdrop_imageView = (ImageView)rootView.findViewById(R.id.backdrop_poster);
        ImageView poster_imageView = (ImageView)rootView.findViewById(R.id.poster);
        final TextView movie_name = (TextView)rootView.findViewById(R.id.movieName);
        final TextView movie_release = (TextView)rootView.findViewById(R.id.movie_release);
        TextView movie_genre_name = (TextView)rootView.findViewById(R.id.movie_genre_textView);
        final TextView movie_language = (TextView)rootView.findViewById(R.id.movie_language);
        TextView movie_votes = (TextView)rootView.findViewById(R.id.movie_rating_textView);
        final TextView movie_rating = (TextView)rootView.findViewById(R.id.movie_rating);
        final TextView movie_popularity = (TextView)rootView.findViewById(R.id.movie_popularity);
        TextView movie_overview = (TextView)rootView.findViewById(R.id.movie_overview);
        Button favButton = (Button)rootView.findViewById(R.id.fav_button);

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavourites();
            }
        });

        RecyclerView review_recyclerView = (RecyclerView)rootView.findViewById(R.id.review_recyclerview);
        RecyclerView trailer_recyclerView = (RecyclerView)rootView.findViewById(R.id.trailer_recyclerview);

        LinearLayoutManager reviewManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager trailerManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        review_recyclerView.setLayoutManager(reviewManager);
        trailer_recyclerView.setLayoutManager(trailerManager);

        TrailerAdapter trailerAdapter = new TrailerAdapter();
        ReviewAdapter reviewAdapter = new ReviewAdapter();

        review_recyclerView.setAdapter(reviewAdapter);
        trailer_recyclerView.setAdapter(trailerAdapter);

        //background for the circles
        final ImageView genreImage = (ImageView)rootView.findViewById(R.id.movie_genre_image);

        final GradientDrawable ratingCircle = (GradientDrawable) movie_language.getBackground();


        Picasso.with(getActivity())
                .load(Utility.TMDB_BACKDROP_POSTER_URL + moviesModel.getBackdrop_path())
                .placeholder(R.drawable.dummy)
                .into(backdrop_imageView);
        Picasso.with(getActivity())
                .load(Utility.TMDB_POSTER_URL+moviesModel.getPoster_path())
                .placeholder(R.drawable.dummy)
                .into(poster_imageView);

        movie_name.setText(moviesModel.getTitle());
        movie_overview.setText(moviesModel.getOverview());
        movie_release.setText(moviesModel.getRelease_date());
        movie_rating.setText(moviesModel.getVote_average());
        movie_votes.setText(getActivity().getString(R.string.formatVotes,moviesModel.getVote_count()));

        movie_genre_name.setText(moviesModel.getGenre_id());
        movie_popularity.setText(String.valueOf(Utility.formatPopularity(moviesModel.getPopularity())));
        movie_language.setText(moviesModel.getLanguage());

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
        return rootView;
    }

    private void addToFavourites() {

        MoviesDBHelper dbHelper = new MoviesDBHelper(getActivity());

        String selection = MoviesContract.MoviesEntry.COL_MOVIE_ID + " = " + moviesModel.getId();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME,
                                    new String[]{MoviesContract.MoviesEntry.COL_MOVIE_ID, MoviesContract.MoviesEntry.COL_SORT},
                                    selection,
                                    null,
                                    null,
                                    null,
                                    null);


        //check for the sort type for given movie id

        long rowId ;

        while (cursor.moveToNext()){
            Log.e("xxxx",cursor.getString(1) + " pos " + cursor.getColumnCount() );


            if (cursor.getString(1).equals(getString(R.string.pref_sort_favourite))) {

                rowId = db.delete(MoviesContract.MoviesEntry.TABLE_NAME,null,null);

                Log.e("xxx",rowId + "deleted");

                return;
            }
            cursor.moveToNext();

        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MoviesEntry.COL_MOVIE_ID,moviesModel.getId());
        contentValues.put(MoviesContract.MoviesEntry.COL_POSTER_PATH,moviesModel.getPoster_path());
        contentValues.put(MoviesContract.MoviesEntry.COL_VOTE_AVERAGE,moviesModel.getVote_average());
        contentValues.put(MoviesContract.MoviesEntry.COL_RELEASE_DATE,moviesModel.getRelease_date());
        contentValues.put(MoviesContract.MoviesEntry.COL_POPULARITY,moviesModel.getPopularity());
        contentValues.put(MoviesContract.MoviesEntry.COL_TITLE,moviesModel.getTitle());
        contentValues.put(MoviesContract.MoviesEntry.COL_BACKDROP,moviesModel.getBackdrop_path());
        contentValues.put(MoviesContract.MoviesEntry.COL_VOTE_COUNT,moviesModel.getVote_count());
        contentValues.put(MoviesContract.MoviesEntry.COL_GENRE,moviesModel.getGenre_id());
        contentValues.put(MoviesContract.MoviesEntry.COL_LANGUAGE,moviesModel.getLanguage());
        contentValues.put(MoviesContract.MoviesEntry.COL_SYNOPSIS,moviesModel.getOverview());
        contentValues.put(MoviesContract.MoviesEntry.COL_SORT,getString(R.string.pref_sort_favourite));
        rowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME,null,contentValues);

        Log.e("xxx",rowId + "inserted");

        cursor.close();
        db.close();

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.e("xxxx","oncreate loader");
        return new ReviewTaskLoader(getActivity(),moviesModel.getId());
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}

