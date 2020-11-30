package actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Actor {
    private final String name;
    private final String careerDescription;
    private final Map<String, Double> filmography = new HashMap<>();
    private final Map<ActorsAwards, Integer> awards;
    private int totalAwards = 0;
    private double actorRating = 0;

    /**
     * @param name              actor's name
     * @param careerDescription actor's description
     * @param filmography       actor's filmography
     * @param awards            actor's awards
     */
    public Actor(final String name, final String careerDescription,
                 final ArrayList<String> filmography, final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.awards = awards;
        // Calculate total numbers of awards from all categories
        for (Integer awardWon : awards.values()) {
            totalAwards += awardWon;
        }
        // Remember filmography of actor as well as its rating (initialized with 0)
        for (String title : filmography) {
            this.filmography.put(title, 0.0);
        }
    }

    /**
     * Replaces current total rating of video with the new rating and recalculates actor's
     * rating, not including unrated videos.
     *
     * @param title  title of the video to be rated
     * @param rating the value of the rating
     */
    public void addActorRating(final String title, final double rating) {
        int ratedMovies = 0;

        filmography.replace(title, rating);
        double totalRating = 0;

        // Computes average rating
        for (Double videoRating : filmography.values()) {
            if (videoRating != 0) {
                totalRating += videoRating;
                ++ratedMovies;
            }
        }
        actorRating = totalRating / ratedMovies;
    }

    // Return number of total awards
    public int getTotalAwards() {
        return totalAwards;
    }

    // Returns actor's name
    public String getName() {
        return name;
    }

    // Returns actor's career description
    public String getCareerDescription() {
        return careerDescription;
    }

    // Returns unmodifiable map of actor's awards
    public Map<ActorsAwards, Integer> getAwards() {
        return Collections.unmodifiableMap(awards);
    }

    // Returns actor's total rating
    public double getActorRating() {
        return actorRating;
    }
}
