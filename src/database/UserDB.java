package database;

import fileio.UserInputData;
import user.User;
import video.MovieDB;
import video.ShowDB;
import video.VideoDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDB {
    private final HashMap<String, User> userDB;

    public UserDB() {
        userDB = new HashMap<>();
    }

    public void populateUserDB(List<UserInputData> users, MovieDB movieDB,
                               ShowDB showDB) {
        String title;
        for (UserInputData user : users) {
            User newUser = new User(
                    user.getUsername(),
                    user.getSubscriptionType(),
                    user.getHistory(),
                    user.getFavoriteMovies()
            );
            userDB.put(newUser.getUsername(), newUser);
            for (int i = 0; i < user.getFavoriteMovies().size(); ++i) {
                title = user.getFavoriteMovies().get(i);
                if (movieDB.isMovie(title)) {
                    movieDB.addFavorites(title);
                } else if (showDB.isShow(title)) {
                    showDB.addFavorites(title);
                }
            }
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                title = entry.getKey();
                if (movieDB.isMovie(title)) {
                    for (int i = 0; i < entry.getValue(); ++i) {
                        movieDB.addViews(title);
                    }
                } else if (showDB.isShow(title)) {
                    for (int i = 0; i < entry.getValue(); ++i) {
                        showDB.addViews(title);
                    }
                }
            }
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
