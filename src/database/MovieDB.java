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
    private final HashMap<String, SortedSet<Video>> genreRatingDB =
            new HashMap<>();
    private final SortedSet<Movie> favMovies = new TreeSet<>(new FavoriteCmp());
    private final SortedSet<Movie> viewedMoviesAsc =
            new TreeSet<>(new ViewCmp(true));
    private final SortedSet<Movie> viewedMoviesDesc =
            new TreeSet<>(new ViewCmp(false));
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
            ratedMovies.add(newMovie);
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
        ratedMovies.remove(tmp);
        tmp.addRating(rating);
        ratedMovies.add(tmp);
        videoDB.updateGenreVideoRatings(tmp);
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

    public List<Movie> getTopFavMovies() {
        return new ArrayList<>(favMovies);
    }
}
