package com.android.popularmoviesapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.LinearLayout;

import com.android.popularmoviesapp.databinding.ActivityMovieDetailBinding;

import com.android.popularmoviesapp.data.MovieContract;
import com.android.popularmoviesapp.sync.MovieTrailerIntentService;
import com.android.popularmoviesapp.utilities.MovieDatabaseJsonUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    final String TAG = MovieDetailActivity.class.getSimpleName();

    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TRAILER_KEY,
            MovieContract.MovieEntry.COLUMN_TRAILER_TYPE,
            MovieContract.MovieEntry.COLUMN_TRAILER_NAME
    };

    public static final int INDEX_MOVIE_TITLE = 0;
    public static final int INDEX_MOVIE_VOTE_AVERAGE = 1;
    public static final int INDEX_MOVIE_POSTER_PATH = 2;
    public static final int INDEX_MOVIE_RELEASE_DATE = 3;
    public static final int INDEX_MOVIE_OVERVIEW = 4;
    public static final int INDEX_MOVIE_ID = 5;
    public static final int INDEX_TRAILER_KEY = 6;
    public static final int INDEX_TRAILER_TYPE = 7;
    public static final int INDEX_TRAILER_NAME = 8;



    private static final int ID_MOVIE_DETAIL_LOADER = 519;

    private Uri mUri;

    private ActivityMovieDetailBinding mDetailBinding;

    private RecyclerView mMovieTrailerList;
    private MovieDetailAdapter mTrailerAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);

        mDetailBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_movie_detail);

        int numberOfColumns = 2;

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(this);

        mMovieTrailerList = mDetailBinding.mdTrailersRv;

        mMovieTrailerList.setLayoutManager(layoutManager);

        mTrailerAdapter = new MovieDetailAdapter(5);

        mMovieTrailerList.setAdapter(mTrailerAdapter);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI cannot be null");
        Intent syncMovieTrailer = new Intent(this, MovieTrailerIntentService.class);
        startService(syncMovieTrailer);

        getSupportLoaderManager().initLoader(ID_MOVIE_DETAIL_LOADER, null, this);


    }

    //will set BACK BUTTON to navigate back to previous activity w/o recreating it.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {

        switch (loaderId) {

            case ID_MOVIE_DETAIL_LOADER:
                Log.v(TAG, mUri.toString());


                return new CursorLoader(this,
                        mUri,
                        MOVIE_DETAIL_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: "  + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mTrailerAdapter.swapCursor(data);
        if(mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mDetailBinding.mdTrailersRv.smoothScrollToPosition(mPosition);

        //data.moveToFirst();
        Log.v(TAG, DatabaseUtils.dumpCursorToString(data));

        boolean cursorHadValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHadValidData = true;
        }

        if (!cursorHadValidData) {
            return;

        }

        String movieTitle = data.getString(INDEX_MOVIE_TITLE);

        String moviePoster = data.getString(INDEX_MOVIE_POSTER_PATH);

        String movieOverview = data.getString(INDEX_MOVIE_OVERVIEW);

        String movieReleaseDate = data.getString(INDEX_MOVIE_RELEASE_DATE);

        String movieRating = data.getString(INDEX_MOVIE_VOTE_AVERAGE);

        String movie_ID = data.getString(INDEX_MOVIE_ID);

        String trailer_type = data.getString(INDEX_TRAILER_TYPE);
        //Log.v( TAG, movie_ID);

        String movieTrailerName = data.getString(INDEX_TRAILER_NAME);
        final String trailer_KEY = data.getString(INDEX_TRAILER_KEY);



        //create the image url for poster
        final String IMAGE_BASE = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w500";
        //obtain poster path from MovieData object
        String IMAGE_URL = moviePoster;

        String url = IMAGE_BASE + IMAGE_SIZE + IMAGE_URL;
        //output the url into Log for debug purposes
        //Log.d(TAG, trailer_KEY);
        //use Picasso to place url image into imageview
        Picasso.get().load(url).into(mDetailBinding.movieDetailIv);

        mDetailBinding.mdOriginalTitle.setText(movieTitle);
        mDetailBinding.mdVoteAvg.setText(movieRating);
        mDetailBinding.mdReleaseDate.setText(movieReleaseDate);
        mDetailBinding.mdPlotSynopsis.setText(movieOverview);

        mDetailBinding.mdOriginalTitleLabel.setText(getString(R.string.md_original_title_label));
        mDetailBinding.mdPlotSynopsisLabel.setText(getString(R.string.md_synopsis_label));
        mDetailBinding.mdReleaseDateLabel.setText(getString(R.string.md_date_label));
        mDetailBinding.mdVoteAvgLabel.setText(getString(R.string.md_rating_label));

        mDetailBinding.mdPlayTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openYTApp = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer_KEY));
                startActivity(openYTApp);
            }
        });

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mTrailerAdapter.swapCursor(null);
    }
}
