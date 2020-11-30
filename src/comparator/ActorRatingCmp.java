package comparator;

import actor.Actor;

import java.util.Comparator;

public class ActorRatingCmp implements Comparator<Actor> {
    // Ascending order (rating/alphabetical)
    @Override
    public int compare(final Actor self, final Actor other) {
        int result = Double.compare(self.getActorRating(), other.getActorRating());

        // Check if first criteria fails
        if (result != 0) {
            return result;
        }

        // Second criteria
        return self.getName().compareTo(other.getName());
    }
}
