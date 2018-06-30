package com.flixster.konceq.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.flixster.konceq.flixster.models.Config;
import com.flixster.konceq.flixster.models.GlideApp;
import com.flixster.konceq.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    //the movie to display
    Movie movie;

    //the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView imageView;

    Config config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        //resolve the view objects
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        imageView = findViewById(R.id.imageView);


        //unwrap the movie passed in via intent, using its simple name as a key
        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        config = Parcels.unwrap(getIntent().getParcelableExtra(Config.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        //set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        //vote average is 0..10, convert to 0..5 by diving by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        int placeholderId = R.drawable.flicks_movie_placeholder;
        //load image using glide
        GlideApp.with(this)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(25, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);
    }
}
