package com.freedom_mobile.popularmovies.utils;

import com.freedom_mobile.popularmovies.model.MovieData;

import java.util.Comparator;

public class PopularComparator implements Comparator<MovieData.MovieDataItem> {

    @Override
    public int compare(MovieData.MovieDataItem lhs, MovieData.MovieDataItem rhs) {
        return ((Double) rhs.getPopularity()).compareTo(lhs.getPopularity());
    }
}
