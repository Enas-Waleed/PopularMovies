package com.surya.popularmovies;

/**
 * Created by Surya on 04-12-2016.
 */

public class MoviesModel {

   private String poster_path;
   private String overview;
   private String release_date;
   private String id;
   private String title;
   private String backdrop_path;
   private String popularity;
   private String vote_count;
   private String vote_average;


    public MoviesModel(String poster_path, String overview, String release_date, String id,
                       String title, String backdrop_path, String popularity, String vote_count,
                       String vote_average) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.id = id;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getVote_count() {
        return vote_count;
    }

    public String getVote_average() {
        return vote_average;
    }
}
