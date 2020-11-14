package video;

import java.util.ArrayList;

public abstract class Video {
    protected final String title;

    protected final int year;

    protected final ArrayList<String> genres;

    protected final ArrayList<String> actors;

    public Video(final String title, final int year,
                 final ArrayList<String> genres, final ArrayList<String> actors) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.actors = actors;
    }

    public boolean respectsFilters(String genre, String year) {
        if (year != null && genre != null) {
            return this.genres.contains(genre) && this.year == Integer.parseInt(year);
        } else if (year == null && genre != null) {
            return this.genres.contains(genre);
        } else if (year != null) {
            return this.year == Integer.parseInt(year);
        } else {
            return true;
        }
    }
}
