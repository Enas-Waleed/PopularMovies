package com.surya.popularmovies;

/**
 * Created by Surya on 04-12-2016.
 */

public class Utility {

    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie";

    public static final String TMDB_POSTER_URL = "https://image.tmdb.org/t/p/w342/";
    public static final String TMDB_BACKDROP_POSTER_URL = "https://image.tmdb.org/t/p/w780/";
    public static final String MOVIES_OBJECT = "movies_object";
    public static final String MOVIES_ARRAY = "movies_array";

    public static int formatPopularity(String popularity) {
        return (int) Math.round(Double.valueOf(popularity));
    }

    public static String getGenreFromId(int id){

        if (id == 28)
            return "Action";

        if (id == 12)
            return "Adventure";

        if (id == 16)
            return "Animation";

        if (id == 35)
            return "Comedy";

        if (id == 80)
            return "Crime";

        if (id == 99)
            return "Documentary";

        if (id == 18)
            return "Drama";

        if (id == 10751)
            return "Family";

        if (id == 14)
            return "Fantasy";

        if (id == 36)
            return "History";

        if (id == 27)
            return "Horror";

        if (id == 10402)
            return "Music";

        if (id == 9648)
            return "Mystery";

        if (id == 10749)
            return "Romance";

        if (id == 878)
            return "Science Fiction";

        if (id == 10770)
            return "TV Movie";

        if (id == 53)
            return "Thriller";


        if (id == 10752)
            return "War";


        if (id == 37)
            return "Western";


        return null;
    }

}
