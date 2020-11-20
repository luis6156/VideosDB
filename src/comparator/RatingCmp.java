package comparator;

import video.Video;

import java.util.Comparator;

public class RatingCmp implements Comparator<Video> {
    private final boolean isAscending;

    public RatingCmp(boolean isAscending) {
        this.isAscending = isAscending;
    }

    // Ascending/Descending order
    @Override
    public int compare(Video self, Video other) {
        int result;

        result = Double.compare(self.getTotalRating(),
                other.getTotalRating());
        if (result != 0) {
            if (isAscending) {
                return result;
            }
            return -result;
        }

        if (isAscending) {
            return self.getTitle().compareTo(other.getTitle());
        }
        return -self.getTitle().compareTo(other.getTitle());
    }
}
