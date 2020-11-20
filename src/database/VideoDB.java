package database;

import comparator.RatingCmp;
import comparator.RecomFavCmp;
import comparator.RecomRatingCmp;
import fileio.MovieInputData;
import fileio.SerialInputData;
import video.GenreViews;
import video.Movie;
import video.Show;
import video.Video;

import java.util.*;

public class VideoDB {
    private final List<String> unorderedVideos = new ArrayList<>();
    private final Map<String, Integer> videoByIndex = new HashMap<>();
    private final Map<String, GenreViews> genreViews = new HashMap<>();
    private final SortedSet<GenreViews> mostViewedGenres = new TreeSet<>();
    private final Map<String, SortedSet<Video>> genreVideoRatings =
            new HashMap<>();
    private final SortedSet<Video> bestRatedVideos =
            new TreeSet<>(new RecomRatingCmp(videoByIndex));
    private final SortedSet<Video> mostFavVideos =
            new TreeSet<>(new RecomFavCmp(videoByIndex));
    private int count = 0;

    public void populateVideoDB(Video video) {
        unorderedVideos.add(unorderedVideos.size(), video.getTitle());
        videoByIndex.put(video.getTitle(), count++);
        bestRatedVideos.add(video);
        populateGenreViews(video);
        updateGenreVideoRatings(video);
    }

    public void populateGenreViews(Video video) {
        String genreName;

        for (int i = 0; i < video.getGenres().size(); ++i) {
            genreName = video.getGenres().get(i);
            GenreViews genre = genreViews.get(genreName);
            if (genre != null) {
                genre.addVideos(video.getTitle());
            } else {
                genre = new GenreViews(video.getTitle());
                genreViews.put(genreName, genre);
                mostViewedGenres.add(genre);
            }
        }
    }

    public void updateGenreViews(Video video) {
        String genreName;

        for (int i = 0; i < video.getGenres().size(); ++i) {
            genreName = video.getGenres().get(i);
            GenreViews genre = genreViews.get(genreName);
            mostViewedGenres.remove(genre);
            genre.addViews();
            mostViewedGenres.add(genre);
        }
    }

    public void removeGenreVideoRatings(Video video) {
        String genreName;

        for (int i = 0; i < video.getGenres().size(); ++i) {
            genreName = video.getGenres().get(i);
            SortedSet<Video> genre = genreVideoRatings.get(genreName);
            if (genre != null) {
                genre.remove(video);
            } else {
                genre = new TreeSet<>(new RatingCmp(true));
                genreVideoRatings.put(genreName, genre);
            }
        }
    }

    public void updateGenreVideoRatings(Video video) {
        String genreName;

        for (int i = 0; i < video.getGenres().size(); ++i) {
            genreName = video.getGenres().get(i);
            SortedSet<Video> genre = genreVideoRatings.get(genreName);
            if (genre == null) {
                genre = new TreeSet<>(new RatingCmp(true));
                genreVideoRatings.put(genreName, genre);
            }
            genre.add(video);
        }
    }

    public void updateVideoRatings(Video video) {
        bestRatedVideos.remove(video);
        bestRatedVideos.add(video);
    }

    public void updateVideoFav(Video video) {
        mostFavVideos.remove(video);
        mostFavVideos.add(video);
    }

    public String getUnwatchedVideo(Map<String, Integer> history) {
        for (String unorderedVideo : unorderedVideos) {
            if (!history.containsKey(unorderedVideo)) {
                return "StandardRecommendation result: " + unorderedVideo;
            }
        }

        return "StandardRecommendation cannot be applied!";
    }

    public String getBestVideo(Map<String, Integer> history) {
        for (Video video : bestRatedVideos) {
            if (!history.containsKey(video.getTitle())) {
                return "BestRatedUnseenRecommendation result: " + video.getTitle();
            }
        }

        return "BestRatedUnseenRecommendation cannot be applied!";
    }

    public String getPopularVideo(Map<String, Integer> history) {
        String success;

        for (GenreViews genre : mostViewedGenres) {
            success = genre.getUnwatchedVideo(history);
            if (success != null) {
                return "PopularRecommendation result: " + success;
            }
        }

        return "PopularRecommendation cannot be applied!";
    }

    public String getFavoriteVideo(Map<String, Integer> history) {
        for (Video video : mostFavVideos) {
            if (!history.containsKey(video.getTitle())) {
                return "FavoriteRecommendation result: " + video.getTitle();
            }
        }

        return "FavoriteRecommendation cannot be applied!";
    }

    public String getSearchedVideo(String genre, Map<String, Integer> history) {
        List<String> solution = new ArrayList<>();
        String message = "SearchRecommendation result: ";

        SortedSet<Video> videos = genreVideoRatings.get(genre);

        if (videos == null) {
            return "SearchRecommendation cannot be applied!";
        }

        for (Video video : videos) {
            if (!history.containsKey(video.getTitle())) {
                solution.add(video.getTitle());
            }
        }

        if (solution.isEmpty()) {
            return "SearchRecommendation cannot be applied!";
        }

        return message + solution.toString();
    }
}
