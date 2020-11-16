package video;

import java.util.ArrayList;

public class Movie extends Video {
    private ArrayList<Double> ratings = new ArrayList<>();

    private final int duration;

    public Movie(final String title, final ArrayList<String> actors,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, genres, actors);
        this.duration = duration;
        this.total_duration = this.duration;
    }

    public void setTotalRating() {
        double total_rating = 0;
        for (Double rating : ratings) {
            total_rating += rating;
        }

        this.total_rating = total_rating / ratings.size();
    }

    public void addRating(double rating) {
        ratings.add(rating);
        setTotalRating();
    }
}
