package com.freedom_mobile.popularmovies.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieData {

    /**
     * An array of movies.
     */
    public static List<MovieDataItem> MOVIE_DATA = new ArrayList<>();

    /**
     * A map of movies, by ID.
     */
    public static Map<String, MovieDataItem> MOVIE_DATA_ITEM = new HashMap<>();

    public void addMovie(MovieDataItem movieDataItem) {
        MOVIE_DATA.add(movieDataItem);
        MOVIE_DATA_ITEM.put(movieDataItem.mId, movieDataItem);
    }

    /**
     * A movie object containing the movies information.
     */
    public static class MovieDataItem {
        private String mId;
        private String mTitle;
        private String mOverview;
        private String mReleaseDate;
        private String mMoviePoster;
        private double mPopularity;
        private double mRating;


        public MovieDataItem(String id, String title, String overview, String releaseDate,
                             String moviePoster, double popularity, double rating) {
            mId = id;
            mTitle = title;
            mOverview = overview;
            mReleaseDate = releaseDate;
            mMoviePoster = moviePoster;
            mPopularity = popularity;
            mRating = rating;
        }

        public String getId() {
            return mId;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getOverview() {
            return mOverview;
        }

        public String getReleaseDate() {
            return mReleaseDate;
        }

        public String getMoviePoster() {
            return mMoviePoster;
        }

        public double getPopularity() {
            return mPopularity;
        }

        public double getRating() {
            return mRating;
        }
    }
}
