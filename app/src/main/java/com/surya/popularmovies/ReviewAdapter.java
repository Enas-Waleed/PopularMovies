package com.surya.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Surya on 16-12-2016.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);

        return new ReviewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mUserName;
        TextView mContent;
        TextView mUrl;

        ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView)itemView.findViewById(R.id.list_item_user_name);
            mContent = (TextView)itemView.findViewById(R.id.list_item_review_textview);
            mUrl = (TextView)itemView.findViewById(R.id.list_item_url);

        }
    }
}
