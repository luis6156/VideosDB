package comparator;

import video.Video;

import java.util.Comparator;

public class RatingCmp implements Comparator<Video> {
    // Ascending order
    @Override
    public int compare(Video self, Video other) {
        int result = Double.compare(self.getTotalRating(),
                other.getTotalRating());
        if (result != 0) return result;
        return self.getTitle().compareTo(other.getTitle());
    }
}
