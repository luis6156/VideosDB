package database;

import comparator.VideoDurationCmp;
import comparator.VideoFavoriteCmp;
import comparator.VideoRatingCmp;
import comparator.VideoViewCmp;
import fileio.SerialInputData;
import video.Show;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;

public class ShowDB extends MediaUtilsDB {
    private final HashMap<String, Show> showDB = new HashMap<>();
    private final TreeSet<Show> favShows = new TreeSet<>(new VideoFavoriteCmp());
    private final TreeSet<Show> viewedShows = new TreeSet<>(new VideoViewCmp());
    private final TreeSet<Show> longestShows = new TreeSet<>(new VideoDurationCmp());
    private final TreeSet<Show> ratedShows = new TreeSet<>(new VideoRatingCmp());

    /**
     * Constructor that assigns values to Show class attributes and updates databases with its
     * data.
     *
     * @param videoDB populate VideoDB for recommended
     * @param shows   list of SerialInputData to be translated to Show class
     */
    public void populateShowDB(final VideoDB videoDB, final List<SerialInputData> shows) {
        // Create new Show
        for (SerialInputData show : shows) {
            Show newShow = new Show(
                    show.getTitle(),
                    show.getYear(),
                    show.getGenres(),
                    show.getCast(),
                    show.getNumberSeason(),
                    show.getSeasons()
            );
            // Put Show into ShowDB
            showDB.put(show.getTitle(), newShow);
            // Add show to longestShow TreeSet (Longest Query)
            longestShows.add(newShow);
            // Add show to VideosDB (Recommended actions)
            videoDB.populateVideoDB(newShow);
        }
    }

    /**
     * Function used in commands to determine if a video is a show or not.
     *
     * @param title title of the video to be queried
     * @return true if video is a show, false otherwise
     */
    public boolean isShow(final String title) {
        return showDB.containsKey(title);
    }

    /**
     * Function used for rating comparator.
     *
     * @param title title of the video to be queried
     * @return the total rating of a show
     */
    public double getShowRating(final String title) {
        return showDB.get(title).getTotalRating();
    }

    /**
     * Function used to get list of actors from show in order to update their rating.
     *
     * @param title title of the video to be queried
     * @return unmodifiable list of actors from show
     */
    public List<String> getShowActors(final String title) {
        return Collections.unmodifiableList(showDB.get(title).getActors());
    }

    /**
     * If user addFavorite operation was successful, add Show to favorite databases
     * (Favorite Recommendation & Favorite Query). Remove the video from both TreeSets,
     * increment favorites counter and insert it back to resort.
     *
     * @param videoDB updates favorite TreeSet (Favorite Recommendation)
     * @param title   title of the video to be added to favorite
     */
    public void addFavorites(final VideoDB videoDB, final String title) {
        Show tmp = showDB.get(title);
        favShows.remove(tmp);
        videoDB.removeVideoFav(tmp);
        tmp.addFavorite();
        favShows.add(tmp);
        videoDB.addVideoFav(tmp);
    }

    /**
     * Add Show to views databases (Popular Recommendation & Views Query). Remove the video from
     * both TreeSets, increment views counter and insert it back to resort.
     *
     * @param videoDB updates views TreeSet (Popular Recommendation)
     * @param title   title of the video to be incremented views counter
     */
    public void addViews(final VideoDB videoDB, final String title) {
        Show tmp = showDB.get(title);
        viewedShows.remove(tmp);
        tmp.addViews();
        viewedShows.add(tmp);
        videoDB.updateGenreViews(tmp);
    }

    /**
     * If user add rating operation was successful, add Show to rating databases
     * (Search, BestUnseen Recommendation & Rating Query). Remove the video from the three
     * TreeSets, add rating, recalculate total rating and insert it back to resort.
     *
     * @param videoDB updates views TreeSets (Search & BestUnseen Recommendation)
     * @param title   title of the video to be added new rating
     * @param season  season to be added new rating
     * @param rating  the value of the new rating
     */
    public void addRating(final VideoDB videoDB, final String title, final int season,
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

    /**
     * Function used for query command.
     *
     * @param query     query type (favorite, ratings, most_viewed, longest)
     * @param orderType order type (asc/desc)
     * @param year      year filter for the video
     * @param genre     genre filter for the video
     * @param k         number of titles to be returned
     * @return query operation or null if wrong query
     */
    public String getTopK(final String query, final String orderType, final String year,
                          final String genre, final int k) {
        Show tmp;
        Iterator<Show> iterator;
        List<String> list = new ArrayList<>();

        /*
        Select TreeSet based on query. If order type is descending, iterate from last element,
        otherwise from head.
         */
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

        /*
        Iterate through the selected list. If the filter apply to the video add it to
        solution, also check the "k" boundary of solution
         */
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
