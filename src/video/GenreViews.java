package video;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenreViews implements Comparable<GenreViews> {
    private final String title;
    private final List<String> videos = new ArrayList<>();
    private int views = 0;

    public GenreViews(String title) {
        this.title = title;
    }

    public void addViews() {
        ++views;
    }

    public void addVideos(String video) {
        videos.add(videos.size(), video);
    }

    public String getUnwatchedVideo(Map<String, Integer> history) {
        for (String video : videos) {
            if (!history.containsKey(video)) {
                return video;
            }
        }

        return null;
    }

    // Descending order
    @Override
    public int compareTo(GenreViews other) {
        int result = Integer.compare(this.views, other.views);
        if (result != 0) return -result;
        return this.title.compareTo(other.title);
    }
}
