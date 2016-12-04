package com.surya.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Surya on 04-12-2016.
 */

public class MoviesAdapter extends RecyclerView.Adapter <MoviesAdapter.ViewHolder>{

    private List<MoviesModel> moviesList;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mRatingView;
        TextView mPopularityView;
        TextView mReleaseView;
        ImageView posterView;
        public ViewHolder(View view) {
            super(view);
            mReleaseView = (TextView)view.findViewById(R.id.list_item_year);
            mPopularityView = (TextView)view.findViewById(R.id.list_item_popularity);
            mRatingView = (TextView)view.findViewById(R.id.list_item_rating);
            posterView = (ImageView) view.findViewById(R.id.list_item_movie_poster);

        }
    }

    public MoviesAdapter(Context context, List<MoviesModel> movieList) {

        this.moviesList = movieList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        (holder.mRatingView).setText(moviesList.get(position).getVote_average() + "/10");

        String releaseDate = moviesList.get(position).getRelease_date();

        String[] year = releaseDate.split("-");

        int rating = (int) Math.round(Double.valueOf(moviesList.get(position).getPopularity()));

        holder.mPopularityView.setText(String.valueOf(rating));
        holder.mReleaseView.setText(year[0]);

        Picasso.with(mContext).load(Utility.TMDB_POSTER_URL + moviesList.get(position).getPoster_path()).into(holder.posterView);


//        Toast.makeText(mContext, moviesList.get(position).getPoster_path(), Toast.LENGTH_SHORT).show();
//        holder.posterView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext,DetailActivity.class);
//                intent.putExtra(Utility.MOVIES_OBJECT,moviesList.get(position));
//                mContext.startActivity(intent);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public  void  set(){

    }
}
