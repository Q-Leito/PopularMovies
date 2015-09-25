package com.freedom_mobile.popularmovies.utils;

import com.freedom_mobile.popularmovies.model.MovieData;

import java.util.Comparator;

public class PopularComparator implements Comparator<MovieData> {

    @Override
    public int compare(MovieData lhs, MovieData rhs) {
        return ((Double) rhs.getPopularity()).compareTo(lhs.getPopularity());
    }
}
