package com.example.imdb;

// Movies By Genre class to set and get the desired details
public class MoviesByGenre {

    private String title;
    private String image;
    private String rating;
    private String release;
    private String imdb_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    @Override
    public String toString() {
        return "MoviesByGenre{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", rating='" + rating + '\'' +
                ", release='" + release + '\'' +
                ", imdb_id='" + imdb_id + '\'' +
                '}';
    }
}
