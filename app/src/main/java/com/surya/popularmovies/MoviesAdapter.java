package com.surya.popularmovies;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Surya on 04-12-2016.
 */
public class MoviesAdapter extends RecyclerView.Adapter <MoviesAdapter.ViewHolder>{

    List<MoviesModel> moviesList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mTextView2;
        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView)view.findViewById(R.id.list_item_movie_name);
            mTextView2 = (TextView)view.findViewById(R.id.list_item_movie_overview);

        }
    }

    public MoviesAdapter(Context context, List<MoviesModel> movieList) {

        this.moviesList = movieList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTextView.setText(moviesList.get(position).getTitle());
        holder.mTextView2.setText(moviesList.get(position).getOverview());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
