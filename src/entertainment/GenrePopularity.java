package entertainment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenrePopularity implements Comparable<GenrePopularity> {
    private final String title;
    private final List<String> videos = new ArrayList<>();
    private int views = 0;

    /**
     * Set genre name for alphabetical sorting
     * @param title title of genre
     */
    public GenrePopularity(String title) {
        this.title = title;
    }

    // Increment total genre views
    public void addViews() {
        ++views;
    }

    /**
     * Append genres' videos (ordered by insertion from input)
     *
     * @param title video title to be added
     */
    public void addVideos(String title) {
        videos.add(videos.size(), title);
    }

    /**
     * @param history history of the user
     * @return the first video unwatched by the user or null if all videos are watched
     */
    public String getUnwatchedVideo(Map<String, Integer> history) {
        for (String video : videos) {
            if (!history.containsKey(video)) {
                return video;
            }
        }

        return null;
    }

    // Descending order comparator
    @Override
    public int compareTo(GenrePopularity other) {
        int result = Integer.compare(this.views, other.views);
        if (result != 0) return -result;
        return this.title.compareTo(other.title);
    }
}
