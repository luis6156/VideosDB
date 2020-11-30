package video;

import java.util.ArrayList;

public final class Movie extends Video {
    private final ArrayList<Double> ratings = new ArrayList<>();

    private final int duration;

    /**
     * @param title    movie's title
     * @param actors   movie's list of actors
     * @param genres   movie's list of genres
     * @param year     movie's year
     * @param duration movie's duration
     */
    public Movie(final String title, final ArrayList<String> actors,
                 final ArrayList<String> genres, final int year, final int duration) {
        super(title, year, genres, actors);
        this.duration = duration;
        totalDuration = duration;
    }

    /**
     * Adds all ratings and divides them by the number of ratings to get the total rating
     */
    protected void setTotalRating() {
        double totalRating = 0;
        for (Double rating : ratings) {
            totalRating += rating;
        }

        this.totalRating = totalRating / ratings.size();
    }

    /**
     * Adds rating to movie and recalculates total rating
     *
     * @param rating rating to be added
     */
    public void addRating(final double rating) {
        ratings.add(rating);
        setTotalRating();
    }

    // Returns movie duration
    public int getDuration() {
        return duration;
    }
}
