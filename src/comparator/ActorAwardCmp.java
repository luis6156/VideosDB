package comparator;

import actor.Actor;

import java.util.Comparator;

public class ActorAwardCmp implements Comparator<Actor> {
    // Ascending order (number of awards/alphabetical)
    @Override
    public int compare(final Actor self, final Actor other) {
        int result = Integer.compare(self.getTotalAwards(), other.getTotalAwards());

        // Check if first criteria fails
        if (result != 0) {
            return result;
        }

        // Second criteria
        return self.getName().compareTo(other.getName());
    }
}
