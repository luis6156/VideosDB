package video;

import java.util.ArrayList;
import java.util.List;

public final class Season {
    private final int currentSeason;
    private final int duration;
    private final List<Double> ratings = new ArrayList<>();
    private double seasonRating;

    /**
     * @param currentSeason season's number
     * @param duration      season's duration
     */
    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
    }

    /**
     * Adds rating to season and computes total rating of the season
     *
     * @param rating rating to be added
     */
    public void addRating(final double rating) {
        double tmpRating = 0;

        ratings.add(rating);
        for (Double currRating : ratings) {
            tmpRating += currRating;
        }

        seasonRating = tmpRating / ratings.size();
    }

    // Returns season's rating
    public double getSeasonRating() {
        return seasonRating;
    }

    // Returns season's duration
    public int getDuration() {
        return duration;
    }

    // Returns current season
    public int getCurrentSeason() {
        return currentSeason;
    }
}
