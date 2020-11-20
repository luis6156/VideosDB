package comparator;

import video.Video;

import java.util.Comparator;

public class ViewCmp implements Comparator<Video> {
    private final boolean isAscending;

    public ViewCmp(boolean isAscending) {
        this.isAscending = isAscending;
    }

    // Ascending/Descending order
    @Override
    public int compare(Video self, Video other) {
        int result;

        result = Integer.compare(self.getViews(), other.getViews());

        if (result != 0){
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
