package video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Movie extends Video {
    private final Map<String, Double> rating = new HashMap<String, Double>();
    private final int duration;

    public Movie(final String title, final ArrayList<String> actors,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, genres, actors);
        this.duration = duration;
    }

    public void addRating(String username, double rating) {
        this.rating.put(username, rating);
    }
}
