package database;

import comparator.FavoriteCmp;
import comparator.DurationCmp;
import comparator.RatingCmp;
import comparator.ViewCmp;
import fileio.MovieInputData;
import video.Movie;
import video.Video;

import java.util.*;

public class MovieDB {
    private final HashMap<String, Movie> movieDB = new HashMap<>();
    private final SortedSet<Movie> favMoviesDesc =
            new TreeSet<>(new FavoriteCmp(false));
    private final SortedSet<Movie> favMoviesAsc =
            new TreeSet<>(new FavoriteCmp(true));
    private final SortedSet<Movie> viewedMoviesAsc =
            new TreeSet<>(new ViewCmp(true));
    private final SortedSet<Movie> viewedMoviesDesc =
            new TreeSet<>(new ViewCmp(false));
    private final SortedSet<Movie> longestMoviesAsc =
            new TreeSet<>(new DurationCmp(true));
    private final SortedSet<Movie> longestMoviesDesc =
            new TreeSet<>(new DurationCmp(false));
    private final SortedSet<Movie> ratedMoviesAsc =
            new TreeSet<>(new RatingCmp(true));
    private final SortedSet<Movie> ratedMoviesDesc =
            new TreeSet<>(new RatingCmp(false));

    public void populateMovieDB(VideoDB videoDB, List<MovieInputData> movieDB) {
        for (MovieInputData movie : movieDB) {
            Movie newMovie = new Movie(
                    movie.getTitle(),
                    movie.getCast(),
                    movie.getGenres(),
                    movie.getYear(),
                    movie.getDuration()
            );
            this.movieDB.put(movie.getTitle(), newMovie);
            longestMoviesAsc.add(newMovie);
            longestMoviesDesc.add(newMovie);
            ratedMoviesDesc.add(newMovie);
            ratedMoviesAsc.add(newMovie);
            videoDB.populateVideoDB(newMovie);
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

    public void addFavorites(VideoDB videoDB, String title) {
        Movie tmp = movieDB.get(title);
        favMoviesDesc.remove(tmp);
        favMoviesAsc.remove(tmp);
        tmp.addFavorite();
        favMoviesDesc.add(tmp);
        favMoviesAsc.add(tmp);
        videoDB.updateVideoFav(tmp);
    }

    public void addViews(VideoDB videoDB, String title) {
        Movie tmp = movieDB.get(title);
        viewedMoviesAsc.remove(tmp);
        viewedMoviesDesc.remove(tmp);
        tmp.addViews();
        viewedMoviesAsc.add(tmp);
        viewedMoviesDesc.add(tmp);
        videoDB.updateGenreViews(tmp);
    }

    public void addRating(VideoDB videoDB, String title, double rating) {
        Movie tmp;
        tmp = movieDB.get(title);
        ratedMoviesAsc.remove(tmp);
        ratedMoviesDesc.remove(tmp);
        videoDB.removeGenreVideoRatings(tmp);
        tmp.addRating(rating);
        ratedMoviesAsc.add(tmp);
        ratedMoviesDesc.add(tmp);
        videoDB.updateGenreVideoRatings(tmp);
        videoDB.updateVideoRatings(tmp);
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

    public List<String> getTopK(String query, String orderType, String year,
                                String genre, int k) {
        List<String> list = new ArrayList<>();
        switch (query) {
            case "favorite":
                if (orderType.equals("desc")) {
                    for (Movie movie : favMoviesDesc) {
                        if (validFilters(movie, year, genre)) {
                            list.add(movie.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                } else {
                    for (Movie movie : favMoviesAsc) {
                        if (validFilters(movie, year, genre)) {
                            list.add(movie.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                }
                break;
            case "ratings":
                for (Movie movie : ratedMoviesAsc) {
                    if (validFilters(movie, year, genre) && movie.getTotalRating() != 0) {
                        list.add(movie.getTitle());
                    }
                    if (list.size() == k) {
                        break;
                    }
                }
                break;
            case "most_viewed":
                if (orderType.equals("desc")) {
                    for (Movie movie : viewedMoviesDesc) {
                        if (validFilters(movie, year, genre)) {
                            list.add(movie.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                } else {
                    for (Movie movie : viewedMoviesAsc) {
                        if (validFilters(movie, year, genre)) {
                            list.add(movie.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                }
                break;
            case "longest":
                if (orderType.equals("desc")) {
                    for (Movie movie : longestMoviesDesc) {
                        if (validFilters(movie, year, genre)) {
                            list.add(movie.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                } else {
                    for (Movie movie : longestMoviesAsc) {
                        if (validFilters(movie, year, genre)) {
                            list.add(movie.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                }
                break;
            default:
                break;
        }

        return list;
    }
}
