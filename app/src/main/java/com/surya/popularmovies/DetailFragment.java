package com.surya.popularmovies;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView backdrop_imageView = (ImageView)rootView.findViewById(R.id.backdrop_poster);
        ImageView poster_imageView = (ImageView)rootView.findViewById(R.id.poster);
        TextView movie_name = (TextView)rootView.findViewById(R.id.movieName);
        TextView movie_release = (TextView)rootView.findViewById(R.id.movie_release);
        TextView movie_genre_name = (TextView)rootView.findViewById(R.id.movie_genre_textView);
        TextView movie_language = (TextView)rootView.findViewById(R.id.movie_language);
        TextView movie_votes = (TextView)rootView.findViewById(R.id.movie_rating_textView);
        TextView movie_rating = (TextView)rootView.findViewById(R.id.movie_rating);
        TextView movie_popularity = (TextView)rootView.findViewById(R.id.movie_popularity);
        TextView movie_overview = (TextView)rootView.findViewById(R.id.movie_overview);
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(manager);

        MoviesModel moviesModel = getActivity().getIntent().getParcelableExtra(Utility.MOVIES_OBJECT);

        List<MoviesModel> modelList = new ArrayList<>();

        modelList.add(moviesModel);
        modelList.add(moviesModel);
        modelList.add(moviesModel);
        modelList.add(moviesModel);

        MoviesAdapter adapter = new MoviesAdapter(getActivity(),modelList,1);
        recyclerView.setAdapter(adapter);

        Picasso.with(getActivity())
                .load(Utility.TMDB_POSTER_URL+moviesModel.getBackdrop_path())
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
        movie_genre_name.setText("dummy");
        movie_popularity.setText(String.valueOf(Utility.formatPopularity(moviesModel.getPopularity())));
        movie_language.setText(moviesModel.getLanguage());

        return rootView;
    }

}

