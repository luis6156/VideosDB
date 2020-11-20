package comparator;

import video.Video;

import java.util.Comparator;

public class FavoriteCmp implements Comparator<Video> {
    // Descending order
    @Override
    public int compare(Video self, Video other) {
        int result = Integer.compare(self.getFavorites(), other.getFavorites());
        if (result != 0) return -result;
        return -self.getTitle().compareTo(other.getTitle());
    }
}
