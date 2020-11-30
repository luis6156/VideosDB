package comparator;

import video.Video;

import java.util.Comparator;

public class VideoViewCmp implements Comparator<Video> {
    // Ascending order (views/alphabetical)
    @Override
    public int compare(final Video self, final Video other) {
        int result = Integer.compare(self.getViews(), other.getViews());

        // Check if first criteria fails
        if (result != 0){
            return result;
        }

        // Second criteria
        return self.getTitle().compareTo(other.getTitle());
    }
}
