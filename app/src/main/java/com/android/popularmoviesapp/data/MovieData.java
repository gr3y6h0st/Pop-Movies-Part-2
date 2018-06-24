package com.android.popularmoviesapp.data;

import java.io.Serializable;


// implement Serializable for use of data between Main --> MovieDetail activities
public class MovieData implements Serializable{

    private String original_title;
    private String poster_path;
    private String backdrop_path;
    private String overview;
    private String vote_average;
    private String release_date;
    private String movie_id;

    private String trailer_name;
    private String trailer_key;
    private String trailer_site;
    private String trailer_id;
    private String trailer_type;
    private String trailer_count;

    private String review_url;
    private String review_author;
    private String review_content;

    private boolean favorite_movie = false;

    /**
     * No args constructor, use in serialization
     */
    public MovieData() {
    }

    public MovieData(String movieID, String originalTitle, String posterPath, String backdropPath,
                     String voteAverage, String releaseDate, String overview) {

        this.movie_id = movieID;
        this.original_title = originalTitle;
        this.poster_path = posterPath;
        this.backdrop_path = backdropPath;
        this.vote_average = voteAverage;
        this.release_date = releaseDate;
        this.overview = overview;
    }

    public MovieData(String trailerName, String trailerId, String trailerKey, String trailerSite,
                     String trailerType, String trailerCount) {

        this.trailer_id = trailerId;
        this.trailer_name = trailerName;
        this.poster_path = trailerKey;
        this.backdrop_path = trailerSite;
        this.vote_average = trailerType;
        this.trailer_count = trailerCount;
    }

    public MovieData(String reviewAuthor, String reviewContent, String reviewUrl) {

        this.review_author = reviewAuthor;
        this.review_content = reviewContent;
        this.review_url = reviewUrl;
    }

    public MovieData (boolean check_favorite_movie){
        this.favorite_movie = check_favorite_movie;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public boolean checkFavorite_movie() {
        return favorite_movie;
    }

    public void setFavorite_movie(boolean new_fav_movie){
        favorite_movie = new_fav_movie;
    }
}
