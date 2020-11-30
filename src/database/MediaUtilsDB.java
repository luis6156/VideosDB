package database;

import video.Video;

public abstract class MediaUtilsDB {
    public abstract void addViews(final VideoDB videoDB, final String title);

    public abstract void addFavorites(final VideoDB videoDB, final String title);

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

    public abstract String getTopK(final String query, final String orderType, final String year,
                                   final String genre, final int k);
}
