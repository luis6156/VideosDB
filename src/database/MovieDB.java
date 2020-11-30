package database;

import comparator.VideoFavoriteCmp;
import comparator.VideoDurationCmp;
import comparator.VideoRatingCmp;
import comparator.VideoViewCmp;
import fileio.MovieInputData;
import video.Movie;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;

public final class MovieDB extends MediaUtilsDB {
    private final HashMap<String, Movie> movieDB = new HashMap<>();
    private final TreeSet<Movie> favMovies = new TreeSet<>(new VideoFavoriteCmp());
    private final TreeSet<Movie> viewedMovies = new TreeSet<>(new VideoViewCmp());
    private final TreeSet<Movie> longestMovies = new TreeSet<>(new VideoDurationCmp());
    private final TreeSet<Movie> ratedMovies = new TreeSet<>(new VideoRatingCmp());

    /**
     * Constructor that assigns values to Movie class attributes and updates databases with its
     * data.
     *
     * @param videoDB populate VideoDB for recommended
     * @param movies  list of MovieInputData to be translated to Movie class
     */
    public void populateMovieDB(final VideoDB videoDB,
                                final List<MovieInputData> movies) {
        for (MovieInputData movie : movies) {
            Movie newMovie = new Movie(
                    movie.getTitle(),
                    movie.getCast(),
                    movie.getGenres(),
                    movie.getYear(),
                    movie.getDuration()
            );
            // Put Movie into MovieDB
            movieDB.put(movie.getTitle(), newMovie);
            // Add show to longestShow TreeSet (Longest Query)
            longestMovies.add(newMovie);
            // Add show to VideosDB (Recommended actions)
            videoDB.populateVideoDB(newMovie);
        }
    }

    /**
     * Function used in commands to determine if a video is a movie or not.
     *
     * @param title title of the video to be queried
     * @return true if video is a show, false otherwise
     */
    public boolean isMovie(final String title) {
        return movieDB.containsKey(title);
    }

    /**
     * Function used for rating comparator.
     *
     * @param title title of the video to be queried
     * @return the total rating of a movie
     */
    public double getMovieRating(final String title) {
        return movieDB.get(title).getTotalRating();
    }

    /**
     * Function used to get list of actors from movie in order to update their rating.
     *
     * @param title title of the video to be queried
     * @return unmodifiable list of actors from movie
     */
    public List<String> getMovieActors(final String title) {
        return Collections.unmodifiableList(movieDB.get(title).getActors());
    }

    /**
     * If user addFavorite operation was successful, add Movie to favorite databases
     * (Favorite Recommendation & Favorite Query). Remove the video from both TreeSets,
     * increment favorites counter and insert it back to resort.
     *
     * @param videoDB updates favorite TreeSet (Favorite Recommendation)
     * @param title   title of the video to be added to favorite
     */
    public void addFavorites(final VideoDB videoDB, final String title) {
        Movie tmp = movieDB.get(title);
        favMovies.remove(tmp);
        videoDB.removeVideoFav(tmp);
        tmp.addFavorite();
        favMovies.add(tmp);
        videoDB.addVideoFav(tmp);
    }

    /**
     * Add Movie to views databases (Popular Recommendation & Views Query). Remove the video from
     * both TreeSets, increment views counter and insert it back to resort.
     *
     * @param videoDB updates views TreeSet (Popular Recommendation)
     * @param title   title of the video to be incremented views counter
     */
    public void addViews(final VideoDB videoDB, final String title) {
        Movie tmp = movieDB.get(title);
        viewedMovies.remove(tmp);
        tmp.addViews();
        viewedMovies.add(tmp);
        videoDB.updateGenreViews(tmp);
    }

    /**
     * If user add rating operation was successful, add Movie to rating databases
     * (Search, BestUnseen Recommendation & Rating Query). Remove the video from the three
     * TreeSets, add rating, recalculate total rating and insert it back to resort.
     *
     * @param videoDB updates views TreeSets (Search & BestUnseen Recommendation)
     * @param title   title of the video to be added new rating
     * @param rating  the value of the new rating
     */
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

    /**
     * Function used for query command.
     *
     * @param query     query type (favorite, ratings, most_viewed, longest)
     * @param orderType order type (asc/desc)
     * @param year      year filter for the video
     * @param genre     genre filter for the video
     * @param k         number of titles to be returned
     * @return query operation or null if wrong query
     */
    public String getTopK(final String query, final String orderType,
                          final String year,
                          final String genre, final int k) {
        Movie tmp;
        Iterator<Movie> iterator;
        List<String> list = new ArrayList<>();

        /*
        Select TreeSet based on query. If order type is descending, iterate from last element,
        otherwise from head.
         */
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

        /*
        Iterate through the selected list. If the filter apply to the video add it to
        solution, also check the "k" boundary of solution.
         */
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
