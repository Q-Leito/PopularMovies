package com.freedom_mobile.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.freedom_mobile.popularmovies.R;
import com.freedom_mobile.popularmovies.adapters.MovieAdapter;
import com.freedom_mobile.popularmovies.model.MovieData;
import com.freedom_mobile.popularmovies.utils.PopularComparator;
import com.freedom_mobile.popularmovies.utils.RatedComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailsActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MovieFragment} and the item details
 * (if present) is a {@link MovieDetailsFragment}.
 * <p/>
 * This activity also implements the required
 * {@link MovieFragment.Callbacks} interface
 * to listen for item selections.
 */
public class MainActivity extends AppCompatActivity
        implements MovieFragment.Callbacks {

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Map<String, MovieData.MovieDataItem> mPopularity = MovieData.MOVIE_DATA;
    private Map<String, MovieData.MovieDataItem> mRating = MovieData.MOVIE_DATA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();

        if (findViewById(R.id.movieDetailsFragment) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }

    /**
     * Callback method from {@link MovieFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onMovieSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(MovieDetailsFragment.MOVIE_ID, id);
            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
            movieDetailsFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movieDetailsFragment, movieDetailsFragment,
                            MovieDetailsFragment.TAG).commit();
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent movieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
            movieDetailsIntent.putExtra(MovieDetailsFragment.MOVIE_ID, id);
            startActivity(movieDetailsIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_popular:
                List<MovieData.MovieDataItem> popularityList =
                        new ArrayList<>(mPopularity.values());
                Collections.sort(popularityList, new PopularComparator());
                for (MovieData.MovieDataItem movieDataItem : popularityList)
                    mPopularity.put(movieDataItem.getId(), movieDataItem);
                MovieAdapter movieAdapter = new MovieAdapter(mPopularity);
                movieAdapter.notifyItemRangeChanged(0, mPopularity.size(), null);
                break;
            case R.id.action_high_rated:
                List<MovieData.MovieDataItem> ratedList =
                        new ArrayList<>(mRating.values());
                Collections.sort(ratedList, new RatedComparator());
                for (MovieData.MovieDataItem movieDataItem : ratedList)
                    mRating.put(movieDataItem.getId(), movieDataItem);
                MovieAdapter adapter = new MovieAdapter(mRating);
                adapter.notifyItemRangeChanged(0, mRating.size(), null);
                break;
//            case R.id.action_favorite:
//
//                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
