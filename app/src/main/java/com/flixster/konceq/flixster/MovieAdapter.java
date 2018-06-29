package com.flixster.konceq.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flixster.konceq.flixster.models.Config;
import com.flixster.konceq.flixster.models.GlideApp;
import com.flixster.konceq.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    //list of movies
    ArrayList<Movie> movies;
    //config needed for image urls
    Config config;
    //context for rendering
    Context context;

    public void setConfig(Config config) {
        this.config = config;
    }

    public MovieAdapter(ArrayList<Movie> movies){
        this.movies = movies;
    }

    //create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        //track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            //lookup view objects by id
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        //when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
            //gets item position
            int position = getAdapterPosition();
            //make sure the position is valid, i.e. actually exists in the view
            if(position != RecyclerView.NO_POSITION){
                //get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                //create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                //serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                Toast.makeText(context, "Go back to view other movies", Toast.LENGTH_LONG).show();
                //show the activity
                context.startActivity(intent);

            }
        }
    }

    //creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        //return a new ViewHolder
        return new ViewHolder(movieView);
    }

    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get the movie data at the specified position
        Movie movie = movies.get(position);
        //populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        //build url for poster image
        String imageUrl = null;

        //if in portrait mode, load the poster image
        if(isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        }
        else{
            //load the backdrop image
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        //get the correct placeholder and imageview for the current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        //load image using glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(25, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);
    }

    //returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }
}
