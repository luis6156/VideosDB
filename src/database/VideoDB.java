package database;

import comparator.RatingCmp;
import comparator.ViewCmp;
import fileio.MovieInputData;
import fileio.SerialInputData;
import video.GenreViews;
import video.Movie;
import video.Show;
import video.Video;

import java.util.*;
import java.util.stream.Collectors;

public class VideoDB {
    private final List<String> unorderedVideos = new ArrayList<>();
    private final Map<String, Integer> videoByIndex = new HashMap<>();
    private final Map<String, GenreViews> genreViews = new HashMap<>();
    private final SortedSet<GenreViews> mostViewedGenres = new TreeSet<>();
    private int count = 0;

    public void populateVideoDB(List<MovieInputData> movieDB,
                                List<SerialInputData> showDB) {
        for (MovieInputData movie : movieDB) {
            unorderedVideos.add(unorderedVideos.size(), movie.getTitle());
            videoByIndex.put(movie.getTitle(), count++);
            Movie newMovie = new Movie(
                    movie.getTitle(),
                    movie.getCast(),
                    movie.getGenres(),
                    movie.getYear(),
                    movie.getDuration()
            );
            populateGenreViews(newMovie);
        }
        for (SerialInputData show : showDB) {
            unorderedVideos.add(unorderedVideos.size(), show.getTitle());
            videoByIndex.put(show.getTitle(), count++);
            Show newShow = new Show(
                    show.getTitle(),
                    show.getYear(),
                    show.getGenres(),
                    show.getCast(),
                    show.getNumberSeason(),
                    show.getSeasons()
            );
            populateGenreViews(newShow);
        }
    }

    public void populateGenreViews(Video video) {
        String genreName;

        for (int i = 0; i < video.getGenres().size(); ++i) {
            genreName = video.getGenres().get(i);
            GenreViews genre = genreViews.get(genreName);
            if (genre != null) {
                genre.addVideos(video.getTitle());
            } else {
                genre = new GenreViews(video.getTitle());
                genreViews.put(genreName, genre);
                mostViewedGenres.add(genre);
            }
        }
    }

    public void updateGenreViews(Video video) {
        String genreName;

        for (int i = 0; i < video.getGenres().size(); ++i) {
            genreName = video.getGenres().get(i);
            GenreViews genre = genreViews.get(genreName);
            mostViewedGenres.remove(genre);
            genre.addViews();
            mostViewedGenres.add(genre);
        }
    }

    public String getUnwatchedVideo(Map<String, Integer> history) {
        boolean found = false;

        for (String unorderedVideo : unorderedVideos) {
            if (!history.containsKey(unorderedVideo)) {
                return unorderedVideo;
            }
        }

        return null;
    }

    public String getBestVideo(MovieDB movieDB,
                               ShowDB showDB, Map<String, Integer> history) {
        List<Movie> movies = movieDB.getTopRatedMovies();
        List<Show> shows = showDB.getTopRatedShows();
        int first = 0, second = 0;
        int movies_size = movies.size(), shows_size = shows.size();
        Movie movie;
        Show show;

        while(first < movies_size || second < shows_size) {
            if (first < movies_size && second < shows_size) {
                movie = movies.get(first);
                show = shows.get(second);
                if (movie.getTotalRating() > show.getTotalRating()) {
                    ++first;
                    if (!history.containsKey(movie.getTitle())) {
                        return movie.getTitle();
                    }
                } else if (movie.getTotalRating() < show.getTotalRating()) {
                    ++second;
                    if (!history.containsKey(show.getTitle())) {
                        return show.getTitle();
                    }
                } else {
                    if (videoByIndex.get(movie.getTitle()) < videoByIndex.get(show.getTitle())) {
                        ++first;
                        if (!history.containsKey(movie.getTitle())) {
                            return movie.getTitle();
                        }
                    } else {
                        ++second;
                        if (!history.containsKey(show.getTitle())) {
                            return show.getTitle();
                        }
                    }
                }
            } else if (first < movies_size) {
                movie = movies.get(first);
                ++first;
                if (!history.containsKey(movie.getTitle())) {
                    return movie.getTitle();
                }
            } else {
                show = shows.get(second);
                ++second;
                if (!history.containsKey(show.getTitle())) {
                    return show.getTitle();
                }
            }
        }

        return null;
    }

    public String getPopularVideo(Map<String, Integer> history) {
        String message = "PopularRecommendation result: ";
        String success;

        for (GenreViews genre : mostViewedGenres) {
            success = genre.getUnwatchedVideo(history);
            if (success != null) {
                return message + success;
            }
        }

        return message;
    }

    public String getFavoriteVideo(MovieDB movieDB,
                                   ShowDB showDB, Map<String, Integer> history) {
        String message = "FavoriteRecommendation result: ";
        List<Movie> movies = movieDB.getTopFavMovies();
        List<Show> shows = showDB.getTopRatedShows();
        int first = 0, second = 0;
        int movies_size = movies.size(), shows_size = shows.size();
        Movie movie;
        Show show;

        while(first < movies_size || second < shows_size) {
            if (first < movies_size && second < shows_size) {
                movie = movies.get(first);
                show = shows.get(second);
                if (movie.getFavorites() > show.getFavorites()) {
                    ++first;
                    if (!history.containsKey(movie.getTitle())) {
                        return message + movie.getTitle();
                    }
                } else if (movie.getFavorites() < show.getFavorites()) {
                    ++second;
                    if (!history.containsKey(show.getTitle())) {
                        return message + show.getTitle();
                    }
                } else {
                    if (videoByIndex.get(movie.getTitle()) < videoByIndex.get(show.getTitle())) {
                        ++first;
                        if (!history.containsKey(movie.getTitle())) {
                            return message + movie.getTitle();
                        }
                    } else {
                        ++second;
                        if (!history.containsKey(show.getTitle())) {
                            return message + show.getTitle();
                        }
                    }
                }
            } else if (first < movies_size) {
                movie = movies.get(first);
                ++first;
                if (!history.containsKey(movie.getTitle())) {
                    return message + movie.getTitle();
                }
            } else {
                show = shows.get(second);
                ++second;
                if (!history.containsKey(show.getTitle())) {
                    return message + show.getTitle();
                }
            }
        }

        return null;
   }
}
