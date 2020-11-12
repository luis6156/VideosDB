package database;

import fileio.UserInputData;
import user.User;

import java.util.HashMap;
import java.util.List;

public class UserDB {
    private final HashMap<String, User> userDB;

    public UserDB() {
        userDB = new HashMap<String, User>();
    }

    public void populateUserDB(List<UserInputData> users) {
        for (UserInputData user : users) {
            User newUser = new User(
                    user.getUsername(),
                    user.getSubscriptionType(),
                    user.getHistory(),
                    user.getFavoriteMovies()
            );
            userDB.put(newUser.getUsername(), newUser);
        }
    }

    public String addFavorites(String username, String title) {
        return userDB.get(username).addFavorite(title);
    }

    public String addViews(String username, String title) {
        return userDB.get(username).addViews(title);
    }

    public String addRatingMovie(String username, String title, double rating) {
        return userDB.get(username).addRatingMovie(title, rating);
    }

    public String addRatingShow(String username, String title, int season,
                                double rating) {
        return userDB.get(username).addRatingShow(title, season, rating);
    }
}
