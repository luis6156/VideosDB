package video;

import entertainment.Season;

import java.util.ArrayList;

public class Show extends Video {
    private final int numberOfSeasons;

    private final ArrayList<video.Season> seasons = new ArrayList<>();

    public Show(final String title, final int year,
                            final ArrayList<String> genres,
                            final ArrayList<String> cast,
                            final int numberOfSeasons, final ArrayList<Season> seasons) {
        super(title, year, genres, cast);
        this.numberOfSeasons = numberOfSeasons;
        for (int i = 0; i < seasons.size(); ++i) {
            this.seasons.add(new video.Season(i + 1,
                    seasons.get(i).getDuration()));
        }
        this.total_duration = getTotalDuration(seasons);
    }

    private int getTotalDuration(ArrayList<Season> seasons) {
        Season tmp = seasons.get(0);
        int duration = 0;
        for (Season season : seasons) {
            duration += season.getDuration();
        }
        return duration;
    }

    public void setTotalRating() {
        double tmp_rating = 0;
        for (video.Season season : seasons) {
            tmp_rating += season.getSeasonRating();
        }

        total_rating = tmp_rating / numberOfSeasons;
    }

    public void addRating(int season, double rating) {
        seasons.get(season - 1).addRating(rating);
        setTotalRating();
    }
}
