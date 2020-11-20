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
    private final SortedSet<Show> favShows = new TreeSet<>(new FavoriteCmp());
    private final SortedSet<Show> viewedShowsAsc =
            new TreeSet<>(new ViewCmp(true));
    private final SortedSet<Show> viewedShowsDesc =
            new TreeSet<>(new ViewCmp(false));
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
            ratedShows.add(newShow);
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
        ratedShows.remove(tmp);
        tmp.addRating(season, rating);
        ratedShows.add(tmp);
        videoDB.updateGenreVideoRatings(tmp);
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
                    if (validFilters(show, year, genre) && show.getTotalRating() != 0) {
                        list.add(show.getTitle());
                    }
                    if (list.size() == k) {
                        break;
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

    public List<Show> getTopFavShows() {
        return new ArrayList<>(favShows);
    }
}
