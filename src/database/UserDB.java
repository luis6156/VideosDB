package database;

import comparator.UserActivityCmp;
import fileio.UserInputData;
import user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;

public class UserDB {
    private final HashMap<String, User> userDB = new HashMap<>();
    private final TreeSet<User> activeUsers = new TreeSet<>(new UserActivityCmp());

    /**
     * Constructor that assigns values to User class attributes and updates databases with its
     * data.
     *
     * @param users   list of UserInputData to be translated to User class
     * @param movieDB update MovieDB with user's favorite movies and history
     * @param showDB  update ShowDB with user's favorite shows and history
     * @param videoDB updates VideoDB with user's favorite videos and history
     */
    public void populateUserDB(final List<UserInputData> users, final MovieDB movieDB,
                               final ShowDB showDB, final VideoDB videoDB) {
        String title;
        // Create new User
        for (UserInputData user : users) {
            User newUser = new User(
                    user.getUsername(),
                    user.getSubscriptionType(),
                    user.getHistory(),
                    user.getFavoriteMovies()
            );
            // Put User into UserDB
            userDB.put(newUser.getUsername(), newUser);
            // Update database favorite videos, shows, movies
            for (int i = 0; i < user.getFavoriteMovies().size(); ++i) {
                title = user.getFavoriteMovies().get(i);
                if (movieDB.isMovie(title)) {
                    movieDB.addFavorites(videoDB, title);
                } else if (showDB.isShow(title)) {
                    showDB.addFavorites(videoDB, title);
                }
            }
            // Update database views for videos, shows, movies
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                title = entry.getKey();
                if (movieDB.isMovie(title)) {
                    for (int i = 0; i < entry.getValue(); ++i) {
                        movieDB.addViews(videoDB, title);
                    }
                } else if (showDB.isShow(title)) {
                    for (int i = 0; i < entry.getValue(); ++i) {
                        showDB.addViews(videoDB, title);
                    }
                }
            }
        }
    }

    /**
     * @param username user's username to be queried
     * @return true if user is in database, otherwise false
     */
    public boolean hasUser(final String username) {
        return userDB.containsKey(username);
    }

    /**
     * @param username user's username to be queried
     * @return unmodifiable map of user's history
     */
    public Map<String, Integer> getHistory(final String username) {
        Map<String, Integer> history = userDB.get(username).getHistory();
        return Collections.unmodifiableMap(history);
    }

    /**
     * @param videoDB  updates VideoDB favorite (Favorite Recommendation)
     * @param movieDB  updates MovieDB favorite (Favorite Query)
     * @param showDB   updates ShowDB favorite (Favorite Query)
     * @param username user's username to be queried
     * @param title    video title to be added to favorite
     * @return success or error
     */
    public String addFavorites(final VideoDB videoDB, final MovieDB movieDB, final ShowDB showDB,
                               final String username, final String title) {
        return userDB.get(username).addFavorite(videoDB, movieDB, showDB,
                title);
    }

    /**
     * @param videoDB  updates VideoDB views (Popular Recommendation)
     * @param movieDB  updates MovieDB favorite (Views Query)
     * @param showDB   updates ShowDB favorite (Views Query)
     * @param username user's username to be queried
     * @param title    video title to be incremented views counter
     * @return success or error
     */
    public String addViews(final VideoDB videoDB, final MovieDB movieDB, final ShowDB showDB,
                           final String username, final String title) {
        return userDB.get(username).addViews(videoDB, movieDB, showDB, title);
    }

    /**
     * Adds rating to a movie, updates user's activity if successful.
     *
     * @param actorDB  updated ActorDB rating (Average Actor Query)
     * @param videoDB  updates VideoDB rating (BestRatedUnseen & Search Recommendation)
     * @param movieDB  updates MovieDB rating (Rating Query)
     * @param username user's username to be queried
     * @param title    video title to be added new rating
     * @param rating   rating value
     * @return success or error
     */
    public String addRatingMovie(final ActorDB actorDB, final VideoDB videoDB,
                                 final MovieDB movieDB, final String username, final String title,
                                 final double rating) {
        User tmp = userDB.get(username);
        activeUsers.remove(tmp);
        String message = tmp.addRatingMovie(actorDB, videoDB, movieDB, title,
                rating);
        activeUsers.add(tmp);

        return message;
    }

    /**
     * Adds rating to a show's season, updates user's activity if successful.
     *
     * @param actorDB  updated ActorDB rating (Average Actor Query)
     * @param videoDB  updates VideoDB rating (BestRatedUnseen & Search Recommendation)
     * @param showDB   updates ShowDB rating (Rating Query)
     * @param username user's username to be queried
     * @param title    video title to be added new rating
     * @param season   season to be rated
     * @param rating   rating value
     * @return success or error
     */
    public String addRatingShow(final ActorDB actorDB, final VideoDB videoDB, final ShowDB showDB,
                                final String username, final String title, final int season,
                                final double rating) {
        User tmp = userDB.get(username);
        activeUsers.remove(tmp);
        String message = tmp.addRatingShow(actorDB, videoDB, showDB, title,
                season,
                rating);
        activeUsers.add(tmp);

        return message;
    }

    /**
     * @param username user's username to be queried
     * @return true is user is PREMIUM, otherwise false
     */
    public boolean checkUserType(final String username) {
        return userDB.get(username).isPremium();
    }

    /**
     * Function used for User Query command.
     *
     * @param orderType type of order (asc/desc)
     * @param k         number of users to be returned
     * @return most active users by specified order
     */
    public String getTopK(final String orderType, final int k) {
        User tmp;
        Iterator<User> iterator;
        List<String> list = new ArrayList<>();

        // If order type is descending, iterate from last element, otherwise from head
        if (orderType.equals("desc")) {
            iterator = activeUsers.descendingIterator();
        } else {
            iterator = activeUsers.iterator();
        }

        // Iterate through sorted TreeSet to get "k" most active users
        while (iterator.hasNext()) {
            tmp = iterator.next();
            if (tmp.isActive()) {
                list.add(tmp.getUsername());
            }
            if (list.size() == k) {
                return list.toString();
            }
        }

        return list.toString();
    }
}
