package com.surya.popularmovies;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.surya.popularmovies.Utils.Utility;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks{

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

        String formatGenre;

        //format  genre of only one type
        if(moviesModel.getGenre_id().length == 1){

            formatGenre = Utility.getGenreFromId((moviesModel.getGenre_id())[0]);

        }else {
            //format  genre of only two type
            formatGenre = getActivity().getString(R.string.formatGenre,
                    Utility.getGenreFromId((moviesModel.getGenre_id())[0]),
                    Utility.getGenreFromId((moviesModel.getGenre_id())[1]));
        }
        movie_genre_name.setText(formatGenre);
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

