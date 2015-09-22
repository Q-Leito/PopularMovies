package com.freedom_mobile.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.freedom_mobile.popularmovies.R;
import com.freedom_mobile.popularmovies.model.MovieData;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Map<String, MovieData.MovieDataItem> mMovieData;

    public MovieAdapter(Map<String,MovieData.MovieDataItem> movieData) {
        mMovieData = movieData;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_posters, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String ImageFiles = mMovieData.get(String.valueOf(position)).getMoviePoster();

        Picasso.with(viewHolder.getImageView().getContext())
                .load("http://image.tmdb.org/t/p/w185/" + ImageFiles)
                .into(viewHolder.getImageView());

        viewHolder.getTextView().setText(mMovieData.get(String.valueOf(position)).getTitle());
    }

    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;

        public ViewHolder(View view) {
            super(view);

            mImageView = (ImageView) view.findViewById(R.id.imageView);
            mTextView = (TextView) view.findViewById(R.id.moviePosterLabel);
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public TextView getTextView() {
            return mTextView;
        }
    }
}
