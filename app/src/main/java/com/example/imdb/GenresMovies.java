package com.example.imdb;

// Genres Movies class to set and get the desired details
public class GenresMovies {

    private String genre;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "GenresMovies{" +
                "genre='" + genre + '\'' +
                '}';
    }
}
