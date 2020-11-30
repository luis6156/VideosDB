package video;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Video {
    protected final String title;
    protected final int year;
    protected final ArrayList<String> genres;
    protected final ArrayList<String> actors;
    protected int favorites = 0;
    protected int views = 0;
    protected int totalDuration = 0;
    protected double totalRating = 0;

    /**
     * @param title  video title
     * @param year   video year
     * @param genres video genre
     * @param actors video list of actors
     */
    public Video(final String title, final int year,
                 final ArrayList<String> genres,
                 final ArrayList<String> actors) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.actors = actors;
    }

    // Increments video's favorite counter
    public void addFavorite() {
        ++favorites;
    }

    // Increments video's views counter
    public void addViews() {
        ++views;
    }

    // Returns video favorite counter
    public int getFavorites() {
        return favorites;
    }

    // Returns video views counter
    public int getViews() {
        return views;
    }

    // Returns video title
    public String getTitle() {
        return title;
    }

    // Returns video year
    public int getYear() {
        return year;
    }

    /**
     * @return unmodifiable actors list
     */
    public List<String> getActors() {
        return Collections.unmodifiableList(actors);
    }

    /**
     * @return unmodifiable genres list
     */
    public List<String> getGenres() {
        return Collections.unmodifiableList(genres);
    }

    /**
     * Protected method to calculate total rating
     */
    protected abstract void setTotalRating();

    // Returns total duration of video
    public int getTotalDuration() {
        return totalDuration;
    }

    // Returns total rating of video
    public double getTotalRating() {
        return totalRating;
    }
}
