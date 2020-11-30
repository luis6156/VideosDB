package database;

import comparator.VideoDurationCmp;
import comparator.VideoFavoriteCmp;
import comparator.VideoRatingCmp;
import comparator.VideoViewCmp;
import fileio.SerialInputData;
import video.Show;

import java.util.*;

public class ShowDB extends MediaUtilsDB {
    private final HashMap<String, Show> showDB = new HashMap<>();
    private final TreeSet<Show> favShows =
            new TreeSet<>(new VideoFavoriteCmp());
    private final TreeSet<Show> viewedShows =
            new TreeSet<>(new VideoViewCmp());
    private final TreeSet<Show> longestShows =
            new TreeSet<>(new VideoDurationCmp());
    private final TreeSet<Show> ratedShows =
            new TreeSet<>(new VideoRatingCmp());

    public void populateShowDB(final VideoDB videoDB,
                               final List<SerialInputData> showDB) {
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
            videoDB.populateVideoDB(newShow);
        }
    }

    public boolean isShow(final String title) {
        return showDB.containsKey(title);
    }

    public double getShowRating(final String title) {
        return showDB.get(title).getTotalRating();
    }

    public List<String> getShowActors(final String title) {
        return Collections.unmodifiableList(showDB.get(title).getActors());
    }

    public void addFavorites(final VideoDB videoDB, final String title) {
        Show tmp = showDB.get(title);
        favShows.remove(tmp);
        videoDB.removeVideoFav(tmp);
        tmp.addFavorite();
        favShows.add(tmp);
        videoDB.addVideoFav(tmp);
    }

    public void addViews(final VideoDB videoDB, final String title) {
        Show tmp = showDB.get(title);
        viewedShows.remove(tmp);
        tmp.addViews();
        viewedShows.add(tmp);
        videoDB.updateGenreViews(tmp);
    }

    public void addRating(final VideoDB videoDB, final String title,
                          final int season,
                          final double rating) {
        Show tmp = showDB.get(title);
        ratedShows.remove(tmp);
        videoDB.removeGenreVideoRatings(tmp);
        videoDB.removeVideoRatings(tmp);
        tmp.addRating(season, rating);
        ratedShows.add(tmp);
        videoDB.addGenreVideoRatings(tmp);
        videoDB.addVideoRatings(tmp);
    }

    public String getTopK(final String query, final String orderType,
                            final String year,
                            final String genre,
                            final int k) {
        Show tmp;
        Iterator<Show> iterator;
        List<String> list = new ArrayList<>();
        switch (query) {
            case "favorite":
                if (orderType.equals("desc")) {
                    iterator = favShows.descendingIterator();
                } else {
                    iterator = favShows.iterator();
                }
                break;
            case "ratings":
                if (orderType.equals("desc")) {
                    iterator =
                            ratedShows.descendingIterator();
                } else {
                    iterator = ratedShows.iterator();
                }
                break;
            case "most_viewed":
                if (orderType.equals("desc")) {
                    iterator = viewedShows.descendingIterator();
                } else {
                    iterator = viewedShows.iterator();
                }
                break;
            case "longest":
                if (orderType.equals("desc")) {
                    iterator = longestShows.descendingIterator();
                } else {
                    iterator = longestShows.iterator();
                }
                break;
            default:
                return null;
        }

        while (iterator.hasNext()) {
            tmp = iterator.next();
            if (validFilters(tmp, year, genre)) {
                list.add(tmp.getTitle());
            }
            if (list.size() == k) {
                return list.toString();
            }
        }

        return list.toString();
    }
}
