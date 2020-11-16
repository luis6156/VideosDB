package video;

import comparator.FavoriteCmp;
import comparator.DurationCmp;
import comparator.ViewCmp;
import fileio.MovieInputData;

import java.util.*;

public class MovieDB extends VideoDB {
    private final HashMap<String, Movie> movies = new HashMap<>();
    private final SortedSet<Movie> favMovies = new TreeSet<>(new FavoriteCmp());
    private final SortedSet<Movie> viewedMovies = new TreeSet<>(new ViewCmp());
    private final SortedSet<Movie> longestMovies =
            new TreeSet<>(new DurationCmp());

    public void populateMovieDB(List<MovieInputData> movies) {
        for (MovieInputData movie : movies) {
            Movie newMovie = new Movie(
                    movie.getTitle(),
                    movie.getCast(),
                    movie.getGenres(),
                    movie.getYear(),
                    movie.getDuration()
            );
            this.movies.put(movie.getTitle(), newMovie);
            longestMovies.add(newMovie);
        }
    }

    public boolean isMovie(String title) {
        return movies.containsKey(title);
    }

    public void addFavorites(String title) {
        Movie tmp = movies.get(title);
        favMovies.remove(tmp);
        tmp.addFavorite();
        favMovies.add(tmp);
    }

    public void addViews(String title) {
        Movie tmp = movies.get(title);
        viewedMovies.remove(tmp);
        tmp.addViews();
        viewedMovies.add(tmp);
    }

    public String getTopK(String query, String year,
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
            case "rating":
                return list.toString();
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
        }

        return list.toString();
    }
}
