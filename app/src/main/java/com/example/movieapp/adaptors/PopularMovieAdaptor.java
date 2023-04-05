package com.example.movieapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.models.MovieModel;

import java.util.List;

public class PopularMovieAdaptor extends RecyclerView.Adapter<PopularMovieAdaptor.ViewHolder> {

    private Context context;
    private List<MovieModel> list;

    public PopularMovieAdaptor(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public PopularMovieAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movie_item,parent,false);

       return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PopularMovieAdaptor.ViewHolder holder, int position) {

        MovieModel movieModel= list.get(position);

        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w500/"+movieModel.getPoster_path())
                .into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.size();

        return 0;
    }

    public void setModelList(List<MovieModel> modelList) {
        this.list = modelList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.movieImage_Popular_item);

        }
    }
}
