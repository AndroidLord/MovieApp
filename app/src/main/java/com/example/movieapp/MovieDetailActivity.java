package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

// hide status bar
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Hide the navigation bar.
            if (Build.VERSION.SDK_INT >= 18) {
                uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT >= 19) {
                uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOptions);
        }

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