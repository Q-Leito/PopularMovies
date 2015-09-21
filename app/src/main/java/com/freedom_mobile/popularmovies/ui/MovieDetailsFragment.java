package com.freedom_mobile.popularmovies.ui;

import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.freedom_mobile.popularmovies.R;
import com.freedom_mobile.popularmovies.model.MovieData;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailsActivity}
 * on handsets.
 */
public class MovieDetailsFragment extends Fragment
                    implements ObservableScrollViewCallbacks {

    public static final String TAG = MovieDetailsFragment.class.getSimpleName();

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String MOVIE_ID = "movie_id";

    @Bind(R.id.imageView) ImageView mImageView;
    @Bind(R.id.textView) TextView mTextView;
    @Bind(R.id.releaseDateView) TextView mReleaseDateView;
    @Bind(R.id.ratingView) TextView mRatingView;
    @Bind(R.id.scrollView) ObservableScrollView mScrollView;
    @Bind(R.id.fab) View mFab;

    private MovieData.MovieDataItem mMovieDataItem;
    private Toolbar mToolbarView;
    private View mGradientView;
    private int mParallaxShowFabOffset;
    private int mParallaxImageHeight;
    private int mActionBarSize;
    private int mFabMargin;
    private boolean mFabIsShown;
    private int mWidth;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(MOVIE_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mMovieDataItem = MovieData.MOVIE_DATA_ITEM.get(getArguments().getString(MOVIE_ID));
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);

        if (mMovieDataItem != null) {
            String movieTitle = mMovieDataItem.getTitle();
            String movieOverView = mMovieDataItem.getOverview();
            String movieReleaseDate = mMovieDataItem.getReleaseDate();
            String moviePoster = mMovieDataItem.getMoviePoster();
            double movieRating = mMovieDataItem.getRating();

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(movieTitle);
            }

            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w780/" + moviePoster)
                    .into(mImageView);

            mTextView.setText(movieOverView);
            mReleaseDateView.setText("Release date: " + movieReleaseDate);
            mRatingView.setText("Movie rating: " + String.valueOf(movieRating) + "/10");
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Point size = new Point();
        WindowManager windowManager = getActivity().getWindowManager();

        windowManager.getDefaultDisplay().getSize(size);
        mWidth = size.x;

        if (getResources().getBoolean(R.bool.portrait_only)) {
            mGradientView = getActivity().findViewById(R.id.gradientHeader);
            mGradientView.bringToFront();
            mToolbarView = (Toolbar) getActivity().findViewById(R.id.toolBar);
            mToolbarView.bringToFront();
            mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(
                    0, getResources().getColor(R.color.primary)));
        }

        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(
                R.dimen.parallax_image_height);
        mParallaxShowFabOffset = getResources().getDimensionPixelSize(
                R.dimen.parallax_image_show_fab_offset);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "FAB is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        mActionBarSize = getActionBarSize();

        final int finalMWidth = mWidth;
        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                if (mFab != null) {
                    ViewHelper.setTranslationX(mFab, finalMWidth - mFabMargin - mFab.getWidth());
                    ViewHelper.setTranslationY(mFab, mParallaxImageHeight - mFab.getHeight() / 2);
                }
            }
        });
        showFab();
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray styledAttributes = getActivity().obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = styledAttributes.getDimensionPixelSize(indexOfAttrTextSize, -1);
        styledAttributes.recycle();

        return actionBarSize;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            int baseColor = getResources().getColor(R.color.primary);
            float alpha = 1 - (float) Math.max(0, mParallaxImageHeight - scrollY) / mParallaxImageHeight;
            mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        }
        ViewHelper.setTranslationY(mImageView, scrollY / 2);

        // Translate FAB
        int maxFabTranslationY = mParallaxImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mParallaxImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mWidth - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFab, mWidth - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mParallaxShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }
}
