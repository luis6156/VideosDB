package database;

import video.Video;

public abstract class MediaUtilsDB {
    /**
     * Increment video favorite counter and update databases.
     *
     * @param videoDB updates favorite TreeSet (Favorite Recommendation)
     * @param title   title of the video to be added to favorite
     */
    public abstract void addViews(VideoDB videoDB, String title);

    /**
     * Increment video views counter and update databases.
     *
     * @param videoDB updates views TreeSet (Popular Recommendation)
     * @param title   title of the video to be incremented views counter
     */
    public abstract void addFavorites(VideoDB videoDB, String title);

    /**
     * Checks if video respects filters, if any of the two filters are given (Video Query).
     *
     * @param video video to be queried
     * @param year  year filter
     * @param genre genre filter
     * @return true if video respects filters, otherwise false
     */
    protected boolean validFilters(final Video video, final String year, final String genre) {
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

    /**
     * Function used for Video Query command.
     *
     * @param query     query type (favorite, ratings, most_viewed, longest)
     * @param orderType order type (asc/desc)
     * @param year      year filter for the video
     * @param genre     genre filter for the video
     * @param k         number of titles to be returned
     * @return query operation or null if wrong query
     */
    public abstract String getTopK(String query, String orderType, String year, String genre,
                                   int k);
}
