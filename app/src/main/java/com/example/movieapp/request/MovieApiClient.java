package com.example.movieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.AppExecutors;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    private static MovieApiClient instance;
    private RetrievingMovieRunnable retrievingMovieRunnable;
    private MutableLiveData<List<MovieModel>> liveMovieList;

    public static synchronized MovieApiClient getInstance(){
        if(instance==null){
            instance = new MovieApiClient();
        }
        return instance;
    }

    public MovieApiClient() {

        liveMovieList = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return liveMovieList;
    }

    public void searchMovieApi(String query, int pageNumber){

        if(retrievingMovieRunnable!=null){
            retrievingMovieRunnable = null;
        }

        retrievingMovieRunnable = new RetrievingMovieRunnable(query,pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrievingMovieRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling the Threads
                myHandler.cancel(true);

            }
        },5000, TimeUnit.MILLISECONDS);

    }

    // Retrieving Data from RESTAPI by runnable class
    private class RetrievingMovieRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrievingMovieRunnable(String query, int pageNumber){
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Getting the response objects

            try {

                Response response = getMovies(query,pageNumber).execute();

                if(cancelRequest)
                    return;

                if(response.code()==200){

                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovieModelList());
                    if(pageNumber==1){
                        liveMovieList.postValue(list);
                    }
                    else{
                        List<MovieModel> current = liveMovieList.getValue();
                        current.addAll(list);
                        liveMovieList.postValue(current);
                    }
                }
                else {
                    String err = response.errorBody().string();
                    Log.d("TAG", "run: " + err);
                    liveMovieList.postValue(null);
                }

            } catch (IOException e) {
                liveMovieList.postValue(null);
                throw new RuntimeException(e);
            }


        }

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber){

            return Servicey.getMovieApi().searchMovies(
                    Credentials.API_KEY,
                    query,
                    String.valueOf(pageNumber)
            );
        }

    }

}
