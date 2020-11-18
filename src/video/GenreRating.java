package video;

import comparator.RatingCmp;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class GenreRating {
    private final String title;
    private final SortedSet<Video> videoRatings =
            new TreeSet<>(new RatingCmp());

    public GenreRating(String title) {
        this.title = title;
    }

    public void resortRatings(Video oldVideo, Video newVideo) {
        if (videoRatings.contains(oldVideo)) {
            videoRatings.remove(oldVideo);
            videoRatings.add(newVideo);
        }
    }
    public void addVideos(Video video) {
        videoRatings.add(video);
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
}
