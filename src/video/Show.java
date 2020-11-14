package video;

import entertainment.Season;

import java.util.ArrayList;

public class Show extends Video {
    private final int numberOfSeasons;

    private final ArrayList<Season> seasons;

    public Show(final String title, final int year,
                            final ArrayList<String> genres,
                            final ArrayList<String> cast,
                            final int numberOfSeasons, final ArrayList<Season> seasons) {
        super(title, year, genres, cast);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }
}
