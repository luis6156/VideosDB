package video;

import entertainment.Season;

import java.util.ArrayList;

public class Show extends Video {
    private final int numberOfSeasons;
    private final ArrayList<video.Season> seasons = new ArrayList<>();

    /**
     * @param title           show's title
     * @param year            show's year
     * @param genres          show's genres
     * @param cast            show's list of actors
     * @param numberOfSeasons show's number of seasons
     * @param seasons         show's list of season objects
     */
    public Show(final String title, final int year,
                final ArrayList<String> genres,
                final ArrayList<String> cast,
                final int numberOfSeasons, final ArrayList<Season> seasons) {
        super(title, year, genres, cast);
        this.numberOfSeasons = numberOfSeasons;
        // Translate entertainment's season object to video's season object
        for (int i = 0; i < seasons.size(); ++i) {
            this.seasons.add(new video.Season(i + 1,
                    seasons.get(i).getDuration()));
        }
        totalDuration = setTotalDuration(seasons);
    }

    /**
     * @param seasons list of seasons to compute total duration
     * @return total duration of show
     */
    private int setTotalDuration(final ArrayList<Season> seasons) {
        int duration = 0;
        for (Season season : seasons) {
            duration += season.getDuration();
        }

        return duration;
    }

    /**
     * Adds all ratings and divides them by the number of seasons to get the total rating
     */
    protected void setTotalRating() {
        double tmpRating = 0;
        for (video.Season season : seasons) {
            tmpRating += season.getSeasonRating();
        }

        totalRating = tmpRating / numberOfSeasons;
    }

    /**
     * Adds rating to season and recalculates total rating
     *
     * @param season season to be rated
     * @param rating rating of the season
     */
    public void addRating(final int season, final double rating) {
        seasons.get(season - 1).addRating(rating);
        setTotalRating();
    }
}
