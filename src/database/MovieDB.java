package database;

import comparator.VideoFavoriteCmp;
import comparator.VideoDurationCmp;
import comparator.VideoRatingCmp;
import comparator.VideoViewCmp;
import fileio.MovieInputData;
import video.Movie;

import java.util.*;

public class MovieDB extends MediaUtilsDB {
    private final HashMap<String, Movie> movieDB = new HashMap<>();
    private final TreeSet<Movie> favMovies =
            new TreeSet<>(new VideoFavoriteCmp());
    private final TreeSet<Movie> viewedMovies =
            new TreeSet<>(new VideoViewCmp());
    private final TreeSet<Movie> longestMovies =
            new TreeSet<>(new VideoDurationCmp());
    private final TreeSet<Movie> ratedMovies =
            new TreeSet<>(new VideoRatingCmp());

    public void populateMovieDB(final VideoDB videoDB,
                                final List<MovieInputData> movieDB) {
        for (MovieInputData movie : movieDB) {
            Movie newMovie = new Movie(
                    movie.getTitle(),
                    movie.getCast(),
                    movie.getGenres(),
                    movie.getYear(),
                    movie.getDuration()
            );
            this.movieDB.put(movie.getTitle(), newMovie);
            longestMovies.add(newMovie);
            videoDB.populateVideoDB(newMovie);
        }
    }

    public boolean isMovie(final String title) {
        return movieDB.containsKey(title);
    }

    public double getMovieRating(final String title) {
        return movieDB.get(title).getTotalRating();
    }

    public List<String> getMovieActors(final String title) {
        return Collections.unmodifiableList(movieDB.get(title).getActors());
    }

    public void addFavorites(final VideoDB videoDB, final String title) {
        Movie tmp = movieDB.get(title);
        favMovies.remove(tmp);
        videoDB.removeVideoFav(tmp);
        tmp.addFavorite();
        favMovies.add(tmp);
        videoDB.addVideoFav(tmp);
    }

    public void addViews(final VideoDB videoDB, final String title) {
        Movie tmp = movieDB.get(title);
        viewedMovies.remove(tmp);
        tmp.addViews();
        viewedMovies.add(tmp);
        videoDB.updateGenreViews(tmp);
    }

    public void addRating(final VideoDB videoDB, final String title,
                          final double rating) {
        Movie tmp;
        tmp = movieDB.get(title);
        ratedMovies.remove(tmp);
        videoDB.removeGenreVideoRatings(tmp);
        videoDB.removeVideoRatings(tmp);
        tmp.addRating(rating);
        ratedMovies.add(tmp);
        videoDB.addGenreVideoRatings(tmp);
        videoDB.addVideoRatings(tmp);
    }

    public String getTopK(final String query, final String orderType,
                          final String year,
                          final String genre, final int k) {
        Movie tmp;
        Iterator<Movie> iterator;
        List<String> list = new ArrayList<>();
        switch (query) {
            case "favorite":
                if (orderType.equals("desc")) {
                    iterator = favMovies.descendingIterator();
                } else {
                    iterator = favMovies.iterator();
                }
                break;
            case "ratings":
                if (orderType.equals("desc")) {
                    iterator = ratedMovies.descendingIterator();
                } else {
                    iterator = ratedMovies.iterator();
                }
                break;
            case "most_viewed":
                if (orderType.equals("desc")) {
                    iterator = viewedMovies.descendingIterator();
                } else {
                    iterator = viewedMovies.iterator();
                }
                break;
            case "longest":
                if (orderType.equals("desc")) {
                    iterator = longestMovies.descendingIterator();
                } else {
                    iterator = longestMovies.iterator();
                }
                break;
            default:
                return null;
        }

        while (iterator.hasNext()) {
            tmp = iterator.next();
            if (validFilters(tmp, year, genre)) {
                list.add(tmp.getTitle());
            }
            if (list.size() == k) {
                return list.toString();
            }
        }

        return list.toString();
    }
}
