package user;

import database.ActorDB;
import database.MovieDB;
import database.ShowDB;
import database.VideoDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteVideos;
    private final HashMap<String, Double> rated = new HashMap<>();

    /**
     * @param username user username
     * @param subscriptionType user subscription (BASIC/PREMIUM)
     * @param history user history list
     * @param favoriteMovies user favorite videos list
     */
    public User(final String username, final String subscriptionType,
                final Map<String, Integer> history, final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteVideos = favoriteMovies;
        this.history = history;
    }

    /**
     * Adds video to favorite and updates database if possible by checking user's history and
     * favorites history
     * @param videoDB update VideoDB used for recommendations
     * @param movieDB if video is a movie update MovieDB
     * @param showDB if video is a show update ShowDB
     * @param title video title
     * @return success or error
     */
    public String addFavorite(final VideoDB videoDB, final MovieDB movieDB, final ShowDB showDB,
                              final String title) {
        String message;

        // Video is already in favorites -> return error
        if (favoriteVideos.contains(title)) {
            message = "error -> " + title + " is already in favourite list";
            return message;
        }

        // Video wasn't watched -> return error
        if (!history.containsKey(title)) {
            message = "error -> " + title + " is not seen";
            return message;
        }

        // Add video in user's favorites and database -> return success
        message = "success -> " + title + " was added as favourite";
        favoriteVideos.add(title);
        if (movieDB.isMovie(title)) {
            movieDB.addFavorites(videoDB, title);
        } else {
            showDB.addFavorites(videoDB, title);
        }
        return message;
    }

    /**
     * Increments video views and updates database by checking user's history
     * @param videoDB update VideoDB used for recommendations
     * @param movieDB if video is a movie update MovieDB
     * @param showDB if video is a show update ShowDB
     * @param title video title
     * @return success or error
     */
    public String addViews(final VideoDB videoDB, final MovieDB movieDB, final ShowDB showDB,
                           final String title) {
        String message;

        // Video was watched already -> increment views, update database & return success
        if (history.containsKey(title)) {
            int views = history.get(title);
            history.put(title, ++views);
            if (movieDB.isMovie(title)) {
                movieDB.addViews(videoDB, title);
            } else {
                showDB.addViews(videoDB, title);
            }
            message = "success -> " + title + " was viewed with total views of " + views;
            return message;
        }

        // Video wasn't watched already -> add to history, update database & return success
        history.put(title, 1);
        if (movieDB.isMovie(title)) {
            movieDB.addViews(videoDB, title);
        } else {
            showDB.addViews(videoDB, title);
        }
        message = "success -> " + title + " was viewed with total views of " + 1;
        return message;
    }

    /**
     * Adds movie rating and updates database by checking user's history and rating history
     * @param actorDB update actors rating in ActorDB
     * @param videoDB update VideoDB for recommendations
     * @param movieDB update MovieDB
     * @param title video title
     * @param rating rating to be added
     * @return success or error
     */
    public String addRatingMovie(final ActorDB actorDB, final VideoDB videoDB,
                                 final MovieDB movieDB,
                                 final String title,
                                 final double rating) {
        String message;

        // Video wasn't watched -> return error
        if (!history.containsKey(title)) {
            message = "error -> " + title + " is not seen";
            return message;
        }

        // Video has been already rated -> return error
        if (rated.containsKey(title)) {
            message = "error -> " + title + " has been already rated";
            return message;
        }

        // Video wasn't rated -> add rating, update database & return success
        rated.put(title, rating);
        movieDB.addRating(videoDB, title, rating);
        actorDB.addRating(movieDB.getMovieActors(title), title,
                movieDB.getMovieRating(title));
        message =
                "success -> " + title + " was rated with " + rating + " by " + username;
        return message;
    }

    /**
     * Adds show rating and updates database by checking user's history and rating history
     * @param actorDB update actors rating in ActorDB
     * @param videoDB update VideoDB for recommendations
     * @param showDB update MovieDB
     * @param title video title
     * @param season season to be rated
     * @param rating rating to be added
     * @return success or error
     */
    public String addRatingShow(final ActorDB actorDB, final VideoDB videoDB,
                                final ShowDB showDB,
                                final String title,
                                final int season,
                                final double rating) {
        String message;

        // Video wasn't watched -> return error
        if (!history.containsKey(title)) {
            message = "error -> " + title + " is not seen";
            return message;
        }

        // Video has been already rated -> return error
        if (rated.containsKey(title + "season" + season)) {
            message = "error -> " + title + " has been already rated";
            return message;
        }

        /*
         * Video wasn't rated -> add rating, update database & return success.
         * Here the key is the title and the season watched, that way we have
         * separate entries for different seasons.
         */
        rated.put(title + "season" + season, rating);
        showDB.addRating(videoDB, title, season, rating);
        actorDB.addRating(showDB.getShowActors(title), title,
                showDB.getShowRating(title));
        message =
                "success -> " + title + " was rated with " + rating + " by " + username;
        return message;
    }

    // Returns user's username
    public String getUsername() {
        return username;
    }

    // Returns immutable map of user's history
    public Map<String, Integer> getHistory() {
        return Collections.unmodifiableMap(history);
    }

    // Checks if user is premium
    public boolean isPremium() {
        return !subscriptionType.equals("BASIC");
    }

    // Returns user's activity
    public int getActivity() {
        return rated.size();
    }

    // Checks if user is active
    public boolean isActive() {
        return rated.size() != 0;
    }
}
