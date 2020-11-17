package database;

import comparator.DurationCmp;
import comparator.FavoriteCmp;
import comparator.RatingCmp;
import comparator.ViewCmp;
import fileio.SerialInputData;
import video.Genre;
import video.Show;

import java.util.*;

public class ShowDB extends VideoDB {
    private final HashMap<String, Show> showDB = new HashMap<>();
    private final SortedSet<Show> favShows = new TreeSet<>(new FavoriteCmp());
    private final SortedSet<Show> viewedShows = new TreeSet<>(new ViewCmp());
    private final SortedSet<Show> longestShows =
            new TreeSet<>(new DurationCmp());
    private final SortedSet<Show> ratedShows = new TreeSet<>(new RatingCmp());

    public void populateVideoDB(List<SerialInputData> showDB) {
        for (SerialInputData show : showDB) {
            Show newShow = new Show(
                    show.getTitle(),
                    show.getYear(),
                    show.getGenres(),
                    show.getCast(),
                    show.getNumberSeason(),
                    show.getSeasons()
            );
            this.showDB.put(show.getTitle(), newShow);
            longestShows.add(newShow);
        }
    }

    public boolean isShow(String title) {
        return showDB.containsKey(title);
    }

    public double getShowRating(String title) {
        return showDB.get(title).getTotalRating();
    }

    public List<String> getShowActors(String title) {
        return showDB.get(title).getActors();
    }

    public void addFavorites(String title) {
        Show tmp = showDB.get(title);
        favShows.remove(tmp);
        tmp.addFavorite();
        favShows.add(tmp);
    }

    public void addViews(String title) {
        Show tmp = showDB.get(title);
        viewedShows.remove(tmp);
        tmp.addViews();
        viewedShows.add(tmp);
    }

    public void addRating(String title, int season, double rating) {
        Show tmp = showDB.get(title);
        ratedShows.remove(tmp);
        tmp.addRating(season, rating);
        ratedShows.add(tmp);
    }

    public List<String> getTopK(String query, String year, String genre,
                                int k) {
        List<String> list = new ArrayList<>();
        switch (query) {
            case "favorite":
                for (Show show : favShows) {
                    if (validFilters(show, year, genre)) {
                        list.add(show.getTitle());
                    }
                    if (list.size() == k) {
                        break;
                    }
                }
                break;
            case "ratings":
                for (Show show : ratedShows) {
                    if (validFilters(show, year, genre)) {
                        list.add(show.getTitle());
                    }
                    if (list.size() == k) {
                        break;
                    }
                }
            case "most_viewed":
                for (Show show : viewedShows) {
                    if (validFilters(show, year, genre)) {
                        list.add(show.getTitle());
                    }
                    if (list.size() == k) {
                        break;
                    }
                }
                break;
            case "longest":
                for (Show show : longestShows) {
                    if (validFilters(show, year, genre)) {
                        list.add(show.getTitle());
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

    public List<Show> getTopRatedShows() {
        return new ArrayList<>(ratedShows);
    }
}
