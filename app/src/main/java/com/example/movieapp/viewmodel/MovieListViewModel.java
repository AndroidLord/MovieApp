package com.example.movieapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.repository.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    MovieRepository repository;



    public MovieListViewModel(){
        repository = MovieRepository.getInstance();
    }



    public LiveData<List<MovieModel>> getMovies(){
        return repository.getMovies();
    }
    public LiveData<List<MovieModel>> getPopularMoviesList(){
        return repository.getPopularMovies();
    }

    public void searchMovieApi(String query, int pageNumber){
        repository.searchMovieApi(query, pageNumber);
    }


    public void searchPopularMovieApi(int pageNumber){
        repository.searchPopularMovieApi(pageNumber);
    }

    public void searchNextPage() {
            repository.searchNextPage();

    }
}
