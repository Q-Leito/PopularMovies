package com.freedom_mobile.popularmovies.ui;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.freedom_mobile.popularmovies.R;
import com.freedom_mobile.popularmovies.adapters.MovieAdapter;
import com.freedom_mobile.popularmovies.model.MovieData;
import com.freedom_mobile.popularmovies.utils.AlertDialogFragment;
import com.freedom_mobile.popularmovies.utils.RecyclerTouchListener;
import com.freedom_mobile.popularmovies.utils.SpacesItemDecoration;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A list fragment representing a list of Movies. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link MovieDetailsFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MovieFragment extends Fragment {
    public static final String TAG = MovieFragment.class.getSimpleName();
    private static final String LIST_STATE = "list_state";
    public static final int PORTRAIT_MODE = 2;
    public static final int LANDSCAPE_MODE = 3;

    private Callbacks mCallbacks;
    private MovieData mMovieData;
    @Bind(R.id.popularMoviesRecyclerView) RecyclerView mRecyclerView;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when a movie has been selected.
         */
        void onMovieSelected(String id);
    }

    public static Fragment newInstance() {
        return new MovieFragment();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMovieData();
    }

    @Override
    public void onAttach(Activity activity) {
//        mAppCompatActivity = (AppCompatActivity) activity;
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        int recyclerState = mRecyclerView.getScrollState();
        state.putInt(LIST_STATE, recyclerState);
    }

    private void getMovieData() {
        String apiKey = "{API_KEY_HERE}";
        String sortBy = "popularity";
        String movieUrl = "http://api.themoviedb.org/3/discover/movie?sort_by="
                + sortBy + ".desc&api_key=" + apiKey;

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(movieUrl)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    alertAboutError();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.i(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mMovieData = getMovieDetails(jsonData);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertAboutError();
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), getString(R.string.networkUnavailable),
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private MovieData getMovieDetails(String jsonData)
            throws JSONException {
        JSONObject moviesData = new JSONObject(jsonData);

        JSONArray results = moviesData.getJSONArray("results");

        mMovieData = new MovieData();
        for (int i = 0; i < results.length(); i++) {
            JSONObject resultsArray = results.getJSONObject(i);

            mMovieData.addMovie(new MovieData.MovieDataItem(
                    String.valueOf(i),
                    resultsArray.getString("original_title"),
                    resultsArray.getString("overview"),
                    resultsArray.getString("release_date"),
                    resultsArray.getString("poster_path"),
                    resultsArray.getDouble("popularity"),
                    resultsArray.getDouble("vote_average")
            ));
        }

        return mMovieData;
    }

    private void updateDisplay() {
        float recyclerViewSpacing = getResources().getDimension(R.dimen.recyclerViewPadding);

        mRecyclerView.addItemDecoration(new SpacesItemDecoration((int) recyclerViewSpacing));
        mRecyclerView.setHasFixedSize(true);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), PORTRAIT_MODE));
        } else if (getResources().getBoolean(R.bool.landscape_only)) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), LANDSCAPE_MODE));
        }
        mRecyclerView.setAdapter(new MovieAdapter(MovieData.MOVIE_DATA));
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        mCallbacks.onMovieSelected(MovieData.MOVIE_DATA.get(position).getId());
//                        Toast.makeText(getActivity(), MovieData.MOVIE_DATA.get(position).getTitle(),
//                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    private void alertAboutError() {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.show(getActivity().getFragmentManager(), "error_dialog");
    }
}
