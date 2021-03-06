package comparator;

import actor.Actor;

import java.util.Comparator;

public final class ActorNameCmp implements Comparator<Actor> {
    // Ascending order (alphabetical)
    @Override
    public int compare(final Actor self, final Actor other) {
        return self.getName().compareTo(other.getName());
    }
}
