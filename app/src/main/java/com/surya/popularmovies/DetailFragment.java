package com.surya.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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

        ImageView imageView = (ImageView)rootView.findViewById(R.id.backdrop_poster);

        MoviesModel moviesModel = getActivity().getIntent().getParcelableExtra(Utility.MOVIES_OBJECT);

        Picasso.with(getActivity()).load(Utility.TMDB_POSTER_URL + moviesModel.getPoster_path()).into(imageView);

        return rootView;
    }
}
