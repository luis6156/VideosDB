package comparator;

import video.Video;

import java.util.Comparator;

public class VideoRatingCmp implements Comparator<Video> {
    // Ascending order (rating/alphabetical)
    @Override
    public int compare(final Video self, final Video other) {
        int result = Double.compare(self.getTotalRating(), other.getTotalRating());

        // Check if first criteria fails
        if (result != 0) {
            return result;
        }

        // Second criteria
        return self.getTitle().compareTo(other.getTitle());
    }
}
