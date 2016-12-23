package com.surya.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.CardView;
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
import com.surya.popularmovies.data.MoviesContract;

import java.util.List;

/**
 * Created by Surya on 04-12-2016.
 */

public class MoviesAdapter extends CursorRecyclerViewAdapter<MoviesAdapter.ViewHolder>{

    private Context mContext;
    private int id;
    final private ListItemClickListener mOnClickListener;

    private int COL_POSTER_PATH = 2;
    private int COL_VOTE_AVERAGE = 3;
    private int COL_RELEASE_DATE = 4;
    private int COL_POPULARITY = 5;



    public interface ListItemClickListener{
        void onListItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mRatingView;
        TextView mPopularityView;
        TextView mReleaseView;
        ImageView posterView;
        CardView cardView;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mReleaseView = (TextView)view.findViewById(R.id.list_item_year);
            mPopularityView = (TextView)view.findViewById(R.id.list_item_popularity);
            mRatingView = (TextView)view.findViewById(R.id.list_item_rating);
            posterView = (ImageView) view.findViewById(R.id.list_item_movie_poster);
            cardView = (CardView)view.findViewById(R.id.cardView);

            if (id == 1)
                cardView.getLayoutParams().width = 250;
            else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(8,8,8,8);
                cardView.setLayoutParams(layoutParams);
            }
        }


        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickPosition);
        }
    }

    public MoviesAdapter(Context context, int id, ListItemClickListener clickListener,Cursor cursor) {

        super(context,cursor);
        this.mContext = context;
        this.id = id;
        this.mOnClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {

//        DatabaseUtils.dumpCursor(cursor);

        (holder.mRatingView).setText(cursor.getString(COL_VOTE_AVERAGE) + "/10");

        String releaseDate = cursor.getString(COL_RELEASE_DATE);

        String[] year = releaseDate.split("-");

        int rating = Utility.formatPopularity(cursor.getString(COL_POPULARITY));

        holder.mPopularityView.setText(String.valueOf(rating));
        holder.mReleaseView.setText(year[0]);

        Picasso.with(mContext)
                .load(Utility.TMDB_POSTER_URL + cursor.getString(COL_POSTER_PATH))
                .placeholder(R.drawable.dummy)
                .into(holder.posterView);

    }

}
