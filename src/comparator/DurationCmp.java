package comparator;

import video.Video;

import java.util.Comparator;

public class DurationCmp implements Comparator<Video> {
    // Ascending order
    @Override
    public int compare(Video self, Video other) {
        int result = Integer.compare(self.getTotalDuration(), other.getTotalDuration());
        if (result != 0) return result;
        return self.getTitle().compareTo(other.getTitle());
    }
}
