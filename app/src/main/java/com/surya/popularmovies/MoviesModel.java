package com.surya.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Surya on 04-12-2016.
 */

public class MoviesModel implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeString(popularity);
        dest.writeString(vote_count);
        dest.writeString(vote_average);

    }

    @Override
    public String toString() {
        return poster_path + overview + release_date + id+ title+backdrop_path+popularity+vote_count+vote_average;
    }
    //Creator

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public MoviesModel createFromParcel(Parcel source) {
            return new MoviesModel(source);
        }

        @Override
        public MoviesModel[] newArray(int size) {
            return new MoviesModel[size];
        }

    };

    public MoviesModel(Parcel in) {
        popularity = in.readString();
        overview = in.readString();
        release_date = in.readString();
        id = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        poster_path = in.readString();
        vote_count = in.readString();
        vote_average = in.readString();
    }


}
