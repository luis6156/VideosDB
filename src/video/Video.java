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
     * @param title  video's title
     * @param year   video's year
     * @param genres video's genre
     * @param actors video's list of actors
     */
    public Video(final String title, final int year, final ArrayList<String> genres,
                 final ArrayList<String> actors) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.actors = actors;
    }

    /**
     * Increment favorite counters.
     */
    public final void addFavorite() {
        ++favorites;
    }

    /**
     * Increment views counter.
     */
    public final void addViews() {
        ++views;
    }

    // Returns video favorite counter
    public final int getFavorites() {
        return favorites;
    }

    // Returns video views counter
    public final int getViews() {
        return views;
    }

    // Returns video title
    public final String getTitle() {
        return title;
    }

    // Returns video year
    public final int getYear() {
        return year;
    }

    /**
     * @return unmodifiable actors list
     */
    public final List<String> getActors() {
        return Collections.unmodifiableList(actors);
    }

    /**
     * @return unmodifiable genres list
     */
    public final List<String> getGenres() {
        return Collections.unmodifiableList(genres);
    }

    /**
     * Protected method to calculate total rating
     */
    protected abstract void setTotalRating();

    // Returns total duration of video
    public final int getTotalDuration() {
        return totalDuration;
    }

    // Returns total rating of video
    public final double getTotalRating() {
        return totalRating;
    }
}
