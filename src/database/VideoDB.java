package database;

import fileio.MovieInputData;
import fileio.SerialInputData;
import video.Movie;
import video.Show;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VideoDB {
    private final HashMap<String, Movie> movies;
    private final HashMap<String, Show> shows;
    private final HashMap<String, Integer> favoriteMovies;
    private final HashMap<String, Integer> favoriteShows;

    public VideoDB() {
        movies = new HashMap<>();
        shows = new HashMap<>();
        favoriteMovies = new HashMap<>();
        favoriteShows = new HashMap<>();
    }

    public void populateVideoDB(List<MovieInputData> movies,
                                List<SerialInputData> shows) {
        for (MovieInputData movie : movies) {
            Movie newMovie = new Movie(
                    movie.getTitle(),
                    movie.getCast(),
                    movie.getGenres(),
                    movie.getYear(),
                    movie.getDuration()
            );
            this.movies.put(movie.getTitle(), newMovie);
        }
        for (SerialInputData show : shows) {
            Show newShow = new Show(
                    show.getTitle(),
                    show.getYear(),
                    show.getGenres(),
                    show.getCast(),
                    show.getNumberSeason(),
                    show.getSeasons()
            );
            this.shows.put(show.getTitle(), newShow);
        }
    }

    // Populate Favorites HashMap
    public void addFavorites(String title) {
        if (movies.containsKey(title)) {
            if (favoriteMovies.containsKey(title)) {
                int freq = favoriteMovies.get(title);
                favoriteMovies.put(title, ++freq);
                return;
            }

            favoriteMovies.put(title, 1);
        } else {
            if (favoriteShows.containsKey(title)) {
                int freq = favoriteShows.get(title);
                favoriteShows.put(title, ++freq);
                return;
            }

            favoriteShows.put(title, 1);
        }
    }

    public String topKFavorites(Boolean isMovie, String year, String genre,
                                int k) {
        //ArrayList<String>[] bucket = new ArrayList[favorites.size() + 1];
        List<String>[] bucket;
        int freq;

        if (isMovie) {
            bucket = new List[favoriteMovies.size() + 1];
            for (Map.Entry<String, Integer> set : favoriteMovies.entrySet()) {
                if (movies.get(set.getKey()).respectsFilters(genre, year)) {
                    freq = set.getValue();
                    if (bucket[freq] == null) {
                        bucket[freq] = new ArrayList<>();
                    }
                    bucket[freq].add(set.getKey());
                }
            }
        } else {
            bucket = new List[favoriteShows.size() + 1];
            for (Map.Entry<String, Integer> set : favoriteShows.entrySet()) {
                if (shows.get(set.getKey()).respectsFilters(genre, year)) {
                    freq = set.getValue();
                    if (bucket[freq] == null) {
                        bucket[freq] = new ArrayList<>();
                    }
                    bucket[freq].add(set.getKey());
                }
            }
        }

        List<String> sol = new ArrayList<>();
        for (int pos = bucket.length - 1; pos >= 0 && sol.size() < k; pos--) {
            if (bucket[pos] != null) {
                sol.addAll(bucket[pos]);
            }
        }

        return sol.toString();
    }
}
