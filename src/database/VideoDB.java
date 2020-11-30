package database;

import comparator.VideoRatingCmp;
import comparator.RecomFavCmp;
import comparator.RecomRatingCmp;
import entertainment.GenrePopularity;
import video.Video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public final class VideoDB {
    private final List<String> unorderedVideos = new ArrayList<>();
    private final Map<String, Integer> videoByIndex = new HashMap<>();
    private final Map<String, GenrePopularity> genreViews = new HashMap<>();
    private final TreeSet<GenrePopularity> mostViewedGenres = new TreeSet<>();
    private final Map<String, TreeSet<Video>> genreVideoRatings = new HashMap<>();
    private final TreeSet<Video> bestRatedVideos = new TreeSet<>(new RecomRatingCmp(videoByIndex));
    private final TreeSet<Video> mostFavVideos = new TreeSet<>(new RecomFavCmp(videoByIndex));
    private int count = 0;

    /**
     * Populates all data structures with the video given as parameter
     *
     * @param video video to be added to database
     */
    public void populateVideoDB(final Video video) {
        unorderedVideos.add(unorderedVideos.size(), video.getTitle());
        videoByIndex.put(video.getTitle(), count++);
        bestRatedVideos.add(video);
        populateGenreViews(video);
        addGenreVideoRatings(video);
    }

    /**
     * Adds video's genres to the HashMap and TreeSet to be sorted by total views or alphabetically
     * in descending order (Popular Recommendation)
     *
     * @param video video used to extract information
     */
    private void populateGenreViews(final Video video) {
        String genreName;

        // Get all video genres, if genre doesn't exist -> add it to HashMap, create a
        // GenrePopularity object and add it to the TreeSet
        for (int i = 0; i < video.getGenres().size(); ++i) {
            genreName = video.getGenres().get(i);
            GenrePopularity genre = genreViews.get(genreName);
            if (genre == null) {
                genre = new GenrePopularity(genreName);
                genreViews.put(genreName, genre);
                mostViewedGenres.add(genre);
            }
            // add video title to GenrePopularity object
            genre.addVideos(video.getTitle());
        }
    }

    /**
     * Removes all genres from TreeSet, updates the views and adds it back to reset order
     * (Popular Recommendation). Only one method is necessary for update because this TreeSet
     * uses another type of Object that can be queried and changed from VideoDB (GenrePopularity)
     *
     * @param video video used to extract information
     */
    public void updateGenreViews(final Video video) {
        String genreName;

        for (int i = 0; i < video.getGenres().size(); ++i) {
            genreName = video.getGenres().get(i);
            GenrePopularity genre = genreViews.get(genreName);
            mostViewedGenres.remove(genre);
            genre.addViews();
            mostViewedGenres.add(genre);
        }
    }

    /**
     * Removes video from GenreVideoRatings TreeSet, if genre is not present in HashMap -> add it
     * (Search Recommendation)
     *
     * @param video video used to extract information
     */
    public void removeGenreVideoRatings(final Video video) {
        String genreName;

        for (int i = 0; i < video.getGenres().size(); ++i) {
            genreName = video.getGenres().get(i);
            TreeSet<Video> genre = genreVideoRatings.get(genreName);
            if (genre != null) {
                genre.remove(video);
            } else {
                genre = new TreeSet<>(new VideoRatingCmp());
                genreVideoRatings.put(genreName, genre);
            }
        }
    }

    /**
     * Adds video to GenreVideoRatings TreeSet, if genre is not present in HashMap -> add it
     * (Search Recommendation)
     *
     * @param video video used to extract information
     */
    public void addGenreVideoRatings(final Video video) {
        String genreName;

        for (int i = 0; i < video.getGenres().size(); ++i) {
            genreName = video.getGenres().get(i);
            TreeSet<Video> genre = genreVideoRatings.get(genreName);
            if (genre == null) {
                genre = new TreeSet<>(new VideoRatingCmp());
                genreVideoRatings.put(genreName, genre);
            }
            genre.add(video);
        }
    }

    /**
     * Removes video from VideoRatings TreeSet (BestRatedUnseen Recommendation)
     *
     * @param video video to be removed
     */
    public void removeVideoRatings(final Video video) {
        bestRatedVideos.remove(video);
    }

    /**
     * Adds video from VideoRatings TreeSet (BestRatedUnseen Recommendation)
     *
     * @param video video to be added
     */
    public void addVideoRatings(final Video video) {
        bestRatedVideos.add(video);
    }

    /**
     * Removes video from VideoFav TreeSet (Favorite Recommendation)
     *
     * @param video video to be removed
     */
    public void removeVideoFav(final Video video) {
        mostFavVideos.remove(video);
    }

    /**
     * Adds video from VideoFav TreeSet (Favorite Recommendation)
     *
     * @param video video to be added
     */
    public void addVideoFav(final Video video) {
        mostFavVideos.add(video);
    }

    /**
     * Gets the first video unwatched by user by order of insertion
     *
     * @param history user's history
     * @return Standard Recommendation result
     */
    public String getUnwatchedVideo(final Map<String, Integer> history) {
        for (String unorderedVideo : unorderedVideos) {
            if (!history.containsKey(unorderedVideo)) {
                return "StandardRecommendation result: " + unorderedVideo;
            }
        }

        return "StandardRecommendation cannot be applied!";
    }

    /**
     * Gets the first video unwatched by user by rating or order of insertion
     *
     * @param history user's history
     * @return BestRatedUnseen Recommendation result
     */
    public String getBestVideo(final Map<String, Integer> history) {
        for (Video video : bestRatedVideos) {
            if (!history.containsKey(video.getTitle())) {
                return "BestRatedUnseenRecommendation result: "
                        + video.getTitle();
            }
        }

        return "BestRatedUnseenRecommendation cannot be applied!";
    }

    /**
     * Gets the first video unwatched by user iterating from the most popular genre to the least.
     * Popularity is determined by the number of views or by insertion order.
     *
     * @param history user's history
     * @return Popular Recommendation result
     */
    public String getPopularVideo(final Map<String, Integer> history) {
        String success;

        for (GenrePopularity genre : mostViewedGenres) {
            success = genre.getUnwatchedVideo(history);
            if (success != null) {
                return "PopularRecommendation result: " + success;
            }
        }

        return "PopularRecommendation cannot be applied!";
    }

    /**
     * Gets the first video unwatched by user by number of favorites or order of insertion
     *
     * @param history user's history
     * @return Favorite Recommendation result
     */
    public String getFavoriteVideo(final Map<String, Integer> history) {
        for (Video video : mostFavVideos) {
            if (!history.containsKey(video.getTitle())) {
                return "FavoriteRecommendation result: " + video.getTitle();
            }
        }

        return "FavoriteRecommendation cannot be applied!";
    }

    /**
     * Gets the first video unwatched by user by rating (descending) or name from a genre
     *
     * @param genre   genre to be queried
     * @param history user's history
     * @return Search Recommendation result
     */
    public String getSearchedVideo(final String genre, final Map<String, Integer> history) {
        List<String> solution = new ArrayList<>();
        String message = "SearchRecommendation result: ";

        TreeSet<Video> videos = genreVideoRatings.get(genre);

        // No video with this genre exists
        if (videos == null) {
            return "SearchRecommendation cannot be applied!";
        }

        for (Video video : videos) {
            if (!history.containsKey(video.getTitle())) {
                solution.add(video.getTitle());
            }
        }

        // All videos from genres were watched
        if (solution.isEmpty()) {
            return "SearchRecommendation cannot be applied!";
        }

        return message + solution.toString();
    }
}
