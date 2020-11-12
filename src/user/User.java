package user;

import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    /**
     * User's username
     */
    private final String username;
    /**
     * Subscription Type
     */
    private final String subscriptionType;
    /**
     * The history of the movies seen
     */
    private final Map<String, Integer> history;
    /**
     * Movies added to favorites
     */
    private final ArrayList<String> favoriteMovies;

    private final HashMap<String, Double> rated;

    public User(final String username, final String subscriptionType,
                final Map<String, Integer> history,
                final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteMovies = favoriteMovies;
        this.history = history;
        rated = new HashMap<String, Double>();
    }

    public String addFavorite(String title) {
        String message;

        // Video is already in favorites -> return error
        if (favoriteMovies.contains(title)) {
            message = "error -> " + title + " is already in favourite list";
            return message;
        }

        // Video wasn't watched -> return error
        if (!history.containsKey(title)) {
            message = "error -> " + title + " is not seen";
            return message;
        }

        // Add video in favorites -> return success
        message = "success -> " + title + " was added as favourite";
        return message;
    }

    public String addViews(String title) {
        String message;

        // Video was watched already -> increment views & return success
        if (history.containsKey(title)) {
            int views = history.get(title);
            history.put(title, ++views);
            message = "success -> " + title + " was viewed with total views of " + views;
            return message;
        }

        // Video wasn't watched already -> add to history & return success
        history.put(title, 1);
        message = "success -> " + title + " was viewed with total views of " + 1;
        return message;
    }

    public String addRatingMovie(String title, double rating) {
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

        // Video wasn't rated -> add rating & return success
        rated.put(title, rating);
        message =
                "success -> " + title + " was rated with " + rating + " by " + username;
        return message;
    }

    public String addRatingShow(String title, int season, double rating) {
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

        /*
         * Video wasn't rated -> add rating & return success.
         * Here the key is the title and the season watched, that way we have
         * separate entries for different seasons.
         */
        rated.put(title + "season" + season, rating);
        message =
                "success -> " + title + " was rated with " + rating + " by " + username;
        return message;
    }

    public String getUsername() {
        return username;
    }
}
