package com.example.imdb;

// Upcoming Movie class to set and get the desired details
public class UpcomingMovie {

    private String title;
    private String image;
    private String release;
    private String imdb_id;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "UpcomingMovie{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", release='" + release + '\'' +
                ", imdb_id='" + imdb_id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
