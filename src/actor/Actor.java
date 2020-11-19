package actor;

import database.MovieDB;
import database.ShowDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Actor {
    private final String name;
    private final String careerDescription;
    private final Map<String, Double> filmography = new HashMap<>();
    private final Map<ActorsAwards, Integer> awards;
    private double actor_rating = 0;

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

    public void addActorRating(String title, double rating) {
        int ratedMovies = 0;

        double old_rating = filmography.get(title);
        filmography.replace(title, rating);
        double totalRating = 0;
//        actor_rating = actor_rating * filmography.size() - old_rating + rating;
//        actor_rating /= filmography.size();
        for (Double videoRating : filmography.values()) {
            if (videoRating != 0) {
                totalRating += videoRating;
                ++ratedMovies;
            }
        }
        actor_rating = totalRating / ratedMovies;
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public double getActorRating() {
        return actor_rating;
    }
}
