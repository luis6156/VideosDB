package actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Actor {
    private final String name;
    private final String careerDescription;
    private final Map<String, Double> filmography = new HashMap<>();
    private final Map<ActorsAwards, Integer> awards;
    private double actorRating = 0;

    public Actor(final String name,
                 final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.awards = awards;
        for (String title : filmography) {
            this.filmography.put(title, 0.0);
        }
    }

    public void addActorRating(final String title, final double rating) {
        int ratedMovies = 0;

        filmography.replace(title, rating);
        double totalRating = 0;
        for (Double videoRating : filmography.values()) {
            if (videoRating != 0) {
                totalRating += videoRating;
                ++ratedMovies;
            }
        }
        actorRating = totalRating / ratedMovies;
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return Collections.unmodifiableMap(awards);
    }

    public double getActorRating() {
        return actorRating;
    }
}
