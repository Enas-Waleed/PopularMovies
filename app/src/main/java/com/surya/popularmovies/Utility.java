package com.surya.popularmovies;

/**
 * Created by Surya on 04-12-2016.
 */

public class Utility {

    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie";

    public static final String TMDB_POSTER_URL = "https://image.tmdb.org/t/p/w500/";
    public static final String MOVIES_OBJECT = "movies_object";

    public static int formatPopularity(String popularity) {
        return (int) Math.round(Double.valueOf(popularity));
    }
}
