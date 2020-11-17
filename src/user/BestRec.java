package user;

import database.MovieDB;
import database.ShowDB;
import database.UserDB;
import database.VideoDB;
import video.Movie;
import video.Show;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class BestRec {
    public static String best(MovieDB movieDB, ShowDB showDB, UserDB userDB,
                              String username) {
        List<Movie> movies = movieDB.getTopRatedMovies();
        List<Show> shows = showDB.getTopRatedShows();
        List<String> history = userDB.getHistory(username);
        int first = 0, second = 0;
        int movies_size = movies.size(), shows_size = shows.size();
        Movie movie;
        Show show;

        while(first < movies_size || second < shows_size) {
            if (first < movies_size && second < shows_size) {
                movie = movies.get(first);
                show = shows.get(second);
                if (movie.getTotalRating() > show.getTotalRating()) {
                    ++first;
                    if (!history.contains(movie.getTitle())) {
                        return movie.getTitle();
                    }
                } else {
                    ++second;
                    if (!history.contains(show.getTitle())) {
                        return show.getTitle();
                    }
                }
            } else if (first < movies_size) {
                movie = movies.get(first);
                ++first;
                if (!history.contains(movie.getTitle())) {
                    return movie.getTitle();
                }
            } else {
                show = shows.get(second);
                ++second;
                if (!history.contains(show.getTitle())) {
                    return show.getTitle();
                }
            }
        }

        return null;
    }
}
