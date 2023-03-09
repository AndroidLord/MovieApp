package com.example.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieModel implements Parcelable {

    private String title;
    private String poster_path;
    private int movie_id;
    private String release_date;
    private float vote_average;

    @SerializedName("overview")
    @Expose
    private String movie_overview;

    private int runtime;

    protected MovieModel(Parcel in) {
        title = in.readString();
        poster_path = in.readString();
        movie_id = in.readInt();
        release_date = in.readString();
        vote_average = in.readFloat();
        movie_overview = in.readString();
        runtime = in.readInt();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public String getRelease_date() {
        return release_date;
    }

    public float getVote_average() {
        return vote_average;
    }


    public String getMovie_overview() {
        return movie_overview;
    }

    public int getRuntime() {
        return runtime;
    }

    public MovieModel(String title, String poster_path, int movie_id, String release_date, float vote_average, String movie_overview, int runtime) {
        this.title = title;
        this.poster_path = poster_path;
        this.movie_id = movie_id;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.movie_overview = movie_overview;
        this.runtime = runtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeInt(movie_id);
        dest.writeString(release_date);
        dest.writeFloat(vote_average);
        dest.writeString(movie_overview);
        dest.writeInt(runtime);
    }
}
