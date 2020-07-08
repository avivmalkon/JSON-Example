package com.example.msappstest;

import androidx.annotation.NonNull;

public class Movie
{
    private String title;
    private String imageURL;
    private double rating;
    private int releaseYear;
    private String genre;


    public Movie(String title, String imageURL, double rating, int releaseYear, String genre)
    {
        this.title = title;
        this.imageURL = imageURL;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    public Movie() {
    }


    @NonNull
    @Override
    public String toString()
    {
        return "Title: " + title + "\n" + "Rating: " + rating + "\n" + "Release Year: " + releaseYear + "\n";
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public double getRating() {
        return rating;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}