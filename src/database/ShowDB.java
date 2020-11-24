package database;

import comparator.DurationCmp;
import comparator.FavoriteCmp;
import comparator.RatingCmp;
import comparator.ViewCmp;
import fileio.SerialInputData;
import video.Show;
import video.Video;

import java.util.*;

public class ShowDB {
    private final HashMap<String, Show> showDB = new HashMap<>();
    private final SortedSet<Show> favShowsDesc =
            new TreeSet<>(new FavoriteCmp(false));
    private final SortedSet<Show> favShowsAsc =
            new TreeSet<>(new FavoriteCmp(true));
    private final SortedSet<Show> viewedShowsAsc =
            new TreeSet<>(new ViewCmp(true));
    private final SortedSet<Show> viewedShowsDesc =
            new TreeSet<>(new ViewCmp(false));
    private final SortedSet<Show> longestShowsAsc =
            new TreeSet<>(new DurationCmp(true));
    private final SortedSet<Show> longestShowsDesc =
            new TreeSet<>(new DurationCmp(false));
    private final SortedSet<Show> ratedShowsAsc =
            new TreeSet<>(new RatingCmp(true));
    private final SortedSet<Show> ratedShowsDesc =
            new TreeSet<>(new RatingCmp(false));

    public void populateShowDB(VideoDB videoDB, List<SerialInputData> showDB) {
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
            longestShowsAsc.add(newShow);
            longestShowsDesc.add(newShow);
            ratedShowsAsc.add(newShow);
            ratedShowsDesc.add(newShow);
            videoDB.populateVideoDB(newShow);
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

    public void addFavorites(VideoDB videoDB, String title) {
        Show tmp = showDB.get(title);
        favShowsDesc.remove(tmp);
        favShowsAsc.remove(tmp);
        tmp.addFavorite();
        favShowsDesc.add(tmp);
        favShowsAsc.add(tmp);
        videoDB.updateVideoFav(tmp);
    }

    public void addViews(VideoDB videoDB, String title) {
        Show tmp = showDB.get(title);
        viewedShowsAsc.remove(tmp);
        viewedShowsDesc.remove(tmp);
        tmp.addViews();
        viewedShowsAsc.add(tmp);
        viewedShowsDesc.add(tmp);
        videoDB.updateGenreViews(tmp);
    }

    public void addRating(VideoDB videoDB, String title, int season,
                          double rating) {
        Show tmp = showDB.get(title);
        ratedShowsDesc.remove(tmp);
        ratedShowsAsc.remove(tmp);
        videoDB.removeGenreVideoRatings(tmp);
        tmp.addRating(season, rating);
        ratedShowsAsc.add(tmp);
        ratedShowsDesc.add(tmp);
        videoDB.updateGenreVideoRatings(tmp);
        videoDB.updateVideoRatings(tmp);
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
                                String genre,
                                int k) {
        List<String> list = new ArrayList<>();
        switch (query) {
            case "favorite":
                if (orderType.equals("desc")) {
                    for (Show show : favShowsDesc) {
                        if (validFilters(show, year, genre)) {
                            list.add(show.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                } else {
                    for (Show show : favShowsAsc) {
                        if (validFilters(show, year, genre)) {
                            list.add(show.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                }
                break;
            case "ratings":
                if (orderType.equals("desc")) {
                    for (Show show : ratedShowsDesc) {
                        if (validFilters(show, year, genre) && show.getTotalRating() != 0) {
                            list.add(show.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                } else {
                    for (Show show : ratedShowsAsc) {
                        if (validFilters(show, year, genre) && show.getTotalRating() != 0) {
                            list.add(show.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                }
                break;
            case "most_viewed":
                if (orderType.equals("desc")) {
                    for (Show show : viewedShowsDesc) {
                        if (validFilters(show, year, genre)) {
                            list.add(show.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                } else {
                    for (Show show : viewedShowsAsc) {
                        if (validFilters(show, year, genre)) {
                            list.add(show.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                }
                break;
            case "longest":
                if (orderType.equals("desc")) {
                    for (Show show : longestShowsDesc) {
                        if (validFilters(show, year, genre)) {
                            list.add(show.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                } else {
                    for (Show show : longestShowsAsc) {
                        if (validFilters(show, year, genre)) {
                            list.add(show.getTitle());
                        }
                        if (list.size() == k) {
                            break;
                        }
                    }
                }
                break;
            default:
                break;
        }

        return list;
    }
}
