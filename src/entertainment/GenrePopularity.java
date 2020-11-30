package entertainment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class GenrePopularity implements Comparable<GenrePopularity> {
    private final String title;
    private final List<String> videos = new ArrayList<>();
    private int views = 0;

    /**
     * Set genre name for alphabetical sorting
     *
     * @param title title of genre
     */
    public GenrePopularity(final String title) {
        this.title = title;
    }

    /**
     * Increment views
     */
    public void addViews() {
        ++views;
    }

    /**
     * Append genres' videos (ordered by insertion from input)
     *
     * @param videoName video title to be added
     */
    public void addVideos(final String videoName) {
        videos.add(videos.size(), videoName);
    }

    /**
     * @param history history of the user
     * @return the first video unwatched by the user or null if all videos are watched
     */
    public String getUnwatchedVideo(final Map<String, Integer> history) {
        for (String video : videos) {
            if (!history.containsKey(video)) {
                return video;
            }
        }

        return null;
    }

    // Descending order comparator (views/alphabetical)
    @Override
    public int compareTo(final GenrePopularity other) {
        int result = Integer.compare(this.views, other.views);

        // Check if first criteria fails
        if (result != 0) {
            return -result;
        }

        // Second criteria
        return this.title.compareTo(other.title);
    }
}
