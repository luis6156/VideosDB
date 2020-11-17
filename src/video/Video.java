package video;

import java.util.ArrayList;

public abstract class Video {
    protected final String title;

    protected final int year;

    protected final ArrayList<String> genres;

    protected final ArrayList<String> actors;

    protected int favorites = 0;

    protected int views = 0;

    protected int total_duration = 0;

    protected double total_rating = 0;

    public Video(final String title, final int year,
                 final ArrayList<String> genres,
                 final ArrayList<String> actors) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.actors = actors;
    }

    public void addFavorite() {
        ++favorites;
    }

    public void addViews() {
        ++views;
    }

    public int getFavorites() {
        return favorites;
    }

    public int getViews() {
        return views;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public int getTotalDuration() {
        return total_duration;
    }

    public double getTotalRating() {
        return total_rating;
    }
}
