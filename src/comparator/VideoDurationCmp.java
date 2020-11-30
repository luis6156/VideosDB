package comparator;

import video.Video;

import java.util.Comparator;

public class VideoDurationCmp implements Comparator<Video> {
    // Ascending order
    @Override
    public int compare(final Video self, final Video other) {
        int result;

        result = Integer.compare(self.getTotalDuration(),
                other.getTotalDuration());
        if (result != 0){
            return result;
        }

        return self.getTitle().compareTo(other.getTitle());
    }
}
