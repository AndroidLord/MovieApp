package com.example.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.Utils.MovieApi;
import com.example.movieapp.adaptors.MovieRecyclerAdaptor;
import com.example.movieapp.adaptors.OnMovieListener;
import com.example.movieapp.adaptors.PopularMovieAdaptor;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.request.Servicey;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.viewmodel.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView recyclerView,mainRecyclerView;
    private OnMovieListener onMovieListener;
    private MovieRecyclerAdaptor adaptor;
    private PopularMovieAdaptor popularAdaptor;
    private MovieListViewModel viewModel;

    List<MovieModel> popularList;
    private LinearLayout mainScreenLL,recyclerLL;
    private TextView pickOfTheDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //ID's
        recyclerView = findViewById(R.id.recyclerView);
        mainRecyclerView = findViewById(R.id.mainScreenRecyclerView);
        mainScreenLL = findViewById(R.id.mainScreenLinearLayout);
        recyclerLL = findViewById(R.id.recyclerLinearLayout);
        pickOfTheDay = findViewById(R.id.movieOfTheDayTextView);

        // View Model
        viewModel = new ViewModelProvider(this).get(MovieListViewModel.class);


        popularMovieApi(1);

        SetupSearchView();

        configureRecyclerView();



        ObserveAnyChanges();
        ObservePopularChanges();



    }

    private void ObservePopularChanges() {


        viewModel.getPopularMoviesList().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Observe Any Changes

                if(movieModels!=null){
                    popularAdaptor.setModelList(movieModels);

                }


            }
        });


    }

    private void searchMovieApi(String query, int pageNumber){
        viewModel.searchMovieApi(query,pageNumber);
    }

    private void popularMovieApi(int pageNumber){
        viewModel.searchPopularMovieApi(pageNumber);
    }

    private void ObserveAnyChanges(){

        viewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Observe Any Changes

                if(movieModels!=null){
                    adaptor.setModelList(movieModels);

                }


            }
        });

    }


    private void configureRecyclerView(){

        // Main Recycler View
        popularAdaptor = new PopularMovieAdaptor(MainActivity.this);
        mainRecyclerView.setAdapter(popularAdaptor);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));


        // search Recycler View
        adaptor = new MovieRecyclerAdaptor(this);

        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(!recyclerView.canScrollVertically(1))
                {
                    viewModel.searchNextPage();
                }

            }
        });

    }

    private void getRetrofitResponseToId() {

        MovieApi movieApi = Servicey.getMovieApi();

        Call<MovieModel> responseCall = movieApi.getMovie(500,Credentials.API_KEY);

        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse( Call<MovieModel> call, Response<MovieModel> response) {

                if(response.code()==200){

                    Toast.makeText(MainActivity.this, "Success by ID", Toast.LENGTH_SHORT).show();

                    MovieModel movie = response.body();
                    assert movie != null;
                    Log.d("TAG", "Movie Name " + movie.getTitle());
                }

                else {
                    try {
                        Toast.makeText(MainActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                        assert response.errorBody() != null;
                        Log.d("TAG", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });

    }

    private void getRetrofitResponse() {

        MovieApi movieApi = Servicey.getMovieApi();

        Call<MovieSearchResponse> responseCall = movieApi.searchMovies(
                Credentials.API_KEY,
                "Action",
                "1"
        );

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {

                if(response.code()==200){
                    Toast.makeText(MainActivity.this, "Search Working", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onResponse: " + response.body());

                    assert response.body() != null;
                    List<MovieModel> movies = new ArrayList<>(response.body().getMovieModelList());

                    for (MovieModel movie: movies){
                        Log.d("TAG", "onResponse: Movie " + movie.getTitle());
                    }

                }
                else {
                    assert response.errorBody() != null;
                    Toast.makeText(MainActivity.this, "It Failed " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG", response.errorBody().toString() );

                }

            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {



            }
        });

    }

    @Override
    public void onMovieClick(int position) {

        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra("movie",adaptor.getSelectedMovie(position));
        startActivity(intent);

    }

    @Override
    public void onCategoryClick(String category) {

    }


    private void SetupSearchView(){

        final SearchView searchView = findViewById(R.id.search_View);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickOfTheDay.setVisibility(View.GONE);
                mainScreenLL.setVisibility(View.GONE);
                recyclerLL.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        ImageView closeButton = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchView.setIconified(true);

                pickOfTheDay.setVisibility(View.VISIBLE);
                mainScreenLL.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);


            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchMovieApi(query,1);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                pickOfTheDay.setVisibility(View.GONE);
                mainScreenLL.setVisibility(View.GONE);
                recyclerLL.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {

        if(recyclerLL.getVisibility()==View.VISIBLE){

            recyclerView.setVisibility(View.GONE);
            recyclerLL.setVisibility(View.GONE);

        }
        else if(mainScreenLL.getVisibility()==View.GONE){

            pickOfTheDay.setVisibility(View.VISIBLE);
            mainScreenLL.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            recyclerLL.setVisibility(View.GONE);

        }
        else{
            super.onBackPressed();
        }
    }


}