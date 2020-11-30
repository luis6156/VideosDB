package comparator;

import video.Video;

import java.util.Comparator;

public class VideoRatingCmp implements Comparator<Video> {
    // Ascending order
    @Override
    public int compare(final Video self, final Video other) {
        int result;

        result = Double.compare(self.getTotalRating(),
                other.getTotalRating());

        if (result != 0) {
            return result;
        }

        return self.getTitle().compareTo(other.getTitle());
    }
}
