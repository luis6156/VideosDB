package comparator;

import actor.Actor;

import java.util.Comparator;

public class ActorRatingCmp implements Comparator<Actor> {
    // Ascending order
    @Override
    public int compare(Actor self, Actor other) {
        int result = Double.compare(self.getActorRating(), other.getActorRating());
        if (result != 0) return result;
        return self.getName().compareTo(other.getName());
    }
}
