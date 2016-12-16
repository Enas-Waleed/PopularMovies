package com.surya.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Surya on 16-12-2016.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);

        return new TrailerAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        ImageView mPosterImage;

        ViewHolder(View itemView) {
            super(itemView);
            mPosterImage = (ImageView)itemView.findViewById(R.id.list_item_trailer_poster);
            mTitle = (TextView)itemView.findViewById(R.id.list_item_trailer_name);
        }
    }
}
