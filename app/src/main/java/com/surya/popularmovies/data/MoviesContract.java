package com.surya.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Surya on 19-12-2016.
 */

public class MoviesContract {


    public static final String CONTENT_AUTHORITY = "com.surya.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_TRAILERS = "Trailers";
    public static final String PATH_MOVIES = "Movies";
    public static final String PATH_REVIEWS = "Reviews";

    public static final class MoviesEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "Movies";

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

        public static final String COL_SORT = "sort_by";
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;;

        public static Uri buildMovies(long rowId) {
            return ContentUris.withAppendedId(CONTENT_URI,rowId);
        }
    }

    public static final class TrailerEntry implements BaseColumns {


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String TABLE_NAME = "trailers";

        public static final String COL_MOVIE_ID = "movie_id";

        //column trailer link
        public static final String COL__TRAILER_LINK = "trailer_link";

        //column trailer name
        public static final String COL_TRAILER_NAME = "trailer_name";

        public static Uri buildTrailers(long rowId) {
            return ContentUris.withAppendedId(CONTENT_URI,rowId);
        }


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;;

    }

    public static final class ReviewEntry implements BaseColumns{


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();


        public static final String COL_MOVIE_ID = "movie_id";

        //column username
        public static final String COL_AUTHOR = "author";

        //column user review
        public static final String COL_CONTENT = "content";

        //col url link
        public static final String COL_URL = "url_link";

        public static final String TABLE_NAME = "reviews";

        public static Uri buildReviews(long rowId) {
            return ContentUris.withAppendedId(CONTENT_URI,rowId);

        }


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;;



    }


}
