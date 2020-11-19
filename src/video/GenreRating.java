package video;

import java.util.*;

public class GenreRating {
    private final String title;
    private final List<String> videos = new ArrayList<>();
    private double rating = 0;
    private int num_ratings = 0;

    public GenreRating(String title) {
        this.title = title;
    }

    public void addRating(double rating) {
        this.rating += rating;
        this.rating /= ++num_ratings;
    }

    public void addVideos(String video) {
        videos.add(videos.size(), video);
    }

    public String getUnwatchedVideo(Map<String, Integer> history) {
        for (String video : videos) {
            if (!history.containsKey(video)) {
                return video;
            }
        }

        return null;
    }

//    public String getUnwatchedVideo(List<String> history) {
//        String message = "FavoriteRecommendation result: ";
//        boolean found = true;
//        for (Movie movie : favMovies) {
//            for (String s : history) {
//                if (movie.getTitle().equals(s)) {
//                    found = false;
//                    break;
//                }
//            }
//            if (found) {
//                return message + movie.getTitle();
//            }
//            found = true;
//        }
//        return message;
//    }

    // Ascending order
//    @Override
//    public int compareTo(GenreRating other) {
//        int result = Integer.compare(this.rating, other.rating);
//        if (result != 0) return -result;
//        return this.title.compareTo(other.title);
//    }
}
