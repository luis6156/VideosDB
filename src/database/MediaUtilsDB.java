package database;

import video.Video;

public abstract class MediaUtilsDB {
    // Method to add views to video
    public abstract void addViews(final VideoDB videoDB, final String title);

    // Method to increment favorite counter to video
    public abstract void addFavorites(final VideoDB videoDB, final String title);

    /**
     * Checks if video respects filters, if any of the two filters are given (Video Query).
     *
     * @param video video to be queried
     * @param year  year filter
     * @param genre genre filter
     * @return true if video respects filters, otherwise false
     */
    public boolean validFilters(final Video video, final String year,
                                final String genre) {
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
    public abstract String getTopK(final String query, final String orderType, final String year,
                                   final String genre, final int k);
}
