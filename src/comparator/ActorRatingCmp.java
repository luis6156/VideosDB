package comparator;

import actor.Actor;

import java.util.Comparator;

public class ActorRatingCmp implements Comparator<Actor> {
    private final boolean isAscending;

    public ActorRatingCmp(boolean isAscending) {
        this.isAscending = isAscending;
    }

    // Ascending/Descending order
    @Override
    public int compare(Actor self, Actor other) {
        int result = Double.compare(self.getActorRating(), other.getActorRating());
        if (result != 0) {
            if (isAscending) {
                return result;
            }
            return -result;
        }

        if (isAscending) {
            return self.getName().compareTo(other.getName());
        }
        return -self.getName().compareTo(other.getName());
    }
}
