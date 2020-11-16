package database;

import comparator.ActiveUserCmp;
import fileio.UserInputData;
import user.User;
import video.*;

import java.util.*;

public class UserDB {
    private final HashMap<String, User> userDB = new HashMap<>();
    private final SortedSet<User> activeUsers = new TreeSet<>(new ActiveUserCmp());

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
        User tmp = userDB.get(username);
        activeUsers.remove(tmp);
        String message = tmp.addRatingMovie(title, rating);
        activeUsers.add(tmp);

        return message;
    }

    public String addRatingShow(String username, String title, int season,
                                double rating) {
        User tmp = userDB.get(username);
        activeUsers.remove(tmp);
        String message = tmp.addRatingShow(title, season, rating);
        activeUsers.add(tmp);

        return message;
    }

    public String getTopK(int k) {
        List<String> list = new ArrayList<>();
        for (User user : activeUsers) {
            if (user.getActivity() != 0) {
                list.add(user.getUsername());
            }
            if (list.size() == k) {
                break;
            }
        }

        return list.toString();
    }
}
