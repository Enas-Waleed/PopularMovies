package com.surya.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by Surya on 17-12-2016.
 */

public final class MoviesContract {


    public static final class FavouriteEntry implements BaseColumns{

        public static final String TABLE_NAME = "Favourites";

        //column movie id
        public static final String COL_MOVIE_ID = "movie_id";

        //column poster path
        public static final String COL_POSTER_PATH = "poster_path";

        //column release date
        public static final String COL_RELEASE_DATE = "release_date";

        //column rating
        public static final String COL_VOTE_AVERAGE = "rating";

        //column popularity
        public static final String COL_POPULARITY = "popularity";

        //column title
        public static final String COL_TITLE = "title";

        //column movie id
        public static final String COL_BACKDROP = "backdrop_path";

        //column vote count
        public static final String COL_VOTE_COUNT = "vote_count";

        //column genre
        public static final String COL_GENRE = "genre";

        //column language
        public static final String COL_LANGUAGE = "language";

        //column synopsis
        public static final String COL_SYNOPSIS = "synopsis";

    }

    public static final class TrailerEntry implements BaseColumns{

        //column trailer link
        public static final String COL_LINK = "trailer_link";

        //column trailer name
        public static final String COL_TRAILER_NAME = "trailer_name";

    }

    public static final class ReviewEntry implements BaseColumns{

        //column username
        public static final String COL_AUTHOR = "author";

        //column user review
        public static final String COL_CONTENT = "content";

        //col url link
        public static final String COL_URL = "url_link";

    }

}
