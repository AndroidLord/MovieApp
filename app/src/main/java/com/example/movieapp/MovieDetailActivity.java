package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movieapp.models.MovieModel;
import com.google.android.material.tabs.TabLayout;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView movieImageDetails;
    private TextView movieTitleDetails,movieDescriptionDetails;
    private RatingBar ratingBarDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // ID's
        movieTitleDetails = findViewById(R.id.movie_title_detail);
        movieDescriptionDetails = findViewById(R.id.movie_description_detail);
        movieImageDetails = findViewById(R.id.movie_img_detail);
        ratingBarDetails = findViewById(R.id.ratingBar_detail);


        getDatafromIntent();

    }

    private void getDatafromIntent() {

        if(getIntent().hasExtra("movie")){
            MovieModel movieModel = getIntent().getParcelableExtra("movie");

            Log.d("d", " Movie Description " + movieModel.getMovie_overview());

            movieTitleDetails.setText(movieModel.getTitle());
            movieDescriptionDetails.setText(movieModel.getMovie_overview());
            ratingBarDetails.setRating(movieModel.getVote_average()/2);

            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+movieModel.getPoster_path())
                    .into(movieImageDetails);

        }

    }
}