package com.freedom_mobile.popularmovies.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A movie object containing the movies information.
 */
public class MovieData implements Parcelable {

    // Parcel keys
    private static final String KEY_ID = "movie_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_MOVIE_POSTER = "movie_poster";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_RATING = "rating";
    /**
     * Creator required for class implementing the parcelable interface.
     */
    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {

        @Override
        public MovieData createFromParcel(Parcel source) {
            // Read the bundle containing key value pairs from the parcel
            Bundle bundle = source.readBundle();

            // Instantiate a movie using values from the bundle
            return new MovieData(
                    bundle.getString(KEY_ID),
                    bundle.getString(KEY_TITLE),
                    bundle.getString(KEY_OVERVIEW),
                    bundle.getString(KEY_RELEASE_DATE),
                    bundle.getString(KEY_MOVIE_POSTER),
                    bundle.getDouble(KEY_POPULARITY),
                    bundle.getDouble(KEY_RATING));
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
    /**
     * An map of movies, by ID.
     */
    public static Map<String, MovieData> MOVIE_DATA = new LinkedHashMap<>();
    /**
     * A map of movies, by ID.
     */
    public static Map<String, MovieData> MOVIE_DATA_ITEM = new HashMap<>();
    private String mId;
    private String mTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mMoviePoster;
    private double mPopularity;
    private double mRating;

    public MovieData() {
        // Empty constructor.
    }

    public MovieData(String id, String title, String overview, String releaseDate,
                     String moviePoster, double popularity, double rating) {
        mId = id;
        mTitle = title;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mMoviePoster = moviePoster;
        mPopularity = popularity;
        mRating = rating;
    }

    public void addMovie(MovieData movieDataItem) {
        if (!MOVIE_DATA.containsKey(movieDataItem.getId())) {
            MOVIE_DATA.put(movieDataItem.getId(), movieDataItem);
        }
        if (!MOVIE_DATA_ITEM.containsKey(movieDataItem.getId())) {
            MOVIE_DATA_ITEM.put(movieDataItem.getId(), movieDataItem);
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // create a bundle for the key value pairs
        Bundle bundle = new Bundle();

        // Insert the key value pairs to the bundle
        bundle.putString(KEY_ID, mId);
        bundle.putString(KEY_TITLE, mTitle);
        bundle.putString(KEY_OVERVIEW, mOverview);
        bundle.putString(KEY_RELEASE_DATE, mReleaseDate);
        bundle.putString(KEY_MOVIE_POSTER, mMoviePoster);
        bundle.putDouble(KEY_POPULARITY, mPopularity);
        bundle.putDouble(KEY_RATING, mRating);

        dest.writeBundle(bundle);
    }
}
