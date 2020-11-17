package actor;

import database.MovieDB;
import database.ShowDB;

import java.util.ArrayList;
import java.util.Map;

public class Actor {
    private final String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;
    private double actor_rating = 0;

    public Actor(MovieDB movieDB, ShowDB showDB, final String name,
                 final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
        setActorRating(movieDB, showDB);
    }

    public void setActorRating(MovieDB movieDB, ShowDB showDB) {
        String title;
        int count = 0;
        double tmp_rating = 0, check_rating;

        for (String s : filmography) {
            title = s;
            if (movieDB.isMovie(title)) {
                check_rating = movieDB.getMovieRating(title);
                if (check_rating != 0) {
                    tmp_rating += check_rating;
                    ++count;
                }
            } else if (showDB.isShow(title)) {
                check_rating = showDB.getShowRating(title);
                if (check_rating != 0) {
                    tmp_rating += check_rating;
                    ++count;
                }
            }
        }

        actor_rating = tmp_rating / count;
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
