package database;

import comparator.FavoriteCmp;
import comparator.DurationCmp;
import comparator.RatingCmp;
import comparator.ViewCmp;
import fileio.MovieInputData;
import video.Movie;
import video.Video;

import java.util.*;

public class MovieDB extends VideoDB {
    private final HashMap<String, Movie> movieDB = new HashMap<>();
    private final SortedSet<Movie> favMovies = new TreeSet<>(new FavoriteCmp());
    private final SortedSet<Movie> viewedMovies = new TreeSet<>(new ViewCmp());
    private final SortedSet<Movie> longestMovies =
            new TreeSet<>(new DurationCmp());
    private final SortedSet<Movie> ratedMovies = new TreeSet<>(new RatingCmp());

    public void populateMovieDB(List<MovieInputData> movieDB) {
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
        }
    }

    public boolean isMovie(String title) {
        return movieDB.containsKey(title);
    }

    public double getMovieRating(String title) {
        return movieDB.get(title).getTotalRating();
    }

    public List<String> getMovieActors(String title) {
        return movieDB.get(title).getActors();
    }

    public void addFavorites(String title) {
        Movie tmp = movieDB.get(title);
        favMovies.remove(tmp);
        tmp.addFavorite();
        favMovies.add(tmp);
    }

    public void addViews(String title) {
        Movie tmp = movieDB.get(title);
        viewedMovies.remove(tmp);
        tmp.addViews();
        viewedMovies.add(tmp);
    }

    public void addRating(String title, double rating) {
        Movie tmp = movieDB.get(title);
        ratedMovies.remove(tmp);
        tmp.addRating(rating);
        ratedMovies.add(tmp);
    }

    public List<String> getTopK(String query, String year,
                                String genre, int k) {
        List<String> list = new ArrayList<>();
        switch (query) {
            case "favorite":
                for (Movie movie : favMovies) {
                    if (validFilters(movie, year, genre)) {
                        list.add(movie.getTitle());
                    }
                    if (list.size() == k) {
                        break;
                    }
                }
                break;
            case "ratings":
                for (Movie movie : ratedMovies) {
                    if (validFilters(movie, year, genre)) {
                        list.add(movie.getTitle());
                    }
                    if (list.size() == k) {
                        break;
                    }
                }
            case "most_viewed":
                for (Movie movie : viewedMovies) {
                    if (validFilters(movie, year, genre)) {
                        list.add(movie.getTitle());
                    }
                    if (list.size() == k) {
                        break;
                    }
                }
                break;
            case "longest":
                for (Movie movie : longestMovies) {
                    if (validFilters(movie, year, genre)) {
                        list.add(movie.getTitle());
                    }
                    if (list.size() == k) {
                        break;
                    }
                }
                break;
            default:
                break;
        }

        return list;
    }

    public List<Movie> getTopRatedMovies() {
        return new ArrayList<>(ratedMovies);
    }
}
