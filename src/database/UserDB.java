package database;

import comparator.UserActivityCmp;
import fileio.UserInputData;
import user.User;

import java.util.*;

public class UserDB {
    private final HashMap<String, User> userDB = new HashMap<>();
    private final SortedSet<User> activeUsersAsc =
            new TreeSet<>(new UserActivityCmp(true));
    private final SortedSet<User> activeUsersDesc =
            new TreeSet<>(new UserActivityCmp(false));

    public void populateUserDB(List<UserInputData> users, MovieDB movieDB,
                               ShowDB showDB, VideoDB videoDB) {
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

    public boolean hasUser(String username) {
        return userDB.containsKey(username);
    }

    public Map<String, Integer> getHistory(String username) {
        Map<String, Integer> history = userDB.get(username).getHistory();
        return new HashMap<>(history);
    }

    public String addFavorites(MovieDB movieDB, ShowDB showDB, String username,
                               String title) {
        return userDB.get(username).addFavorite(movieDB, showDB, title);
    }

    public String addViews(VideoDB videoDB, MovieDB movieDB, ShowDB showDB,
                           String username,
                           String title) {
        return userDB.get(username).addViews(videoDB, movieDB, showDB, title);
    }

    public String addRatingMovie(ActorDB actorDB, VideoDB videoDB,
                                 MovieDB movieDB,
                                 String username,
                                 String title,
                                  double rating) {
        User tmp = userDB.get(username);
        activeUsersAsc.remove(tmp);
        activeUsersDesc.remove(tmp);
        String message = tmp.addRatingMovie(actorDB, videoDB, movieDB, title,
                rating);
        activeUsersDesc.add(tmp);
        activeUsersAsc.add(tmp);

        return message;
    }

    public String addRatingShow(ActorDB actorDB, VideoDB videoDB, ShowDB showDB,
                                 String username,
                                String title,
                                int season,
                                double rating) {
        User tmp = userDB.get(username);
        activeUsersAsc.remove(tmp);
        activeUsersDesc.remove(tmp);
        String message = tmp.addRatingShow(actorDB, videoDB, showDB, title,
                season,
                rating);
        activeUsersDesc.add(tmp);
        activeUsersAsc.add(tmp);

        return message;
    }

    public boolean checkUserSub(String username) {
        return userDB.get(username).isPremium();
    }

    public String getTopK(String orderType, int k) {
        List<String> list = new ArrayList<>();
        if (orderType.equals("desc")) {
            for (User user : activeUsersDesc) {
                if (user.isActive()) {
                    list.add(user.getUsername());
                }
                if (list.size() == k) {
                    break;
                }
            }
        } else {
            for (User user : activeUsersAsc) {
                if (user.isActive()) {
                    list.add(user.getUsername());
                }
                if (list.size() == k) {
                    break;
                }
            }
        }

        return list.toString();
    }
}
