package comparator;

import video.Video;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RecomRatingCmp implements Comparator<Video> {
    private final Map<String, Integer> videoByIndex;

    public RecomRatingCmp(Map<String, Integer> videoByIndex) {
        this.videoByIndex = videoByIndex;
    }

    // Descending order
    @Override
    public int compare(final Video self, final Video other) {
        int result, selfIndex, otherIndex;

        result = Double.compare(self.getTotalRating(),
                other.getTotalRating());
        if (result != 0) {
            return -result;
        } else {
            selfIndex = videoByIndex.get(self.getTitle());
            otherIndex = videoByIndex.get(other.getTitle());
            if (!videoByIndex.containsKey(self.getTitle()) || !videoByIndex.containsKey(other.getTitle())) {
                return 1;
            } else {
                result = Integer.compare(selfIndex, otherIndex);
                return result;
            }
        }
    }
}
