package database;

import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.ShowInput;
import video.Genre;
import video.Movie;
import video.Show;
import video.Video;

import java.util.*;
import java.util.stream.Collectors;

public class VideoDB {
    private final List<String> unorderedVideos = new ArrayList<>();
    private final HashMap<String, Video> orderedVideos = new HashMap<>();
    protected final Map<String, Genre> videoByGenre = new HashMap<>();
    protected final SortedSet<Genre> bestGenres = new TreeSet<>();

    public void populateVideoDB(List<MovieInputData> movieDB,
                                List<SerialInputData> showDB) {

        for (MovieInputData movie : movieDB) {
            unorderedVideos.add(unorderedVideos.size(), movie.getTitle());
            Movie newMovie = new Movie(
                    movie.getTitle(),
                    movie.getCast(),
                    movie.getGenres(),
                    movie.getYear(),
                    movie.getDuration()
            );
            orderedVideos.put(movie.getTitle(), newMovie);
            populateGenre(newMovie);
        }
        for (SerialInputData show : showDB) {
            unorderedVideos.add(unorderedVideos.size(), show.getTitle());
            Show newShow = new Show(
                    show.getTitle(),
                    show.getYear(),
                    show.getGenres(),
                    show.getCast(),
                    show.getNumberSeason(),
                    show.getSeasons()
            );
            orderedVideos.put(show.getTitle(), newShow);
            populateGenre(newShow);
        }
    }

    public void populateGenre(Video video) {
        String title, genreName;

        title = video.getTitle();
        for (int i = 0; i < video.getGenres().size(); ++i) {
            genreName = video.getGenres().get(i);
            Genre genre =
                    videoByGenre.get(genreName);
            if (genre != null) {
                bestGenres.remove(genre);
                genre.addVideos(title);
            } else {
                genre = new Genre(genreName);
                genre.addVideos(title);
                videoByGenre.put(genreName, genre);
            }
            bestGenres.add(genre);
        }
    }

    public void addGenreViews(String title) {
        Video tmp_video;
        Genre tmp;

        tmp_video = orderedVideos.get(title);

        List<String> videoGenres = tmp_video.getGenres();
        for (String videoGenre : videoGenres) {
            tmp = videoByGenre.get(videoGenre);
            bestGenres.remove(tmp);
            tmp.addViews();
            bestGenres.add(tmp);
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

        if (differences.isEmpty()) {
            return null;
        }

        return differences.get(0);
    }

    public String getPopularVideo(List<String> history) {
        for (Genre genre : bestGenres) {
            String video = genre.getUnwatchedVideo(history);
            if (video != null) {
                return "PopularRecommendation result: " + video;
            }
        }
        return "PopularRecommendation result: ";
    }
}
