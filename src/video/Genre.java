package video;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Genre implements Comparable<Genre> {
    private final String title;
    private final List<String> videos = new ArrayList<>();
    private int views = 0;

    public Genre(String title) {
        this.title = title;
    }

    public void addViews() {
        ++views;
    }

    public void addVideos(String video) {
        videos.add(videos.size(), video);
    }

    public String getUnwatchedVideo(List<String> history) {
        List<String> differences =
                videos.stream().filter(element -> !history.contains(element)).collect(Collectors.toList());

        if (differences.isEmpty()) {
            return null;
        }

        return differences.get(0);
    }

    @Override
    public int compareTo(Genre other) {
        int result = Integer.compare(this.views, other.views);
        if (result != 0) return -result;
        return this.title.compareTo(other.title);
    }
}
