package com.surya.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.surya.popularmovies.Utils.Utility;

/**
 * Created by Surya on 16-12-2016.
 */

public class TrailerAdapter extends CursorRecyclerViewAdapter<TrailerAdapter.ViewHolder> {

    Context context;

    final private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int position);
    }

    public TrailerAdapter(Context context, Cursor cursor,ListItemClickListener listItemClickListener) {
        super(context, cursor);
        this.context = context;
        this.listItemClickListener = listItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {


//        DatabaseUtils.dumpCursor(cursor);

        holder.mTitle.setText(cursor.getString(2));
        Picasso.with(context)
                .load(Utility.YOUTUBE_IMG_URL + cursor.getString(3) + Utility.END_IMG_URL)
                .placeholder(R.drawable.dummy)
                .into(holder.mPosterImage);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTitle;
        ImageView mPosterImage;

         ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mPosterImage = (ImageView)itemView.findViewById(R.id.list_item_trailer_poster);
            mTitle = (TextView)itemView.findViewById(R.id.list_item_trailer_name);
        }
        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(clickPosition);
        }
    }
}
