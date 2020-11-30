package comparator;

import video.Video;

import java.util.Comparator;
import java.util.Map;

public final class RecomRatingCmp implements Comparator<Video> {
    private final Map<String, Integer> videoByIndex;

    public RecomRatingCmp(final Map<String, Integer> videoByIndex) {
        this.videoByIndex = videoByIndex;
    }

    // Descending order (rating/order of insertion)
    @Override
    public int compare(final Video self, final Video other) {
        int result, selfIndex, otherIndex;

        result = Double.compare(self.getTotalRating(), other.getTotalRating());

        // Check if first criteria fails
        if (result != 0) {
            return -result;
        } else {
            // Compare indexes
            selfIndex = videoByIndex.get(self.getTitle());
            otherIndex = videoByIndex.get(other.getTitle());
            // If its first time video is added to database, push it to tail to maintain
            // insertion order
            if (!videoByIndex.containsKey(self.getTitle())
                    || !videoByIndex.containsKey(other.getTitle())) {
                return 1;
            } else {
                // Second criteria
                result = Integer.compare(selfIndex, otherIndex);
                return result;
            }
        }
    }
}
