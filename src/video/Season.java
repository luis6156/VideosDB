package video;

import java.util.ArrayList;
import java.util.List;

public class Season {
    private final int currentSeason;
    private int duration;
    private List<Double> ratings = new ArrayList<>();;
    private double season_rating;

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
    }

    public void addRating(double rating) {
        double tmp_rating = 0;

        ratings.add(rating);
        for (Double aDouble : ratings) {
            tmp_rating += aDouble;
        }

        season_rating = tmp_rating / ratings.size();
    }

    public double getSeasonRating() {
        return season_rating;
    }
}
