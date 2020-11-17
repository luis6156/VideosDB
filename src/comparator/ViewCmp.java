package comparator;

import video.Video;

import java.util.Comparator;

public class ViewCmp implements Comparator<Video> {
    // Descending order
    @Override
    public int compare(Video self, Video other) {
        int result = Integer.compare(self.getViews(), other.getViews());
        if (result != 0) return -result;
        return self.getTitle().compareTo(other.getTitle());
    }
}
