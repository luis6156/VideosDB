package video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Movie extends Video {
    private final int duration;

    public Movie(final String title, final ArrayList<String> actors,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, genres, actors);
        this.duration = duration;
        setTotalDuration(duration);
    }
}
