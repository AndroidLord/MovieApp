package com.example.movieapp.request;

import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.Utils.MovieApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Servicey {

     private static Retrofit.Builder  retrofitbuilder = new Retrofit.Builder()
             .baseUrl(Credentials.BASE_URL)
             .addConverterFactory(GsonConverterFactory.create());

     private static final Retrofit retrofit = retrofitbuilder.build();

     private static final MovieApi movieApi = retrofit.create(MovieApi.class);

     public static MovieApi getMovieApi(){

          return movieApi;
     }



}
