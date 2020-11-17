package database;

import comparator.RatingCmp;
import fileio.MovieInputData;
import fileio.SerialInputData;
import video.Movie;
import video.Show;
import video.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VideoDB {
    private final List<String> unorderedVideos = new ArrayList<>();

    public void populateVideoDB(List<MovieInputData> movieDB,
                                List<SerialInputData> showDB) {
        for (MovieInputData movie : movieDB) {
            unorderedVideos.add(unorderedVideos.size(), movie.getTitle());
        }
        for (SerialInputData show : showDB) {
            unorderedVideos.add(unorderedVideos.size(), show.getTitle());
        }
    }

    public boolean validFilters(Video video, String year, String genre) {
        if (year != null && genre != null) {
            return video.getGenres().contains(genre) && video.getYear() == Integer.parseInt(year);
        } else if (year == null && genre != null) {
            return video.getGenres().contains(genre);
        } else if (year != null) {
            return video.getYear() == Integer.parseInt(year);
        } else {
            return true;
        }
    }

    public String getUnwatchedVideo(List<String> history) {
        List<String> differences =
                unorderedVideos.stream().filter(element -> !history.contains(element)).collect(Collectors.toList());

        if (differences.size() == 0) {
            return null;
        }

        return differences.get(0);
    }
}
