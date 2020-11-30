package video;

import java.util.ArrayList;
import java.util.List;

public class Season {
    private final int currentSeason;
    private final int duration;
    private final List<Double> ratings = new ArrayList<>();
    private double season_rating;

    /**
     * @param currentSeason season number
     * @param duration      duration of season
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
        double tmp_rating = 0;

        ratings.add(rating);
        for (Double aDouble : ratings) {
            tmp_rating += aDouble;
        }

        season_rating = tmp_rating / ratings.size();
    }

    // Returns season's rating
    public double getSeasonRating() {
        return season_rating;
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
