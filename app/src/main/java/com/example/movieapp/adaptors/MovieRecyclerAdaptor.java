package com.example.movieapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.models.MovieModel;

import java.util.List;

public class MovieRecyclerAdaptor extends RecyclerView.Adapter<MovieViewHolder> {

    private OnMovieListener onMovieListener;
    private List<MovieModel> modelList;

    private Context context;

    public MovieRecyclerAdaptor(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }



    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_list_item,parent,false);
        return new MovieViewHolder(view,onMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        MovieModel movieModel= modelList.get(position);

        holder.title.setText(movieModel.getTitle());
        holder.run_time.setText(String.valueOf(movieModel.getVote_average()).substring(0,3));
        holder.release_data.setText(movieModel.getRelease_date());

        holder.ratingBar.setRating((movieModel.getVote_average())/2);
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w500/"+movieModel.getPoster_path())
                .into(holder.movieImage);


    }

    @Override
    public int getItemCount() {

        if(modelList!=null)
            return modelList.size();

        return 0;

    }

    public void setModelList(List<MovieModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    //  Getting the id's of the selected movie
    public MovieModel getSelectedMovie(int position){

        if( modelList != null ){
            if( modelList.size() > 0 ){
                return modelList.get(position);
            }
        }


        return null;
    }

}
