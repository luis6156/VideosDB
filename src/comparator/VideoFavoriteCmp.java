package comparator;

import video.Video;

import java.util.Comparator;

public final class VideoFavoriteCmp implements Comparator<Video> {
    // Ascending order (favorites/alphabetical)
    @Override
    public int compare(final Video self, final Video other) {
        int result = Integer.compare(self.getFavorites(), other.getFavorites());

        // Check if first criteria fails
        if (result != 0) {
            return result;
        }

        // Second criteria
        return self.getTitle().compareTo(other.getTitle());
    }
}
