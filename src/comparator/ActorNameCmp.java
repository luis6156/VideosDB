package comparator;

import actor.Actor;

import java.util.Comparator;

public class ActorNameCmp implements Comparator<Actor> {
    private final boolean isAscending;

    public ActorNameCmp(boolean isAscending) {
        this.isAscending = isAscending;
    }

    // Descending order
    @Override
    public int compare(Actor self, Actor other) {
        if (isAscending) {
            return self.getName().compareTo(other.getName());
        }
        return -self.getName().compareTo(other.getName());
    }
}
