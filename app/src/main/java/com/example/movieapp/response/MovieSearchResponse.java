package com.example.movieapp.response;

import com.example.movieapp.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSearchResponse {



    @SerializedName("results")
    @Expose
    private List<MovieModel> movieModelList;

    public int getTotalCount() {
        return movieModelList.size();
    }

    public List<MovieModel> getMovieModelList() {
        return movieModelList;
    }
}
